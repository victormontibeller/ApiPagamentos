package com.pagamento.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Development");

        Contact contact = new Contact();
        contact.setEmail("victormontibeller@hotmail.com");
        contact.setName("VMontibeller");

        Info info = new Info()
                .title("API Pagamentos - Hackathon FIAP")
                .version("1.0")
                .contact(contact)
                .description("Documentação de endpoints da Hackathon FIAP");

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}