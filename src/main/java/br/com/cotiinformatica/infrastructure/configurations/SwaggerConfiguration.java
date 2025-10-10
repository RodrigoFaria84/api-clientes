package br.com.cotiinformatica.infrastructure.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Clientes - Coti Informatica")
                        .version("1.0.0")
                        .description("API para gerenciamento de clientes, criada no curso Java Avançado – Formação Arquiteto")
                        .contact(new Contact()
                                .name("Sergio Mendes")
                                .email("contato@cotiinformatica.com.br")
                                .url("https://www.cotiinformatica.com.br")
                        )
                );
    }
}
