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
package com.springboot.springcache.service;

import com.springboot.springcache.dao.PersonRepository;
import com.springboot.springcache.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/4/28 15:05
 * CachePut: 无论如何都增加缓存
 * Cacheable：不存在时增加缓存
 * CacheEvict：删除缓存
 */
@Service
public class PersonService {
    @Autowired
    private PersonRepository repository;

    @CachePut(value = "people", key = "#person.id")
    public Person save(Person person) {
        Person p = repository.save(person);
        System.out.println("为id、key：" + p.getId() + "数据做了缓存");
        return p;
    }

    @Cacheable(value = "people", key = "#person.id")
    public Person findOne(Person person) {
        Person p = repository.getOne(person.getId());
        System.out.println("为id、key：" + p.getId() + "数据做了缓存");
        return p;
    }

    @CacheEvict(value = "people")
    public void remove(Long id) {
        System.out.println("删除了id、key：" + id + "的数据缓存");
        repository.deleteById(id);
    }
}