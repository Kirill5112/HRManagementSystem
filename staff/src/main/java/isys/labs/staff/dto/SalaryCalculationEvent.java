package isys.labs.staff.dto;

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
