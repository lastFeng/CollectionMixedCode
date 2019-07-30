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
package com.springboot.weatheruiserver.controller;

import com.springboot.weatheruiserver.domain.WeatherResponse;
import com.springboot.weatheruiserver.service.WeatherClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/27 9:27
 */
@Controller
@RequestMapping()
public class WeatherUiController {
    @Autowired
    private WeatherClient weatherClient;


    @GetMapping("/city/{cityName}")
    public ModelAndView getReportByCityName(@PathVariable("cityName")String cityName, Model model) throws Exception {
        model.addAttribute("title", "天气预报");
        model.addAttribute("cityName", cityName);
        model.addAttribute("cityList", weatherClient.getAllCity());
        WeatherResponse response = weatherClient.getWeatherByCityName(cityName);
        if (response.getData()==null){
            response = null;
        }
        model.addAttribute("report", response);
        return new ModelAndView("weather/report", "reportModel", model);
    }
}