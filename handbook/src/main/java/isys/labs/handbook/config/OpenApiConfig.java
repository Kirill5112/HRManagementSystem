package isys.labs.handbook.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "HR HandBook Service API",
                version = "1.0",
                description = "Справочник должностей, грейдов, ставок и льгот"
        )
)
@Configuration
public class OpenApiConfig {
}
