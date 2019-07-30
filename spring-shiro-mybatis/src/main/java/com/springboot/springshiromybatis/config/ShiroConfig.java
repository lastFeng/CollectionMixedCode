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
package com.springboot.springshiromybatis.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.springboot.springshiromybatis.realm.UserRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/4/29 8:53
 *
 * Subject: 用户主体  （把操作交给SecurityManager）
 * SecurityManager: 安全管理器 （关联Realm）
 * Realm: 域 （Shiro连接数据的桥梁）
 *
 * 整合步骤：
 * 1.导入整合依赖， 将Shiro的整合依赖导入
 * 2.编写Shiro配置类
 */
@Configuration
public class ShiroConfig {
    // 1. 创建ShiroFilterFactoryBean
    // 过滤的意思就是，什么网页什么权限的用户可以访问
    // 哪些页面是公共页面
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        // 设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        // 设置自定义跳转页面 不成功默认跳转为login.jsp页面
        // 这个页面不会被拦截
        shiroFilterFactoryBean.setLoginUrl("/toLogin");

        // 使用内置过滤器实现拦截，使用Map来进行设置
        /**
         * 常用过滤器：
         *      anon：匿名访问，无需认证（登录）可以访问
         *      authc：必须认证才可以访问
         *      user： 如果使用remember-me的功能可以直接访问
         *      perms：该资源必须拥有相应的资源授权才可以访问
         *      role：该资源必须得到角色授权才可以访问
         */

        // 使用LinkedHashMap是为了保持设置的属性顺序不变
        // 过滤与不过滤的内容，先设置，优先权更大
        Map<String, String> filerChainFilterMap = new LinkedHashMap<>();

        // 设置过滤内容
        // 其实过滤的规则应该是，所有的都需要认证，然后设置哪些页面放行
        // 不许认证登录的页面要放在认证页面前面

        //filerChainFilterMap.put("/add", "authc");
        //filerChainFilterMap.put("/update", "authc");
        // 设置不过滤的页面
        filerChainFilterMap.put("/testThymeleaf", "anon");
        filerChainFilterMap.put("/login", "anon");

        // 利用通配符优化
        filerChainFilterMap.put("/*", "authc");

        // 增加页面的访问权限
        filerChainFilterMap.put("/user/add", "perms[user:add]");
        filerChainFilterMap.put("/user/update", "perms[user:update]");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filerChainFilterMap);

        // 未验证时，地址的跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/noAuth");

        return shiroFilterFactoryBean;
    }

    // 2. 创建DefaultWebSecurityManager
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(UserRealm userRealm) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        // 关联自定义的Realm
        defaultWebSecurityManager.setRealm(userRealm);
        return defaultWebSecurityManager;
    }

    // 3. 创建Realm对象 自定义
    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }

    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}