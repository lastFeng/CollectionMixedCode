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

import com.springboot.springweather.domain.City;
import com.springboot.springweather.domain.CityList;
import com.springboot.springweather.service.CityDataService;
import com.springboot.springweather.util.XmlBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/24 14:53
 */
@Service
public class CityDataServiceImpl implements CityDataService {
    @Override
    public List<City> getListCity() throws Exception {

        // 读取xml文件
        Resource resource = new ClassPathResource("citylist.xml");

        // 获得数据读取流
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resource.getInputStream(),
            "utf-8"));
        // 将数据放入StringBuffer中
        StringBuffer stringBuffer = new StringBuffer();
        String line = "";

        // 读到数据为空
        while ((line = bufferedReader.readLine()) != null){
            stringBuffer.append(line);
        }
        // 关闭BufferReader
        bufferedReader.close();

        // 将文件内容转换成Java对象
        CityList cityList = (CityList) XmlBuilder.xmlStr2Object(CityList.class, stringBuffer.toString());

        return cityList.getCityList().subList(0, 2573);
    }
}