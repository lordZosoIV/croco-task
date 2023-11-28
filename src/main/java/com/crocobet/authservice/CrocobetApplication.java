package com.crocobet.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class CrocobetApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrocobetApplication.class, args);
    }

}
