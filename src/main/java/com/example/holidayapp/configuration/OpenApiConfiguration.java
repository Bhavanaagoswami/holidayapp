package com.example.holidayapp.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * open api configuration.
 */
@Configuration
public class OpenApiConfiguration {

    /**
     * Adding open api document.
     * @return OpenApi.
     */
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                .title("Holiday App")
                .version("1.0")
                .description("APIs for holiday details"));
    }
}
