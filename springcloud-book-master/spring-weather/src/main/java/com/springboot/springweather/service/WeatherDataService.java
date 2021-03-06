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
package com.springboot.springweather.service;

import com.springboot.springweather.domain.WeatherResponse;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/24 10:07
 */
public interface WeatherDataService {
    /**
     * 根据城市名来查询数据
     * @param cityName 城市名称
     * @return 天气的所有信息
     * */
    WeatherResponse getDataByCityName(String cityName);

    /**
     * 根据城市名保存到数据库中
     * @param cityName 城市名
     * */
    void saveWeatherByCityName(String cityName);
}