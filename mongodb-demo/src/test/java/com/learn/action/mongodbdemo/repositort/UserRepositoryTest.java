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
package com.learn.action.mongodbdemo.repositort;

import com.learn.action.mongodbdemo.MongodbDemoApplication;
import com.learn.action.mongodbdemo.domain.User;
import com.learn.action.mongodbdemo.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/7/29 16:41
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MongodbDemoApplication.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testUserRepository() {
        // 创建三个User，并验证User总数
        userRepository.save(new User(1L, "didi", 30));
        userRepository.save(new User(2L, "mama", 40));
        userRepository.save(new User(3L, "kaka", 50));

        // 删除一个User，再验证User总数
        Optional<User> u = userRepository.findById(1L);
        User user = null;
        if (u != null) {
            user = u.get();
        }
        userRepository.delete(user);
        Assert.assertEquals(2, userRepository.findAll().size());

        // 删除一个User，再验证User总数
        user = userRepository.findByUsername("mama");
        userRepository.delete(user);
        Assert.assertEquals(1, userRepository.findAll().size());
    }

    @After
    public void finished() {
        userRepository.deleteAll();
    }
}