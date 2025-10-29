package com.senai.senai_conta_bancaria_spring_api.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI contaBancariaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Conta Bancária")
                        .description("API para gerenciamento de contas bancárias, clientes e transações.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("DEVSENAI CONTA BANCÁRIA")
                                .email("suporte@contabancaria.com")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));


    }
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}
