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
package com.springboot.learncodepart.springsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

import javax.sql.DataSource;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/4/24 13:40
 */
@Configuration
//@EnableWebSecurity
@EnableWebMvcSecurity

/**
 * WebSecurityConfigurerAdapter的方法有
 * configure(WebSecurity)    配置Spring Security的Filter链
 * configure(HttpSecurity)   配置如何通过拦截器保护请求
 * configure(AuthenticationManagerBuilder)  配置user-detail服务
 * */
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * AuthenticationManagerBuilder的方法
     * accountExpired(boolean) 定义账号是否已经过期
     * accountLocked(boolean)  定义账号是否已经锁定
     * and()                    用来连接配置
     * authorities(GrantedAuthority ...) 授予某个用户一项或多项权限
     * authorities(List<? extends GrantedAuthority>) 授予某个用户一项或多项权限
     * authorities(String ...)  授予某个用户一项或多项权限
     * credentialsExpired(boolean)  定义凭证是否已经过期
     * disable(boolean)         定义账号是否已被禁用
     * password(String)         定义用户的密码
     * roles(String ...)        授予某个用户一项或多项角色
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()       // 启用内存用户存储
            .withUser("user").password("password").roles("USER") // 添加用户及其角色
            .and()
            .withUser("admin").password("password").roles("USER", "ADMIN");  // 添加用户admin及其角色
    }

    /**
     * 基于数据库表进行认证
     */
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .jdbcAuthentication()               // 通过访问数据库进行认证
            .dataSource(dataSource);
    }

    /**
     * 拦截请求，通过HttpSecurity来进行重写拦截请求
     *
     * 用来定义如何保护路径的配置方法: 即authorityRequests()下的方法
     * access(String) 如果给定的SpEL表达式的计算结果为true，就允许访问
     * SpEL表达式有：
     * authentication 用户的认证对象
     * denyALl     结果始终为false
     * hasAnyRole(list of role)
     * hasRole(role)
     * hasIpAddress(IP Address)
     * isAnonymous()
     * isAuthenticated()
     * isFullyAuthenticated()
     * ifRememberMe()
     * permitAll
     * principal
     * anonymous()  允许用户匿名访问
     * authenticated() 允许认证过的用户访问
     * denyAll()    无条件拒绝所有访问
     * fullyAuthenticated() 如果用户是完整认证的话（不是通过remember-me功能认证的），就允许访问
     * hasAnyAuthority(String ...) 如果用户具备给定权限中的任意一个的话，就允许访问
     * hasAnyRole(String ...)  如果用户具备给定角色中的任意一个的话，就允许访问
     * hasAuthority(String) 如果用户具备给定的权限的话，就允许访问
     * hasRole(String)  如果用户具备给定的角色的话，就允许访问
     * hasIpAddress(String) 如果请求来自给定IP地址的话，就允许访问
     * not()       对其他访问方法的结果取反
     * permitAll()  无条件允许访问
     * rememberMe() 如果用户是通过Remember-me功能认证的，就允许访问
     *
     *
     * 强制通道：requiresChannel()
     * requiresSecure()       需要HTTPS
     * requiresInsecure()     普通HTTP
     *
     * 防止跨站请求伪造csrf：csrf()
     *
     * disable()   禁用CSRF防护功能
     *
     * 认证用户：formLogin()
     * loginPage(String) 指定认证用户的网址
     *
     * 启用HTTP Basic认证：httpBasic()
     * realName(String)        指定相应域
     *
     * 启动RememberMe功能：rememberMe()
     * tokenValiditySeconds(int)  默认为2周
     * key(String)   设置应用独有的私钥
     *
     * 退出： logout()
     * logoutSuccessUrl(String)  成功退出，重定向地址
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/spitters/me").authenticated()
            .antMatchers(HttpMethod.POST, "/spittles").authenticated()
            .anyRequest().permitAll();
    }

    /**
     * 配置用户不仅需要认证，还具有ROLE_SPITTER权限的两种写法
     */
    @Override
    protected void confiure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/spitter/me").hasAuthority("ROLE_SPITTER")
            .antMatchers(HttpMethod.POST, "/spittles").hasAuthority("ROLE_SPITTER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("spitter/me").hasRole("SPITTER")
            .antMatchers(HttpMethod.POST, "/spittles").hasRole("SPITTER");
    }
}