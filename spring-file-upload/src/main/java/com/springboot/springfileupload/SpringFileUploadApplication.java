package com.springboot.springfileupload;

import com.springboot.springfileupload.property.StorageProperties;
import com.springboot.springfileupload.service.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class SpringFileUploadApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringFileUploadApplication.class, args);
    }

    @Bean
    public CommandLineRunner init(StorageService storageService) {
        return (args -> {
            //storageService.deleteAll();
            storageService.init();
        });
    }
}
