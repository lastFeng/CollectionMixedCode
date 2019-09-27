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
package com.smart.web;

import com.smart.domain.User;
import com.smart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: L.J
 * @Email: alieismy@gmail.com
 * @version: 1.0
 * @create: 2019/9/24 13:55
 */
@Controller
public class LoginController {
    @Autowired
    private UserService userService;

    /**处理Index请求*/
    @RequestMapping(value = "/index.html")
    public String loginPage() {
        return "login";
    }

    /**处理登录检查请求*/
    @RequestMapping(value = "loginCheck.html")
    public ModelAndView loginCheck(HttpServletRequest request, LoginCommand loginCommand) {
        boolean isValidUser = userService.hasMatchUser(loginCommand.getUserName(), loginCommand.getPassword());

        if (!isValidUser) {
            return new ModelAndView("login", "error,", "用户名或密码错误");
        } else {
            User user = userService.findByUserName(loginCommand.getUserName());
            user.setLastIp(request.getLocalAddr());
            user.setLastVisit(new Date());
            request.getSession().setAttribute("user", user);
            return new ModelAndView("main");
        }
    }
}