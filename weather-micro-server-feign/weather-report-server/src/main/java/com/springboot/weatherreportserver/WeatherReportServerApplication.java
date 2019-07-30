package com.springboot.weatherreportserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class WeatherReportServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherReportServerApplication.class, args);
    }

}
