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
package com.springboot.springjpa.controller;

import com.springboot.springjpa.dao.Person;
import com.springboot.springjpa.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/4/28 12:34
 */
@RestController
@RequestMapping("controller")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/name")
    private List<Person> getNames() {
        List<String> nameArr = new ArrayList<>();
        List<Person> personArr = new ArrayList<>();
        for (Person person : userService.getAll()) {
            String name = person.getName();
            logger.info(">>>>>>> name is: " + name);
            nameArr.add(name);
            personArr.add(person);
        }
        return personArr;
    }

    @DeleteMapping("/del/{id}")
    private String delRecord(@PathVariable Long id) {
        userService.delById(id);
        return String.format(">>>>> record(id=%s) deleted", id);
    }

    @GetMapping("/add/{id}/{name}/{age}/{address}")
    private String addRecord(@PathVariable Long id,
                             @PathVariable String name,
                             @PathVariable Integer age,
                             @PathVariable String address) {
        userService.addRecord(new Person(id, name, age, address));
        return String.format(">>>>> add record:(id=%s, name=%s, age=%s, address=%s)", id, name, age, address);
    }
}