package com.github.ssproessig.tinkerpop.tinkerpop_rest.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(new Components())
        .info(
            new Info()
                .title("TinkerPop REST API for railML 3")
                .description("Provides low-level API to access railML information via REST"));
  }
}
