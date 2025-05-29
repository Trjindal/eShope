package com.eshope.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.eshope.consumer.*")
@ComponentScan(basePackages = {"com.eshope.consumer.*"})
@EntityScan(basePackages = {"com.eshope.consumer.Entity","com.eShope.common.entity"})
@EnableJpaRepositories( basePackages = "com.eshope.consumer.Repository")

public class EshopeFrontendApplication {
    public static void main(String[] args) {

        SpringApplication.run(EshopeFrontendApplication.class,args);
    }
}