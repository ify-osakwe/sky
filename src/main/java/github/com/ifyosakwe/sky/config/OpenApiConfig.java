package github.com.ifyosakwe.sky.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Sky Weather API",
        version = "1.0",
        description = "Weather and City search API"
    )
)
public class OpenApiConfig {
    
}
