package isys.labs.staff.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SalaryCalculationRequest {
    private LocalDate periodStart;
    private LocalDate periodEnd;
}


