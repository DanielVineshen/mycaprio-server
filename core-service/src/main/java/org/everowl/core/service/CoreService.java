package org.everowl.core.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = "org.everowl")
@EnableTransactionManagement
public class CoreService {
    public static void main(String[] args) {
        SpringApplication.run(CoreService.class, args);
    }
}