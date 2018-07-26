package com.security.web;

import com.security.web.configuration.AppConfig;
import org.springframework.boot.SpringApplication;

public class StartApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppConfig.class, args);
    }
}
