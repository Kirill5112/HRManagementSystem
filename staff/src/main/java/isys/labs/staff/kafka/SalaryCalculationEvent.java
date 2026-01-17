package isys.labs.staff.kafka;

import isys.labs.staff.dto.EmployeePositionGradeDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SalaryCalculationEvent {

    private Long employeeId;
    private LocalDate periodStart;
    private LocalDate periodEnd;

    private List<EmployeePositionGradeDto> positions;
}
