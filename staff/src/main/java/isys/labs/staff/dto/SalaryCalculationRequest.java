package isys.labs.staff.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SalaryCalculationRequest {
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private BigDecimal grossSalary;
    private BigDecimal bonuses;
    private BigDecimal deductions;
}

