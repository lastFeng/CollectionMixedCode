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
package com.springboot.springweather.service.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.springweather.domain.WeatherResponse;
import com.springboot.springweather.service.WeatherDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.springboot.springweather.constants.Constant.TIMEOUT;
import static com.springboot.springweather.constants.Constant.WEATHER_KEY;
import static com.springboot.springweather.constants.Constant.WEATHER_URL;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/24 10:09
 */
@Service
public class WeatherDataServiceImpl implements WeatherDataService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(WeatherDataServiceImpl.class);
    
    @Override
    public WeatherResponse getDataByCityName(String cityName) {

        WeatherResponse weatherResponse = this.doGetWeatherResponse(cityName);

        return weatherResponse;
    }

    /**
     * 通过名称获取数据，首先先从Redis数据库中获取，如果没有，再通过RestTemplate获取，并存储到数据库中
     * @param request 请求城市的城市名
     * */
    private WeatherResponse doGetWeatherResponse(String request){
        String key = getWeatherKey(request);
        String url = WEATHER_URL + request;

        WeatherResponse weatherResponse = new WeatherResponse();
        ObjectMapper mapper = new ObjectMapper();
        String weatherString;

        // 先查redis
        if (redisTemplate.hasKey(key)){
            weatherString = redisTemplate.opsForValue().get(key);
            logger.info("Searching in Redis, the message is: " + weatherString);
        } else {
            // 如果不存在，那么调用第三方接口，同时存储到redis中
            weatherString = restTemplate.getForObject(url, String.class);
            logger.info("Searching in url, the message is: " + weatherString);
            redisTemplate.opsForValue().set(key, weatherString, TIMEOUT, TimeUnit.DAYS);
            logger.info("Store in the Redis~~");
        }
        try {
            weatherResponse = mapper.readValue(weatherString, WeatherResponse.class);
        } catch (JsonParseException e) {
            logger.error("Convert to json failures!! " + e.getMessage());
        } catch (JsonMappingException e) {
            logger.error("Convert to json failures!! " + e.getMessage());
        } catch (IOException e) {
            logger.error("Convert to json failures!! " + e.getMessage());
        }

        return weatherResponse;
    }


    /**
     * 获取天气key
     * @param cityName 城市名
     * @return key
     * */
    private String getWeatherKey(String cityName){
        return WEATHER_KEY + cityName;
    }

    @Override
    public void saveWeatherByCityName(String cityName){
        String key = getWeatherKey(cityName);
        String url = WEATHER_URL + cityName;

        String weatherString = restTemplate.getForObject(url, String.class);
        redisTemplate.opsForValue().set(key, weatherString, TIMEOUT, TimeUnit.DAYS);
    }
}