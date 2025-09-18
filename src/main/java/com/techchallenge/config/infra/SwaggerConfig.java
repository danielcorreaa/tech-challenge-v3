package com.techchallenge.config.infra;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Tech Challenge REST API", version = "1.0",
        description = "REST API para cadastro de clientes, produtos, e pedidos",
        contact = @Contact(name = "Daniel A. Correa", email = "daniel.cor@outlook.com")),
        security = {@SecurityRequirement(name = "bearerToken")}
)
@SecuritySchemes({
        @SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP, 
                        scheme = "bearer", bearerFormat = "JWT")
})
public class SwaggerConfig {

	
}
