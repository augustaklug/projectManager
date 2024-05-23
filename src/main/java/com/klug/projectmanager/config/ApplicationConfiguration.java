package com.klug.projectmanager.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties
@ConfigurationPropertiesScan
public class ApplicationConfiguration {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiryDuration}")
    private Long jwtExpiryDuration;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    // Additional beans and configuration properties can be added here

    // Example of a custom configuration properties class
    @Bean
    public JwtProperties jwtProperties() {
        return new JwtProperties(jwtSecret, jwtExpiryDuration);
    }

    public static class JwtProperties {
        private final String secret;
        private final Long expiryDuration;

        public JwtProperties(String secret, Long expiryDuration) {
            this.secret = secret;
            this.expiryDuration = expiryDuration;
        }

        public String getSecret() {
            return secret;
        }

        public Long getExpiryDuration() {
            return expiryDuration;
        }
    }
}
