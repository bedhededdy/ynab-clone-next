package com.ynab.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                // *ECP FIXME: PUT THE URL IN THE APPLICATION.PROPERTIES FILE

                // We need to set allowed origin and allow credentials so that
                // credentials in cookie are able to be used from the request
                // and so that we are not vulnerable to CSRF attacks
                registry.addMapping("/**")
                    .allowedMethods("GET", "PATCH", "POST", "PUT", "DELETE")
                    .allowedOrigins("http://localhost:3000")
                    .allowCredentials(true);
            }
        };
    }
}
