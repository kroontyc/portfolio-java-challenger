package com.portfolio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Portfolio Management API")
                        .description("API para gerenciamento de portfolio de projetos corporativos")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Portfolio Team")))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .schemaRequirement("basicAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic"));
    }
}
