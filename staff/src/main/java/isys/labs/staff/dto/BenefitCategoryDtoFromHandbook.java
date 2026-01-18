package isys.labs.staff.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BenefitCategoryDtoFromHandbook {
    private String name;
    private String description;
    private BigDecimal taxFreeAmount;
}
