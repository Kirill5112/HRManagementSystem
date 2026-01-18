package isys.labs.staff.service;

import isys.labs.staff.dto.EmployeePositionGradeDto;
import isys.labs.staff.entity.Employee;
import isys.labs.staff.entity.EmployeePositionGrade;
import isys.labs.staff.kafka.SalaryCalculationEvent;
import isys.labs.staff.kafka.SalaryCalculationProducer;
import isys.labs.staff.kafka.SalaryCalculationRequest;
import isys.labs.staff.repository.EmployeePositionGradeRepository;
import isys.labs.staff.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaryCalculationOrchestrator {

    private final EmployeePositionGradeRepository employeePositionGradeRepository;
    private final SalaryCalculationProducer salaryCalculationProducer;
    private final EmployeeRepository employeeRepository;

    public void startSalaryCalculation(Long employeeId, SalaryCalculationRequest request) {
        List<EmployeePositionGrade> epgList =
                employeePositionGradeRepository.findByEmployeeId(employeeId);

        List<EmployeePositionGradeDto> positions = epgList.stream()
                .map(epg -> {
                    EmployeePositionGradeDto dto = new EmployeePositionGradeDto();
                    dto.setPositionId(epg.getPositionId());
                    dto.setGradeId(epg.getGradeId());
                    return dto;
                })
                .toList();

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + employeeId));

        SalaryCalculationEvent event = new SalaryCalculationEvent();
        event.setEmployeeId(employeeId);
        event.setBenefitCategoryId(employee.getBenefitCategoryId());
        event.setPeriodStart(request.getPeriodStart());
        event.setPeriodEnd(request.getPeriodEnd());
        event.setPositions(positions);

        salaryCalculationProducer.sendSalaryCalculation(event);
    }
}

