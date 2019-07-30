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
package com.springboot.learncodepart.springorm.hibernate.repository;

import com.springboot.learncodepart.domain.Spitter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/4/26 9:37
 * @description: 构建不依赖于Spring 的Hibernate代码
 */
public class HibernateRepository {
    @Autowired
    private SessionFactory sessionFactory;

    // 注入SessionFactory
    public HibernateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // 从SessionFactory中获取当前的Session
    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public long conut() {
        return findAll().size();
    }

    // 使用当前Session
    public Spitter save(Spitter spitter) {
        Serializable id = currentSession().save(spitter);
        return new Spitter((Long) id,
            spitter.getUsername(),
            spitter.getPassword(),
            spitter.getFullName(),
            spitter.getEmail());
    }

    // 通过currentSession，利用id查找
    public Spitter findById(Long id) {
        return (Spitter) currentSession().get(Spitter.class, id);
    }

    // 通过currentSession，利用username查找
    public Spitter findByUsername(String username) {
        return (Spitter) currentSession().get(Spitter.class, username);
    }

    // 通过currentSession查找所有用户
    public List<Spitter> findAll() {
        return (List<Spitter>) currentSession().createCriteria(Spitter.class).list();
    }
}