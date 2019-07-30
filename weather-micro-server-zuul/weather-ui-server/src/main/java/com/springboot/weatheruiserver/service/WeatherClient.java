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
package com.springboot.weatheruiserver.service;

import com.springboot.weatheruiserver.domain.City;
import com.springboot.weatheruiserver.domain.WeatherResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: G.Weifeng
 * @version: 1.0
 * @create: 2019/5/29 12:29
 */
@FeignClient("weather-zuul-server")
public interface WeatherClient {
    /**
     * 通过FeignClient进行声明式服务查找
     * @param cityName c城市名
     * @return weatherResponse
     * */
    @GetMapping("/report/city/{cityName}")
    WeatherResponse getWeatherByCityName(@PathVariable("cityName") String cityName);

    /**
     * 获取city信息
     * @return list
     * */
    @GetMapping("/city/list")
    List<City> getAllCity();
}
