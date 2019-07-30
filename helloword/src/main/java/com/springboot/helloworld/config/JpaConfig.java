/*
 * Copyright 2001-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springboot.helloworld.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/4/26 11:38
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories(basePackages = "com.springboot.helloworld.repository")  // 由于设置了该注解，所以可以不用实现interface
@EntityScan(basePackages = "com.springboot.helloworld.dao")
public class JpaConfig {
    //@Autowired
    //private DataSource dataSource;

    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    // 如果显示注入EntityManager，就要使用定义该bean
    //@Bean
    //public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor(){
    //    return new PersistenceAnnotationBeanPostProcessor();
    //}

    //@Bean
    //public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(DataSource dataSource,
    //                                                                       JpaVendorAdapter jpaVendorAdapter){
    //    LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean
    //        = new LocalContainerEntityManagerFactoryBean();
    //    localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
    //    localContainerEntityManagerFactoryBean.setDataSource(dataSource);
    //    return localContainerEntityManagerFactoryBean;
    //}

    //@Bean
    //public EntityManagerFactory entityManagerFactory(){
    //    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    //
    //    LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
    //    factoryBean.setDataSource(dataSource);
    //    factoryBean.setJpaVendorAdapter(vendorAdapter);
    //
    //    return factoryBean.getObject();
    //}

    //@Bean
    //public LocalEntityManagerFactoryBean entityManagerFactoryBean(){
    //    return new LocalEntityManagerFactoryBean();
    //}
}