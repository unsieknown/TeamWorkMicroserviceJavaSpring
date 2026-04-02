package com.mordiniaa.bordservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class BordServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BordServiceApplication.class, args);
    }

}
