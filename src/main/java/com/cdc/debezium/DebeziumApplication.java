package com.cdc.debezium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;

@SpringBootApplication
public class DebeziumApplication {

    public static void main(String[] args) {
        SpringApplication.run(DebeziumApplication.class, args);
    }

}
