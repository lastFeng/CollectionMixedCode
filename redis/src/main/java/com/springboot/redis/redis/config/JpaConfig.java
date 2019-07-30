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
package com.springboot.redis.redis.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Properties;

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
@EnableJpaRepositories(basePackages = "com.springboot.redis.redis.repository")  // 由于设置了该注解，所以可以不用实现interface
@EntityScan(basePackages = "com.springboot.redis.redis.dao")
public class JpaConfig {
    /**
     * #spring.datasource.url=jdbc:mysql://localhost:3306/test
     * #spring.datasource.username=root
     * #spring.datasource.password=12345678
     * #spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUsername("root");
        dataSource.setPassword("12345678");

        return dataSource;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    protected Properties buildHibernateProperties() {
        Properties hibernateProperties = new Properties();

        //hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
        //hibernateProperties.setProperty("hibernate.use_sql_comments", "false");
        //hibernateProperties.setProperty("hibernate.format_sql", "true");
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        //hibernateProperties.setProperty("hibernate.generate_statistics", "false");
        //hibernateProperties.setProperty("javax.persistence.validation.mode", "none");

        ////Audit History flags
        //hibernateProperties.setProperty("org.hibernate.envers.store_data_at_delete", "true");
        //hibernateProperties.setProperty("org.hibernate.envers.global_with_modified_flag", "true");

        return hibernateProperties;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan("dbdemo.mysql.entity");
        entityManagerFactoryBean.setJpaProperties(buildHibernateProperties());
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter() {{
            setDatabase(Database.MYSQL);
        }});
        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    @Bean
    public TransactionTemplate transactionTemplate() {
        return new TransactionTemplate(transactionManager());
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