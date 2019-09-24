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
package com.smart.dao;

import com.smart.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: L.J
 * @Email: alieismy@gmail.com
 * @version: 1.0
 * @create: 2019/9/19 9:10
 */
@Repository
public class UserDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 通过用户名和密码查询数据库是否有该内容，如果有，返回1，没有，返回0
     * @param userName 用户名
     * @param password 密码
     * @return int
     * */
    public int getMatchCount(String userName, String password) {
        String sqlStr = "SELECT COUNT(*) FROM t_user " +
            "WHERE user_name = ? AND password = ?";

        return jdbcTemplate.queryForObject(sqlStr, new Object[] {userName, password}, Integer.class);
    }

    /**
     * 通过用户名查询用户
     * @param userName 用户名
     * @return user
     * */
    public User findUserByUserName(String userName) {
        String sqlStr = "SELECT * FROM t_user WHERE user_name = ?";
        return jdbcTemplate.queryForObject(sqlStr, new Object[]{userName}, User.class);
    }

    /**
     * 通过用户id更新用户访问ip、最后访问时间以及登录积分
     * @param userId 用户id，用于查询哪个用户登录
     * @param lastIp 最新访问ip
     * */
    public void updateUserInfo(Integer userId, String lastIp) {
        String sqlStr = "UPDATE t_user SET credits = credits + 5, last_visit = NOW(), last_ip = ? WHERE user_id=?";
        jdbcTemplate.queryForObject(sqlStr, new Object[]{lastIp, userId}, Void.class);
    }
}