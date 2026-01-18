package isys.labs.calculations.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CashBalanceDto {
    private Long id;
    private LocalDate date;
    private Long departmentId;
    private String currency;
    private BigDecimal balance;
}
