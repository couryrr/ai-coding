package github.couryrr.aicoding.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Customer Management API",
        version = "1.0.0",
        description = "RESTful API for managing customers and their purchases"
    )
)
public class OpenApiConfiguration implements WebMvcConfigurer {
}