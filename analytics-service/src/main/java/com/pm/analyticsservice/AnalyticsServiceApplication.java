package com.pm.analyticsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AnalyticsServiceApplication {

    public static void main(String[] args) {
        System.out.println("Starting Analytics Service Application...");
        SpringApplication.run(AnalyticsServiceApplication.class, args);
    }

}
