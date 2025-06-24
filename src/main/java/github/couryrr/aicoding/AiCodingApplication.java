package github.couryrr.aicoding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "Customer Management API", version = "1.0.0", description = "RESTful API for managing customers and their purchases"))
@SpringBootApplication
public class AiCodingApplication {
    public static void main(String[] args) {
        SpringApplication.run(AiCodingApplication.class, args);
    }
}
