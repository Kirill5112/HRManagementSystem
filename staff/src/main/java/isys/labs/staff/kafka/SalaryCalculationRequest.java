package isys.labs.staff.kafka;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SalaryCalculationRequest {
    private LocalDate periodStart;
    private LocalDate periodEnd;
}


