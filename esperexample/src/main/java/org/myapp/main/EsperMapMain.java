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
package org.myapp.main;

import com.espertech.esper.client.Configuration;
import org.myapp.event.Address;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/8/2 11:40
 */
public class EsperMapMain {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();

        // Person 定义
        Map<String, Object> person = new HashMap<String, Object>();
        person.put("name", String.class);
        person.put("age", Integer.class);
        person.put("children", List.class);
        person.put("phones", Map.class);
        person.put("address", Address.class);

        // 注册Person到Esper
        configuration.addEventType("Person", person);

        // 发送消息的时候可以使用多线程发送

    }
}