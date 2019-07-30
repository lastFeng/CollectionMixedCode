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
package com.springboot.springweather.job;

import com.springboot.springweather.domain.City;
import com.springboot.springweather.service.CityDataService;
import com.springboot.springweather.service.WeatherDataService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/24 11:51
 */
public class WeatherDataSyncJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(WeatherDataSyncJob.class);

    @Autowired
    private CityDataService cityDataService;
    @Autowired
    private WeatherDataService weatherDataService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info("Starting getAllCityList...");
        logger.info("获取所有城市名，将其保存");
        List<City> cityList = null;
        try {
            cityList = cityDataService.getListCity();
            // 遍历获取城市id
            cityList.forEach(city -> {
                logger.info("WeatherByCityName: " + city.getCityName());
                ////weatherDataService.syncDataByCityName(city.getCityName());
                //String key = WEATHER_KEY + city.getCityName();
                //String url = WEATHER_URL +city.getCityName();
                //String weatherString = restTemplate.getForObject(url, String.class);
                //redisTemplate.opsForValue().set(key, weatherString, TIMEOUT, TimeUnit.DAYS);
                weatherDataService.saveWeatherByCityName(city.getCityName());
            });
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        logger.info("Weather Stored in Redis successfully");
    }
}