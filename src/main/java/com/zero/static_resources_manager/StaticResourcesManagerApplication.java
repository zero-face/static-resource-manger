package com.zero.static_resources_manager;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//@MapperScan(basePackages = "com.zero.static_resources_manager")
@SpringBootApplication
public class StaticResourcesManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaticResourcesManagerApplication.class, args);
    }

}
