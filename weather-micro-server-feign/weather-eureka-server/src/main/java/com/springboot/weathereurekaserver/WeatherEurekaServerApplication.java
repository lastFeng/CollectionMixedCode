package com.springboot.weathereurekaserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class WeatherEurekaServerApplication {
    private static final Logger logger = LoggerFactory.getLogger(WeatherEurekaServerApplication.class);
    public static void main(String[] args) {
        SpringApplication.run(WeatherEurekaServerApplication.class, args);
        logger.info("Eureka starts up successfully!!!!!");
    }
}
