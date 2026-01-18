package isys.labs.calculations.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import isys.labs.calculations.client.HandbookClient;
import isys.labs.calculations.dto.BenefitCategoryInfoDto;
import isys.labs.calculations.dto.EmployeePositionGradeDto;
import isys.labs.calculations.dto.GradeInfoDto;
import isys.labs.calculations.dto.TaxRateInfoDto;
import isys.labs.calculations.kafka.SalaryCalculationEvent;
import isys.labs.calculations.entity.CalculationHistory;
import isys.labs.calculations.entity.SalaryCalculation;
import isys.labs.calculations.repository.CalculationHistoryRepository;
import isys.labs.calculations.repository.SalaryCalculationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SalaryCalculationService {

    private final SalaryCalculationRepository salaryCalculationRepository;
    private final CalculationHistoryRepository calculationHistoryRepository;
    private final HandbookClient handbookClient;

    @Transactional
    public void handleCalculationEvent(SalaryCalculationEvent event) {

        BigDecimal grossSalary = BigDecimal.ZERO;
        for (EmployeePositionGradeDto positionGrade : event.getPositions()) {
            GradeInfoDto grade = handbookClient.getGrade(positionGrade.getGradeId());
            BigDecimal salaryMultiplier = handbookClient.getPositionGrade(
                    positionGrade.getPositionId(),
                    positionGrade.getGradeId()
            ).getSalaryMultiplier();
            BigDecimal baseFromGrade = grade.getMinSalary();
            BigDecimal monthlyBase = baseFromGrade.multiply(salaryMultiplier);// минимальная * на ставку
            long daysInPeriod = ChronoUnit.DAYS.between(event.getPeriodStart(), event.getPeriodEnd()) + 1;
            long daysInMonth = YearMonth.from(event.getPeriodStart()).lengthOfMonth();

            BigDecimal grossPart = monthlyBase
                    .multiply(BigDecimal.valueOf(daysInPeriod))
                    .divide(BigDecimal.valueOf(daysInMonth), 2, RoundingMode.HALF_UP);
            grossSalary = grossSalary.add(grossPart);
        }
        BigDecimal bonuses = BigDecimal.ZERO;
        BigDecimal deductions = BigDecimal.ZERO;
        TaxRateInfoDto taxRateInfo = handbookClient.getTaxRate(event.getPeriodEnd());
        if (taxRateInfo.getRate() == null)
            throw new IllegalStateException("не найдено налоговой ставки");
        BigDecimal taxRate = taxRateInfo.getRate();
        BigDecimal base = grossSalary
                .add(bonuses
                        .subtract(deductions));

        Long benefitCategoryId = event.getBenefitCategoryId();
        BigDecimal taxFree = BigDecimal.ZERO;
        if (benefitCategoryId != null) {
            BenefitCategoryInfoDto benefitCategoryInfoDto = handbookClient.getBenefitCategory(benefitCategoryId);
            taxFree = benefitCategoryInfoDto != null ? benefitCategoryInfoDto.getTaxFreeAmount() : BigDecimal.ZERO;
        }

        BigDecimal taxableBase = base.subtract(taxFree);
        if (taxableBase.compareTo(BigDecimal.ZERO) < 0) {
            taxableBase = BigDecimal.ZERO;
        }


        BigDecimal taxes = taxableBase.multiply(taxRate);
        BigDecimal social = taxableBase.multiply(new BigDecimal("0.30"));
        BigDecimal net = taxableBase.subtract(taxes).subtract(social);

        SalaryCalculation calc = new SalaryCalculation();
        calc.setEmployeeId(event.getEmployeeId());
        calc.setPeriodStart(event.getPeriodStart());
        calc.setPeriodEnd(event.getPeriodEnd());
        calc.setGrossSalary(grossSalary);
        calc.setBonuses(bonuses);
        calc.setDeductions(deductions);
        calc.setTaxRate(taxRate);
        calc.setTaxesAmount(taxes);
        calc.setSocialContributions(social);
        calc.setNetSalary(net);
        calc.setStatus(SalaryCalculation.CalculationStatus.CALCULATED);
        calc.setCalculatedAt(LocalDateTime.now());
        calc.setCreatedAt(LocalDateTime.now());

        salaryCalculationRepository.save(calc);

        CalculationHistory step = new CalculationHistory();
        step.setCalculation(calc);
        step.setStepName("APPLY_TAX_AND_SOCIAL");
        step.setStepOrder(1);
        step.setInputData(toJson(event));
        step.setOutputData(toJson(Map.of(
                "rate", taxRate,
                "taxes", taxes,
                "socialContributions", social,
                "netSalary", net
        )));
        step.setCreatedAt(LocalDateTime.now());

        calculationHistoryRepository.save(step);
    }

    private String toJson(Object o) {
        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}

