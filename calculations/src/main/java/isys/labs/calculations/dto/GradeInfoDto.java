package isys.labs.calculations.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GradeInfoDto {
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
}
