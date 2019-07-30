package com.springboot.springshiromybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.springboot.springshiromybatis.dao")
public class SpringShiroMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringShiroMybatisApplication.class, args);
    }

}
