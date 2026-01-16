package isys.labs.staff.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "HR Staff Service API",
                version = "1.0",
                description = "Кадровое обеспечение: Сотрудники, департаменты"
        )
)
@Configuration
public class OpenApiConfig {
}

