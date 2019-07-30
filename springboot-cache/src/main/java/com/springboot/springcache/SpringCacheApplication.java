package com.springboot.springcache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 开启缓存技术
 * 有：JCache、EhCache、Hazelcast、Guava、Redis等
 * Spring 集成Cache时，需要注册实现CacheManager的Bean
 * SpringBoot为我们自动配置了相应的Bean：
 * JcacheCacheConfiguration;
 * EhCacheCacheConfiguration;
 * HazelcastCacheConfiguration;
 * GuavaCacheConfiguration;
 * RedisCacheConfiguration
 * SimpleCacheConfiguration 等等；
 *
 * @author
 */
@SpringBootApplication
@EnableCaching
public class SpringCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCacheApplication.class, args);
    }

}
