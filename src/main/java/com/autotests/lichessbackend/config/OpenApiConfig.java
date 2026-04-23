package com.autotests.lichessbackend.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI lichessStatsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Lichess Stats Backend API")
                        .version("1.0")
                        .description("Backend for fetching Lichess player games and calculating statistics"));
    }
}
