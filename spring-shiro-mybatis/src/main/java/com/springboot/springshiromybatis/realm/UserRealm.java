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
package com.springboot.springshiromybatis.realm;

import com.springboot.springshiromybatis.domain.User;
import com.springboot.springshiromybatis.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/4/29 9:14
 */
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    /**
     * 执行授权逻辑，能有什么权限访问哪些页面
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //System.out.println("执行授权逻辑");

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //// 增加权限
        //authorizationInfo.addStringPermission("user:add");
        //authorizationInfo.addStringPermission("user:update");

        // 获取登录用户信息
        Subject subject = SecurityUtils.getSubject();
        // 从doGetAuthenticationInfo的返回的第一个参数获取
        User user = (User) subject.getPrincipal();

        // 读取数据库中的用户所拥有的权限
        User dbUser = userService.findById(user.getId());

        // 要判断用户是否存在？？  -- 只有登录之后的才会访问，登录不了的就不用谈权限的问题，所有不用判断
        authorizationInfo.addStringPermission(dbUser.getPerms());

        return authorizationInfo;
    }

    /**
     * 执行认证逻辑，哪些用户能够进行认证
     *
     * 进行认证操作逻辑：
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //System.out.println("执行认证逻辑");
        /**
         * 编写数据库的用户名和密码
         * */
        //String username = "123";
        //String password = "123";

        /**
         *  1. 判断用户名
         *  2. 判断密码
         * */
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        User user = userService.findByName(usernamePasswordToken.getUsername());
        //System.out.println(user.getUsername());
        //System.out.println(user.getPassword());
        //if (!usernamePasswordToken.getUsername().equals(user.getUsername())){
        // 底层将会抛出UnknownAccountException异常
        // 验证用户名是否存在
        if (user == null) {
            return null;
        }
        // 存在，验证密码是否正确，第一个参数将类型传递给授权逻辑的，以此来进行用户信息的传递
        return new SimpleAuthenticationInfo(user, user.getPassword(), "");
    }
}