package com.technical.workshop.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Swagger {
    @Bean
    public OpenAPI customAPI() {
        return new OpenAPI().info(new Info().title("Workshop API"));
    }
}
