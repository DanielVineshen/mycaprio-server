package org.everowl.core.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.everowl")
public class CoreService {
    public static void main(String[] args) {
        SpringApplication.run(CoreService.class, args);
    }
}