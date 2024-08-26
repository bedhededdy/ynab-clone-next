package com.ynab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // *ECP FIXME: PUT THE URL IN THE APPLICATION.PROPERTIES FILE
                registry.addMapping("/**").allowedMethods("GET", "PATCH", "POST", "PUT", "DELETE");
                    // .allowedOrigins("http://localhost:3000");
            }
        };
    }
}
