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
package com.springboot.o2o.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/13 9:31
 */
public class EncryptPropertyPlaceHolderConfigurer extends PropertyPlaceholderConfigurer {

    /**
     * 需要解密的字段
     */
    private String[] encryptPropName = {"jdbc.username", "jdbc.password", "redis.password"};

    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        // 只有是加密了字段的才需要解密， 不是所有字段都需要解密
        if (isEncryptPro(propertyName)) {
            return DesUtil.getDecryptString(propertyValue);
        } else {
            return propertyValue;
        }
    }

    /**
     * 判断字段是否进行加密了
     */
    private boolean isEncryptPro(String propertyName) {
        // 需要加密的
        for (String name : encryptPropName) {
            if (name.equals(propertyName)) {
                return true;
            }
        }
        return false;
    }
}