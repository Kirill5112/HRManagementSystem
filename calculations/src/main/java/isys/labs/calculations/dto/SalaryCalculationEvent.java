package isys.labs.calculations.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SalaryCalculationEvent {

    private Long employeeId;
    private LocalDate periodStart;
    private LocalDate periodEnd;

    private BigDecimal grossSalary;
    private BigDecimal bonuses;
    private BigDecimal deductions;
}

