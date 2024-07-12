package com.example.final_project_java;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

   @Bean
   public OpenAPI customOpenAPI() {
      return new OpenAPI()
            .components(new Components())
            .info(new Info().title("PlugNGo API ")
                  .description("Final Project API")
                  .version("v1.0.0")
            );

   }


}












































