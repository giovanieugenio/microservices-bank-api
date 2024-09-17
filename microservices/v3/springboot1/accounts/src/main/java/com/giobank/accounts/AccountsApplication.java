package com.giobank.accounts;

import com.giobank.accounts.dto.AccountsContactInfoDto;
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
                title = "Accounts microservice REST Api documentation",
                description = "GioBank Accounts microservice REST Api Documentation",
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
                description = "GioBank Accounts microservice REST Api Documentation",
                url = "https://www.giobank.com/swagger-ui/index.html"
        )
)
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties(value = AccountsContactInfoDto.class)
@SpringBootApplication
public class AccountsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsApplication.class, args);
    }

}
