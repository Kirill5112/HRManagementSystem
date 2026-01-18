package isys.labs.calculations.kafka;

import isys.labs.calculations.dto.EmployeePositionGradeDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SalaryCalculationEvent {

    private Long employeeId;
    private Long benefitCategoryId;
    private LocalDate periodStart;
    private LocalDate periodEnd;

    private List<EmployeePositionGradeDto> positions;
}
