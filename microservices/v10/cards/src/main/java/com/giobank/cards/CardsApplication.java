package com.giobank.cards;

import com.giobank.cards.dto.CardsContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@OpenAPIDefinition(
        info = @Info(
                title = "Cards microservice REST Api documentation",
                description = "GioBank Cards microservice REST Api Documentation",
                version = "v1",
                contact = @Contact(
                        name = "Giovani Eugenio",
                        email = "giovani.eugenio.sp@gmail.com",
                        url = "https://www.giobank.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.giobank.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "GioBank Cards microservice REST Api Documentation",
                url = "https://www.giobank.com/swagger-ui/index.html"
        )
)
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties(value = CardsContactInfoDto.class)
@SpringBootApplication
public class CardsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardsApplication.class, args);
    }

}
