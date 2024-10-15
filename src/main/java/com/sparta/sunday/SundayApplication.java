package com.sparta.sunday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableJpaAuditing
public class SundayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SundayApplication.class, args);
    }

}
