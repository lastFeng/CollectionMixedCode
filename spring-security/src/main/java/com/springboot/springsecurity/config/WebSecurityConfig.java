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
package com.springboot.springsecurity.config;

import com.springboot.springsecurity.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.sql.DataSource;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/5 9:09
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // jdbc
    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService customUserService() {
        return new CustomUserService();
    }

    /**
     * 用户认证
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //super.configure(auth);
        // 添加内存中的用户，并指定角色权限
        auth
            .inMemoryAuthentication()
            .withUser("wyf").password("wyf").roles("ROLE_ADMIN")
            .and()
            .withUser("wisely").password("wisely").roles("ROLE_USER");

        // jdbc中的用户直接指定dataSource即可
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            // 自定义查询用户和权限的SQL语句（如果不写，则有默认形式，需要建立与默认情况下相同的表信息）
            .usersByUsernameQuery("select username, password, true " +
                "from my_users where username=?")
            // 查找权限
            .authoritiesByUsernameQuery("select username, role from roles where username = ?");

        // 通用模式，则是实现UserDetailsService接口，来重写loadUserByUsername()方法，来实现
        auth.userDetailsService(customUserService());
    }

    /**
     * 请求授权
     * antMatchers: 使用Ant风格的路径匹配
     * regexMatchers: 使用正则表达式匹配路径
     * anyRequest：匹配所有请求路径
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        http
            .authorizeRequests()        // 开始请求权限配置
            .antMatchers("/admin/").hasRole("ROLE_ADMIN")   // 只用拥有ROLE_ADMIN角色的用户可以访问 /admin/*
            .antMatchers("/user/").hasRole("ROLE_USER")     // 只用拥有ROLE_USER角色的用户可以访问 /user/*
            .anyRequest().authenticated();      // 其余请求均需要登陆后才可访问

        /**
         * 定制登录行为
         * */
        http
            .formLogin()        // 定制登录操作
            .loginPage("/login")    // 登录页面访问地址
            .defaultSuccessUrl("/index")  // 登录成功的跳转页面
            .failureUrl("/login?error")   // 登录错误的跳转页面
            .permitAll()                  // 登录成功后，能访问什么页面
            .and()
            .rememberMe()           // 开启cookie存储用户信息
            .tokenValiditySeconds(1209600)  // 设置cookie有效期，2个星期（单位：秒）
            .key("myKey")       // 指定cookie中的私钥
            .and()
            .logout()
            .logoutUrl("/custom-logout")        // 指定注销的URL路径
            .logoutSuccessUrl("/logout-success")
            .permitAll();

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
}