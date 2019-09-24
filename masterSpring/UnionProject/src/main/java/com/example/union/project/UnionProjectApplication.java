package com.example.union.project;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.example.zgf.dao")
public class UnionProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(UnionProjectApplication.class, args);
    }

}
