package isys.labs.calculations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenefitCategoryInfoDto {
    private BigDecimal taxFreeAmount;
}
