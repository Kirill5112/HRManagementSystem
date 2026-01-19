package isys.labs.calculations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeePositionGradeDto {
    private Long positionId;
    private Long gradeId;
}

