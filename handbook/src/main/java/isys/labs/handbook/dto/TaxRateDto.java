package isys.labs.handbook.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TaxRateDto {

    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    @PositiveOrZero
    @Digits(integer = 3, fraction = 2) // например 13.00, 20.00
    private BigDecimal rate;

    @Size(max = 255)
    private String description;

    @NotNull
    private LocalDate validFrom;

    private LocalDate validTo;
}

