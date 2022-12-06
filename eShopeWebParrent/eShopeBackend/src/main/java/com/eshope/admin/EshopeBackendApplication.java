package com.eshope.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan({"com.eshope.admin.user","com.eshope.common.entity"})
@EnableAspectJAutoProxy
//@EnableJpaRepositories("com.eshope.admin.Repositories")
//@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class EshopeBackendApplication {
    public static void main(String[] args) {

        SpringApplication.run(EshopeBackendApplication.class,args);
    }
}