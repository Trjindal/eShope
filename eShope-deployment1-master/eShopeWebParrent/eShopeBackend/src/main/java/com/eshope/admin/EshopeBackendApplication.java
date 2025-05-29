package com.eshope.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@SpringBootApplication
@SpringBootApplication(scanBasePackages = "com.eshope.admin.*")
@ComponentScan(basePackages = {"com.eshope.admin.*"})
@EntityScan(basePackages = {"com.eshope.admin.Entity","com.eShope.common.entity"})
@EnableJpaRepositories( basePackages = "com.eshope.admin.Repository")

public class EshopeBackendApplication {
    public static void main(String[] args) {

        SpringApplication.run(EshopeBackendApplication.class,args);
    }
}