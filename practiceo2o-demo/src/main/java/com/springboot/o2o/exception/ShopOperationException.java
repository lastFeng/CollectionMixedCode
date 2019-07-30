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
package com.springboot.o2o.exception;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/7 12:28
 *
 * 集成RuntimeException会让事务操作进行回滚
 * 如果不是RuntimeException类及其子类，就不会进行回滚，数据库就会出现不一致的现象
 */
public class ShopOperationException extends RuntimeException {
    public ShopOperationException(String msg) {
        super(msg);
    }

    public ShopOperationException(String msg, Error error) {
        super(msg, error);
    }
}