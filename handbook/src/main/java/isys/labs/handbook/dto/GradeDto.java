package isys.labs.handbook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GradeDto {

    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    @Positive
    private Integer level;

    @NotNull
    @PositiveOrZero
    private BigDecimal minSalary;

    @NotNull
    @PositiveOrZero
    private BigDecimal maxSalary;

    @NotBlank
    @Size(min = 3, max = 3) // ISO-код валюты
    private String currency;
}

