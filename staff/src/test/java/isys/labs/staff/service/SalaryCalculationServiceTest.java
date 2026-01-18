package isys.labs.staff.service;

import isys.labs.staff.dto.EmployeePositionGradeDto;
import isys.labs.staff.entity.Employee;
import isys.labs.staff.entity.EmployeePositionGrade;
import isys.labs.staff.kafka.SalaryCalculationEvent;
import isys.labs.staff.kafka.SalaryCalculationProducer;
import isys.labs.staff.kafka.SalaryCalculationRequest;
import isys.labs.staff.repository.EmployeePositionGradeRepository;
import isys.labs.staff.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SalaryCalculationServiceTest {

    @Mock
    private EmployeePositionGradeRepository employeePositionGradeRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private SalaryCalculationProducer salaryCalculationProducer;

    @InjectMocks
    private SalaryCalculationOrchestrator service;  // предполагаем, что метод в сервисе

    private EmployeePositionGrade epg1;
    private EmployeePositionGrade epg2;
    private Employee employee;
    private SalaryCalculationRequest request;

    @BeforeEach
    void setUp() {
        epg1 = new EmployeePositionGrade();
        epg1.setPositionId(1L);
        epg1.setGradeId(10L);

        epg2 = new EmployeePositionGrade();
        epg2.setPositionId(2L);
        epg2.setGradeId(20L);

        employee = new Employee();
        employee.setId(123L);
        employee.setBenefitCategoryId(5L);

        request = new SalaryCalculationRequest();
        request.setPeriodStart(LocalDate.of(2026, 1, 1));
        request.setPeriodEnd(LocalDate.of(2026, 1, 31));
    }

    @Test
    void startSalaryCalculation_ShouldSendEvent_WhenEmployeeAndPositionsExist() {
        // given
        Long employeeId = 123L;
        List<EmployeePositionGrade> epgList = List.of(epg1, epg2);

        when(employeePositionGradeRepository.findByEmployeeId(employeeId)).thenReturn(epgList);
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        createExpectedEvent(employeeId);

        // when
        service.startSalaryCalculation(employeeId, request);

        // then
        verify(employeePositionGradeRepository).findByEmployeeId(employeeId);
        verify(employeeRepository).findById(employeeId);
        verify(salaryCalculationProducer).sendSalaryCalculation(argThat(event ->
                event.getEmployeeId().equals(employeeId) &&
                event.getBenefitCategoryId().equals(5L) &&
                event.getPeriodStart().equals(request.getPeriodStart()) &&
                event.getPositions().size() == 2 &&
                event.getPositions().getFirst().getPositionId().equals(1L) &&
                event.getPositions().getFirst().getGradeId().equals(10L)
        ));
    }

    @Test
    void startSalaryCalculation_ShouldThrowException_WhenEmployeeNotFound() {
        // given
        Long employeeId = 999L;
        when(employeePositionGradeRepository.findByEmployeeId(employeeId)).thenReturn(List.of());
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> service.startSalaryCalculation(employeeId, request));
        assertEquals("Employee not found: " + employeeId, exception.getMessage());

        verify(salaryCalculationProducer, never()).sendSalaryCalculation(any());
    }

    @Test
    void startSalaryCalculation_ShouldThrowException_WhenNoPositions() {
        // given
        Long employeeId = 123L;
        when(employeePositionGradeRepository.findByEmployeeId(employeeId)).thenReturn(List.of());
        lenient().when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        // when & then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.startSalaryCalculation(employeeId, request));
        assertEquals("No positions found for employee: " + employeeId, exception.getMessage());

        verify(salaryCalculationProducer, never()).sendSalaryCalculation(any());
    }

    private void createExpectedEvent(Long employeeId) {
        SalaryCalculationEvent event = new SalaryCalculationEvent();
        event.setEmployeeId(employeeId);
        event.setBenefitCategoryId(5L);
        event.setPeriodStart(request.getPeriodStart());
        event.setPeriodEnd(request.getPeriodEnd());
        List<EmployeePositionGradeDto> positions = List.of(
                new EmployeePositionGradeDto(1L, 10L),
                new EmployeePositionGradeDto(2L, 20L)
        );
        event.setPositions(positions);
    }
}

