package com.dam.login.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


/**
 * Configuración de Swagger/OpenAPI para la documentación de la API.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configura la documentación OpenAPI (Swagger) para la API.
     * 
     * @return La configuración de OpenAPI
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GUZPASEN API")
                        .description("API para la gestión de usuarios, roles y módulos del sistema GUZPASEN")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("GUZPASEN")
                                .url("https://guzpasen.edu")
                                .email("info@guzpasen.edu"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8084")
                                .description("Servidor de desarrollo"),
                        new Server()
                                .url("https://api.guzpasen.edu")
                                .description("Servidor de producción")))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Introduce el token JWT para autenticarte")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("Bearer Authentication"));
    }
}
