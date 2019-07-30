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
package com.springboot.redis.redis.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.springboot.redis.redis.dao.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/4/26 14:12
 */
@Repository
public class UserRedis {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 保存单个user，给定相应的key
     */
    public void add(String key, Long time, User user) {
        Gson gson = new Gson();
        redisTemplate.opsForValue().set(key, gson.toJson(user), time, TimeUnit.MINUTES);
    }

    /**
     * 保存多个user，给定相应的key
     */
    public void add(String key, Long time, List<User> users) {
        Gson gson = new Gson();
        redisTemplate.opsForValue().set(key, gson.toJson(users), time, TimeUnit.MINUTES);
    }

    /**
     * 通过key获取单个对象
     *
     * @param key redis的key
     */
    public User get(String key) {
        Gson gson = new Gson();
        User user = null;

        String userJson = (String) redisTemplate.opsForValue().get(key);
        if (!StringUtils.isEmpty(userJson)) {
            user = gson.fromJson(userJson, User.class);
        }
        return user;
    }

    /**
     * 通过key获取多个对象
     */
    public List<User> getList(String key) {
        Gson gson = new Gson();
        List<User> list = null;
        String listJson = (String) redisTemplate.opsForValue().get(key);

        if (!StringUtils.isEmpty(listJson)) {
            list = gson.fromJson(listJson, new TypeToken<List<User>>() {
            }.getType());
        }
        return list;
    }

    public void delete(String key) {
        redisTemplate.opsForValue().getOperations().delete(key);
    }
}