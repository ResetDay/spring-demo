package com.spring.jpa;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JpaApplication {

    public static void main(String[] args) {
        Metrics.addRegistry(new SimpleMeterRegistry());

        SpringApplication.run(JpaApplication.class, args);
    }

}
