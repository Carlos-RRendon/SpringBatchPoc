package com.chrendon.springbatchpoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBatchPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchPocApplication.class, args);
    }

}
