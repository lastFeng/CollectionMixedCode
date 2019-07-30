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
package com.springboot.springshiromybatis.controller;

import com.springboot.springshiromybatis.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/4/29 8:52
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 测试首页
     */
    @GetMapping("/testThymeleaf")
    public String testThymeleaf(Model model) {
        // 把数据存入model中
        model.addAttribute("name", "Hello Thymeleaf!");
        // 返回
        return "testThymeleaf";
    }

    /**
     * 用户添加页面
     */
    @GetMapping("/user/add")
    public String add() {
        return "user/add";
    }

    /**
     * 用户更新页面
     */
    @GetMapping("/user/update")
    public String update() {
        return "user/update";
    }

    /**
     * 自定义跳转用户登录页面
     */
    @GetMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    /**
     * 用户登录逻辑
     */
    @PostMapping("/login")
    public String login(String username, String password, Model model) {
        /**
         * 使用Shiro编写认证操作
         *  1. 获取Subject
         *  2. 封装用户数据
         *  3. 执行登录--只要subject.login(token)方法没有任何异常，就表示登录成功
         * */
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        try {
            subject.login(token);
            // 登录成功，跳转
            return "redirect:/testThymeleaf";
        }
        // 用户名不存在
        catch (UnknownAccountException e) {
            model.addAttribute("msg", "用户名不存在 ");
            return "login";
        }
        // 密码错误
        catch (IncorrectCredentialsException e) {
            model.addAttribute("msg", "密码错误");
            return "login";
        }
    }

    /**
     * 未授权页面跳转
     */
    @GetMapping("/noAuth")
    public String noAuth() {

        return "noAuth";
    }
}