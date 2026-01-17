package isys.labs.staff.service;

import isys.labs.staff.dto.EmployeePositionGradeDto;
import isys.labs.staff.entity.EmployeePositionGrade;
import isys.labs.staff.kafka.SalaryCalculationEvent;
import isys.labs.staff.kafka.SalaryCalculationProducer;
import isys.labs.staff.kafka.SalaryCalculationRequest;
import isys.labs.staff.repository.EmployeePositionGradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalaryCalculationOrchestrator {

    private final EmployeePositionGradeRepository employeePositionGradeRepository;
    private final SalaryCalculationProducer salaryCalculationProducer;

    public void startSalaryCalculation(Long employeeId, SalaryCalculationRequest request) {
        // 1. вытаскиваем все должности+грейды сотрудника
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

        // 2. формируем событие
        SalaryCalculationEvent event = new SalaryCalculationEvent();
        event.setEmployeeId(employeeId);
        event.setPeriodStart(request.getPeriodStart());
        event.setPeriodEnd(request.getPeriodEnd());
        event.setPositions(positions); // заполняем программно

        // 3. отправляем в Kafka
        salaryCalculationProducer.sendSalaryCalculation(event);
    }
}

