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
package com.springboot.citycollectionserver.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.Reader;
import java.io.StringReader;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/24 14:45
 */
public class XmlBuilder {

    /**
     * 将xml转为指定的POJO
     * @param clazz 指定的Java对象
     * @param xmlStr xml对象
     * @return Object
     * */
    public static Object xmlStr2Object(Class<?> clazz, String xmlStr) throws Exception{
        Object xmlObject = null;
        Reader reader = new StringReader(xmlStr);
        JAXBContext context = JAXBContext.newInstance(clazz);

        // xml 解析为class类
        Unmarshaller unmarshaller = context.createUnmarshaller();
        xmlObject = unmarshaller.unmarshal(reader);

        // 关闭
        if (null != reader){
            reader.close();
        }

        return xmlObject;
    }
}