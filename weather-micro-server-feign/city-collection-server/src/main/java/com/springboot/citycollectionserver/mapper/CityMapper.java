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
package com.springboot.citycollectionserver.mapper;

import com.springboot.citycollectionserver.domain.City;

import java.util.List;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/28 14:14
 */
public interface CityMapper {
    /**
     * 从数据库中获取所有city的信息
     * @return list
     * */
    List<City> getAllCity();

    /**
     * 将数据批量加入数据库
     * @param cityList 城市列表
     * */
    void batchInsertCities(List<City> cityList);
}