package isys.labs.handbook.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PositionGradeDto {

    private Long id;

    @NotNull
    private Long positionId;

    @NotNull
    private Long gradeId;

    @NotNull
    @Positive
    private BigDecimal salaryMultiplier;
}

