package com.eshope.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.eshope.admin.user","com.eshope.common.entity"})
public class EshopeBackendApplication {
    public static void main(String[] args) {

        SpringApplication.run(EshopeBackendApplication.class,args);
    }
}