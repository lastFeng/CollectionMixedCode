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
package com.springboot.weathercollectionserver.service.impl;

import com.springboot.weathercollectionserver.service.WeatherDataCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

import static com.springboot.weathercollectionserver.constants.Constant.TIMEOUT;
import static com.springboot.weathercollectionserver.constants.Constant.WEATHER_KEY;
import static com.springboot.weathercollectionserver.constants.Constant.WEATHER_URL;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/28 13:44
 */
@Service
public class WeatherDataCollectionImpl implements WeatherDataCollection {
    private static final Logger logger = LoggerFactory.getLogger(WeatherDataCollection.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public void syncDataByCityName(String cityName) {
        String key = getWeatherKey(cityName);
        String url = WEATHER_URL + cityName;

        String weatherString = restTemplate.getForObject(url, String.class);
        redisTemplate.opsForValue().set(key, weatherString, TIMEOUT, TimeUnit.DAYS);
    }

    /**
     * 获取天气key
     * @param cityName 城市名
     * @return key
     * */
    private String getWeatherKey(String cityName){
        return WEATHER_KEY + cityName;
    }
}