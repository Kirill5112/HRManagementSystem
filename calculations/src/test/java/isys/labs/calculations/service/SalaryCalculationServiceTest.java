package isys.labs.calculations.service;

import isys.labs.calculations.client.HandbookClient;
import isys.labs.calculations.dto.BenefitCategoryInfoDto;
import isys.labs.calculations.dto.EmployeePositionGradeDto;
import isys.labs.calculations.dto.GradeInfoDto;
import isys.labs.calculations.dto.PositionGradeInfoDto;
import isys.labs.calculations.dto.SalaryCalculationEvent;
import isys.labs.calculations.dto.TaxRateInfoDto;
import isys.labs.calculations.entity.CalculationHistory;
import isys.labs.calculations.repository.CalculationHistoryRepository;
import isys.labs.calculations.repository.SalaryCalculationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaryCalculationServiceTest {

    @Mock
    private HandbookClient handbookClient;

    @Mock
    private SalaryCalculationRepository salaryCalculationRepository;

    @Mock
    private CalculationHistoryRepository calculationHistoryRepository;

    @InjectMocks
    private SalaryCalculationService service;

    @Test
    void handleCalculationEvent_ShouldCalculateSalaryCorrectly() {
        // given
        SalaryCalculationEvent event = createEvent();
        GradeInfoDto grade = new GradeInfoDto();
        grade.setMinSalary(new BigDecimal("10000.00"));
        PositionGradeInfoDto positionGrade = new PositionGradeInfoDto();
        positionGrade.setSalaryMultiplier(new BigDecimal("1.2"));
        TaxRateInfoDto taxRateInfo = new TaxRateInfoDto();
        taxRateInfo.setRate(new BigDecimal("0.13"));
        BenefitCategoryInfoDto benefit = new BenefitCategoryInfoDto();
        benefit.setTaxFreeAmount(new BigDecimal("1000.00"));

        when(handbookClient.getGrade(1L)).thenReturn(grade);
        when(handbookClient.getPositionGrade(1L, 1L)).thenReturn(positionGrade);
        when(handbookClient.getTaxRate(event.getPeriodEnd())).thenReturn(taxRateInfo);
        when(handbookClient.getBenefitCategory(1L)).thenReturn(benefit);

        // when
        service.handleCalculationEvent(event);

        verify(salaryCalculationRepository).save(argThat(calc -> {
            boolean grossMatch = calc.getGrossSalary().compareTo(new BigDecimal("12000.00")) == 0;
            boolean taxableMatch = calc.getTaxesAmount().compareTo(new BigDecimal("1430.00")) == 0;   // (12000-1000)*0.13
            boolean socialMatch = calc.getSocialContributions().compareTo(new BigDecimal("3300.00")) == 0; //(12000-1000)*0.3
            boolean netMatch = calc.getNetSalary().compareTo(new BigDecimal("6270.00")) == 0;
            return grossMatch && taxableMatch && socialMatch && netMatch;
        }));
        verify(calculationHistoryRepository).save(any(CalculationHistory.class));
    }

    @Test
    void handleCalculationEvent_ShouldHandleNoBenefitCategory() {
        // given
        SalaryCalculationEvent event = createEvent();
        event.setBenefitCategoryId(null);
        GradeInfoDto grade = new GradeInfoDto();
        grade.setMinSalary(new BigDecimal("10000.00"));
        PositionGradeInfoDto positionGrade = new PositionGradeInfoDto();
        positionGrade.setSalaryMultiplier(new BigDecimal("1.2"));
        TaxRateInfoDto taxRateInfo = new TaxRateInfoDto();
        taxRateInfo.setRate(new BigDecimal("0.13"));

        when(handbookClient.getGrade(1L)).thenReturn(grade);
        when(handbookClient.getPositionGrade(1L, 1L)).thenReturn(positionGrade);
        when(handbookClient.getTaxRate(event.getPeriodEnd())).thenReturn(taxRateInfo);

        // when
        service.handleCalculationEvent(event);

        // then
        verify(salaryCalculationRepository).save(argThat(calc -> {
            boolean grossMatch = calc.getGrossSalary().compareTo(new BigDecimal("12000.00")) == 0;
            boolean taxesMatch = calc.getTaxesAmount().compareTo(new BigDecimal("1560.00")) == 0;      // 12000 * 0.13
            boolean socialMatch = calc.getSocialContributions().compareTo(new BigDecimal("3600.00")) == 0; // 12000 * 0.30
            boolean netMatch = calc.getNetSalary().compareTo(new BigDecimal("6840.00")) == 0;          // 12000 - 1560 - 3600
            return taxesMatch && socialMatch && netMatch && grossMatch;
        }));    }

    @Test
    void handleCalculationEvent_ShouldSetTaxableBaseToZero_WhenNegative() {
        // given
        SalaryCalculationEvent event = createEvent();
        GradeInfoDto grade = new GradeInfoDto();  // маленький gross
        grade.setMinSalary(new BigDecimal("100.00"));
        PositionGradeInfoDto positionGrade = new PositionGradeInfoDto();
        positionGrade.setSalaryMultiplier(new BigDecimal("1.0"));
        TaxRateInfoDto taxRateInfo = new TaxRateInfoDto();
        taxRateInfo.setRate(new BigDecimal("0.13"));
        BenefitCategoryInfoDto benefit = new BenefitCategoryInfoDto(new BigDecimal("10000.00"));  // большой taxFree

        when(handbookClient.getGrade(1L)).thenReturn(grade);
        when(handbookClient.getPositionGrade(1L, 1L)).thenReturn(positionGrade);
        when(handbookClient.getTaxRate(event.getPeriodEnd())).thenReturn(taxRateInfo);
        when(handbookClient.getBenefitCategory(1L)).thenReturn(benefit);

        // when
        service.handleCalculationEvent(event);

        // then
        verify(salaryCalculationRepository).save(argThat(calc ->
                calc.getTaxesAmount().compareTo(BigDecimal.ZERO) == 0 &&
                calc.getSocialContributions().compareTo(BigDecimal.ZERO) == 0
        ));
    }

    @Test
    void handleCalculationEvent_ShouldThrowException_WhenNoTaxRate() {
        // given
        SalaryCalculationEvent event = createEvent();
        TaxRateInfoDto taxRateInfo = new TaxRateInfoDto();
        taxRateInfo.setRate(null);

        GradeInfoDto grade = new GradeInfoDto();
        PositionGradeInfoDto positionGrade = new PositionGradeInfoDto();
        positionGrade.setSalaryMultiplier(new BigDecimal("1.0"));
        grade.setMinSalary(new BigDecimal("10000"));
        when(handbookClient.getGrade(1L)).thenReturn(grade);
        when(handbookClient.getPositionGrade(1L, 1L)).thenReturn(positionGrade);
        when(handbookClient.getTaxRate(event.getPeriodEnd())).thenReturn(taxRateInfo);

        // when & then
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> service.handleCalculationEvent(event));
        verifyNoInteractions(salaryCalculationRepository, calculationHistoryRepository);
    }

    private SalaryCalculationEvent createEvent() {
        SalaryCalculationEvent event = new SalaryCalculationEvent();
        event.setEmployeeId(1L);
        event.setPeriodStart(LocalDate.of(2026, 1, 1));
        event.setPeriodEnd(LocalDate.of(2026, 1, 31));
        event.setBenefitCategoryId(1L);
        event.setPositions(List.of(new EmployeePositionGradeDto(1L, 1L)));  // 31 дней в январе
        return event;
    }
}