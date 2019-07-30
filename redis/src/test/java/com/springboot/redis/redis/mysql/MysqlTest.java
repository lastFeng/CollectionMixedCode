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
package com.springboot.redis.redis.mysql;

import com.springboot.redis.redis.config.JpaConfig;
import com.springboot.redis.redis.dao.Department;
import com.springboot.redis.redis.dao.Role;
import com.springboot.redis.redis.dao.User;
import com.springboot.redis.redis.repository.DepartmentRepository;
import com.springboot.redis.redis.repository.RoleRepository;
import com.springboot.redis.redis.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/4/26 11:47
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JpaConfig.class})
//@SpringBootTest
public class MysqlTest {
    private static Logger logger = LoggerFactory.getLogger(MysqlTest.class);

    @Autowired
    UserRepository userRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    RoleRepository roleRepository;

    @Before
    public void initData() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        departmentRepository.deleteAll();

        Department department = new Department();
        department.setName("开发部");
        departmentRepository.save(department);
        Assert.notNull(department.getId());

        Role role = new Role();
        role.setName("admin");
        roleRepository.save(role);
        Assert.notNull(role.getId());

        User user = new User();
        user.setName("user");
        user.setCreatedate(new Date());
        user.setDepartment(department);

        List<Role> roles = roleRepository.findAll();
        Assert.notNull(roles);
        user.setRoles(roles);

        userRepository.save(user);
        Assert.notNull(user.getId());
    }

    @Test
    public void findPage() {
        Pageable pageable = new PageRequest(0, 10, new Sort(Sort.Direction.ASC, "id"));
        Page<User> page = userRepository.findAll(pageable);
        Assert.notNull(page);
        for (User user : page.getContent()) {
            logger.info("====user==== user name:{}, department name:{}, role name:{}",
                user.getName(), user.getDepartment().getName(), user.getRoles().get(0).getName());
        }
    }
    //@Autowired
    //private UserRepository userRepository;
    //
    //@Autowired
    //private DepartmentRepository departmentRepository;
    //
    //@Autowired
    //private RoleRepository roleRepository;
    //
    //// 测试前初始化数据
    //@Before
    //public void initData(){
    //    userRepository.deleteAll();
    //    roleRepository.deleteAll();
    //    departmentRepository.deleteAll();
    //
    //    // 增加department数据
    //    Department department = new Department();
    //    department.setName("开发部");
    //    departmentRepository.save(department);
    //    Assert.notNull(department.getId());
    //
    //    // 增加role
    //    Role role = new Role();
    //    role.setName("admin");
    //    roleRepository.save(role);
    //    Assert.notNull(role.getId());
    //
    //    // 增加user
    //    User user = new User();
    //    user.setName("user");
    //    user.setCreatedate(new Date(System.currentTimeMillis()));
    //    user.setDepartment(department);
    //    List<Role> roles = roleRepository.findAll();
    //    Assert.notNull(roles);
    //    user.setRoles(roles);
    //
    //    userRepository.save(user);
    //    Assert.notNull(user.getId());
    //}
    //
    //@Test
    //public void findPage(){
    //    Pageable pageable = new PageRequest(0, 10, new Sort(Sort.Direction.ASC, "id"));
    //    Page<User> page = userRepository.findAll(pageable);
    //    Assert.notNull(page);
    //    for (User user : page.getContent()){
    //        logger.info("=====user====== user name: {}, department name: {}, role name: {}", user.getName(),
    //            user.getDepartment().getName(), user.getRoles().get(0).getName());
    //    }
    //}
}