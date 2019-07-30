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
package com.springboot.o2o.dto;

import java.util.Map;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/8 14:00
 */
public class Result<T> {
    private boolean success;
    private T data;
    private String errMsg;
    private int errCode;

    /**
     * 操作成功后的构造函数
     */
    public Result(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    /**
     * 操作失败的构造函数
     */
    public Result(boolean success, String errMsg, int errCode) {
        this.success = success;
        this.errMsg = errMsg;
        this.errCode = errCode;
    }

    public static void errorModelMap(Map<String, Object> modelMap, boolean success, String errMsg) {
        modelMap.put("success", success);
        modelMap.put("errMsg", errMsg);
    }

    public static void successModelMap(Map<String, Object> modelMap, boolean success, Object data) {
        modelMap.put("success", success);
        modelMap.put("data", data);
    }
}