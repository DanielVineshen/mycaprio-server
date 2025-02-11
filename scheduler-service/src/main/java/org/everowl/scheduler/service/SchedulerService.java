package org.everowl.scheduler.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan(basePackages = "org.everowl")
@EnableScheduling
@EnableTransactionManagement
public class SchedulerService {
    public static void main(String[] args) {
        SpringApplication.run(SchedulerService.class, args);
    }
}
