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
package com.springboot.learncodepart.springjdbc.config;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/4/25 9:24
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.security.core.parameters.P;

import javax.sql.DataSource;

/**
 * Spring提供的数据访问模板，分别适用于不同的持久化机制
 * 模板类（org.springframework.*）                       用途
 * jca.cci.core.CciTemplate                     JCA CCI连接
 * jdbc.core.JdbcTemplate                           JDBC连接
 * jdbc.core.namedparam.NamedParameterJdbcTemplate  支持命名参数的JDBC连接
 * jdbc.core.simple.SimpleJdbcTemplate              通过Java5简化后的JDBC连接（Spring3.1中已废弃）
 * orm.hibernate3.HibernateTemplate                 Hibernate3.x以上的Session
 * orm.ibatis.SqlMapClientTemplate                  iBATIS SqlMap客户端
 * orm.jdo.JdoTemplate                              Java数据对象（Java Data Object）实现
 * orm.jpa.JpaTemplate                              Java持久化API的实体管理器
 */
@Configuration
public class DataSourceAndTemplate {
    /**
     * 使用JNDI数据源
     */
    @Profile("jndi")
    @Bean
    public JndiObjectFactoryBean dataSource() {
        JndiObjectFactoryBean jndi = new JndiObjectFactoryBean();
        jndi.setJndiName("jdbc/SpittrDS");                      // 指定JNDI中资源的名称，可以根据指定的名称查找数据源
        jndi.setResourceRef(true);                              // 将其设置为true，将给给定的jndi-name自动添加"Java:comp/env/"前缀
        jndi.setProxyInterface(javax.sql.DataSource.class);     //
        return jndi;
    }

    /**
     * 使用数据源连接池
     * 开源的实现有：
     * Apache Commons DBCP
     * c3p0
     * BoneCP
     *
     * BasicDataSource的池配置属性
     * 池配置属性           所指定的内容
     * initialSize         池启动时创建的连接数量
     * maxActive           同一时间可以从池中分配的最多连接数，如果设置为0，表示无限制
     * maxIdle             池里不会被释放的最多空闲连接数，如果设置为0，表示无限制
     * maxOpenPreparedStatements   同一时间能够从语句中分配的预处理语句（preparedStatement）的最大数量，如果设置为0，表示无限制
     * maxWait             在抛出异常之前，池等待连接回收的最大时间（当没有可用连接时）。如果设置为-1，表示无限等待
     * minEvictableIdleTimeMillis      连接在池中保持空闲而不被回收的最大时间
     * minIdle             在不创建新连接的情况下，池中保持空闲的最小连接数
     * poolPreparedStatements      是否对预处理语句（prepared statement）进行池管理（布尔型）
     */
    @Profile("h2")
    @Bean
    public BasicDataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:tcp://localhost/~/spitter");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        dataSource.setInitialSize(5);
        dataSource.setMaxActive(10);
        return dataSource;
    }

    /**
     * 基于JDBC驱动的数据源,Spring提供了三个这样的数据源类
     * 1. DriverManagerDataSource： 在每个连接请求时都会返回一个新建的连接。与DBCP的BasicDataSource不同，由DriverManagerDataSource提供的连接并没有进行池化管理；
     * 2. SimpleDriverDataSource：与DriverManagerDataSource的工作方式类似，但是它直接使用JDBC驱动，来解决在特定环境下的类加载问题，这样的环境包括OSGi容器；
     * 3. SingleConnectionDataSource：在每个连接请求时都会返回同一个的连接。不是一个严格的连接池数据源，但是可以将其视为只有一个连接的池
     */
    @Profile("pro")
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:tcp://localhost/~/spitter");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    /**
     * 使用嵌入式的数据源进行安装
     */
    @Profile("embedded")
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("classpath:schema.sql")
            .addScript("classpath:test-data.sql")
            .build();
    }
}