package com.learn.springcloudeurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class SpringcloudeurekaserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudeurekaserverApplication.class, args);
    }

}
