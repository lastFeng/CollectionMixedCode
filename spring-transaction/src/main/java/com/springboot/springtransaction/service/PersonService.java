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
package com.springboot.springtransaction.service;

import com.springboot.springtransaction.dao.PersonRepository;
import com.springboot.springtransaction.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/4/28 13:54
 */
@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    @Transactional(rollbackFor = IllegalArgumentException.class)
    public Person savePersonWithRollBack(Person person) {
        Person p = personRepository.save(person);

        if (person.getName().equals("lisi")) {
            throw new IllegalArgumentException("lisi已经存在，数据将回滚");
        }
        return p;
    }

    @Transactional(noRollbackFor = IllegalArgumentException.class)
    public Person savePersonWithoutRollback(Person person) {
        Person p = personRepository.save(person);

        if (person.getName().equals("lisi")) {
            throw new IllegalArgumentException("lisi虽已经存在，但不会回滚");
        }
        return p;
    }
}