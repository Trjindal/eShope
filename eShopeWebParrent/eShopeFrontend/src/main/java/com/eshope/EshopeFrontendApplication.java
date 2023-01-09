package com.eshope;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.eshope.common.entity"})
public class EshopeFrontendApplication {
    public static void main(String[] args) {

        SpringApplication.run(EshopeFrontendApplication.class,args);
    }
}