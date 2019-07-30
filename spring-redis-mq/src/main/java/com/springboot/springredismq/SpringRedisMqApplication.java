package com.springboot.springredismq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.CountDownLatch;

/**
 * @author
 */
@SpringBootApplication
public class SpringRedisMqApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringApplication.class);

    public static void main(String[] args) throws InterruptedException {
        //SpringApplication.run(SpringRedisMqApplication.class, args);
        ApplicationContext context = SpringApplication.run(SpringRedisMqApplication.class, args);

        StringRedisTemplate template = context.getBean(StringRedisTemplate.class);
        CountDownLatch latch = context.getBean(CountDownLatch.class);

        LOGGER.info("Sending message...");
        template.convertAndSend("chat", "Hello from Redis");

        latch.await();

        System.exit(0);
    }
}
