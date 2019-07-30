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
package com.springboot.neo4j.config;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.rmi.server.RemoteServer;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/4/28 9:20
 */
@Configuration
@EnableTransactionManagement
@EnableNeo4jRepositories(basePackages = "com.springboot.neo4j.repository")
public class Neo4jConfig extends Neo4jConfiguration {
    @Override
    public Neo4jServer neo4jServer() {
        return new RemoteServer("http://localhost:7474", "username", "password");
    }

    @Override
    public SessionFactory getSessionFactory() {
        return new SessionFactory("xxx.xxx.xxx");
    }
}