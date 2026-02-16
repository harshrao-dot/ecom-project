package org.harshdev.ecom;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "E-com API", version = "1.0", description = "Ecommerce Backend"))
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}