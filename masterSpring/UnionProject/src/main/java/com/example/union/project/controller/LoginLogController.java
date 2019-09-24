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
package com.example.union.project.controller;

import com.example.union.project.domain.LoginLog;
import com.example.union.project.service.LoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: L.J
 * @Email: alieismy@gmail.com
 * @version: 1.0
 * @create: 2019/9/19 14:24
 */
@RestController
@RequestMapping(value = "/log", produces = "application/json")
public class LoginLogController {
    @Autowired
    private LoginLogService loginLogService;

    @GetMapping("/count")
    public long getCountByDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        String currentDate = format.format(date);

        return loginLogService.getCountByDate(currentDate);
    }

    @GetMapping("/union")
    public long getUnionCountByDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        String currentDate = format.format(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        date = calendar.getTime();
        String preDate = format.format(date);

        return loginLogService.getUnionCountByDate(currentDate, preDate);
    }

    @GetMapping("/batch")
    public ResponseEntity insert() {

        LoginLog loginLog;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date cur = new Date();
        String currentDate = format.format(cur);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cur);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        Date pre = calendar.getTime();
        String preDate = format.format(pre);

        for (int i = 0; i < 10000; i++) {
            loginLog = new LoginLog();
            loginLog.setLoginLogId(i+1);
            loginLog.setUserId(i+1);
            loginLog.setLoginDate(cur);
            loginLog.setUserId(i+1);
            loginLog.setIp(String.valueOf(i+1));
            loginLogService.insert(loginLog);
        }

        for (int i = 0, j = 20000; i < 10000; i++, j++) {
            loginLog = new LoginLog();
            loginLog.setLoginLogId(j+1);
            loginLog.setUserId(i+1);
            loginLog.setLoginDate(pre);
            loginLog.setUserId(i+1);
            loginLog.setIp(String.valueOf(i+1));
            loginLogService.insert(loginLog);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}