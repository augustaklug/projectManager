package com.klug.noteservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication
@EnableDiscoveryClient
public class NoteServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteServiceApplication.class, args);
    }

}
