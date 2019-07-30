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
package com.springboot.springweather.task;

import com.springboot.springweather.service.WeatherDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/24 15:42
 */
@Configuration
public class WeatherScheduleSimpleTask {
    private static final Logger logger = LoggerFactory.getLogger(WeatherScheduleSimpleTask.class);

    @Autowired
    private WeatherDataService weatherDataService;

    private static final String[] cityNames = {"深圳", "北京", "长汀", "福州", "厦门", "成都", "赣州"};

    /**
     * task
     * 每天凌晨5点，进行数据拉取，保存到Redis数据库中，如果存在，则覆盖
     * */
    public void weatherScheduler(){
        logger.info("Starting weatherScheduler...");
        for (String cityName: cityNames){
            weatherDataService.saveWeatherByCityName(cityName);
        }
        logger.info("Weather Stored in Redis successfully");
    }
}