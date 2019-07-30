package com.springboot.citycollectionserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.springboot.citycollectionserver.mapper")
public class CityCollectionServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CityCollectionServerApplication.class, args);
    }

}
