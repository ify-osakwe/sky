package github.com.ifyosakwe.sky.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.info.Contact;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Sky Weather API",
                version = "1.0",
                description = "Weather and City search API",
                contact = @Contact(
                        name = "Ify Osakwe",
                        email = "ifeakachukwuosakwe@gmail.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Sky (GitHub Repo)",
                url = "https://github.com/ify-osakwe/sky"
        )
)
public class OpenApiConfig {

}
