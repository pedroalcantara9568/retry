package com.alquiteto.retry.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "API Open Banking Payments SICREDI", version = "v1"))
public class OpenAPIConfig {
    @Bean
    public OpenAPI buildOpenAPI() {
        var info = getUnfi();
        var docs = getExternalDocs();
        return new OpenAPI()
                .info(info)
                .externalDocs(docs);
    }

    private io.swagger.v3.oas.models.info.Info getUnfi() {
        var license = getLiance();
        return new io.swagger.v3.oas.models.info.Info()
                .title("Sistema Open Banking - Sicredi")
                .description("API Responsável pela persistencia de iniciação de pagamentos")
                .version("1.0.0v")
                .license(license);
    }

    private License getLiance() {
        return new License()
                .name("Apache License Version 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0\"");
    }

    private ExternalDocumentation getExternalDocs() {
        return new ExternalDocumentation()
                .description("Mais informações sobre: Febraban - Open Banking")
                .url("https://openbanking-brasil.github.io/areadesenvolvedor/");
    }
}
