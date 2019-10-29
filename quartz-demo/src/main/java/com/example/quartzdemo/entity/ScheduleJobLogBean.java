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
package com.example.quartzdemo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo Weifeng
 * @version: 1.0
 * @create: 2019/10/29 12:22
 */
@Data
public class ScheduleJobLogBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long logId;
    private Long jobId;
    private String beanName;
    private String params;
    private Integer status;
    private String error;
    private Integer times;
    private Date createTime;
}