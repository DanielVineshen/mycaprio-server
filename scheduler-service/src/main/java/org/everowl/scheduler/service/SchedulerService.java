package org.everowl.scheduler.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.everowl")
public class SchedulerService {
    public static void main(String[] args) {
        SpringApplication.run(SchedulerService.class, args);
    }
}
