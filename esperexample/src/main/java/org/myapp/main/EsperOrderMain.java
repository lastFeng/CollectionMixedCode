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
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import org.myapp.event.OrderEvent;
import org.myapp.listener.MyListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/8/2 10:22
 */
public class EsperOrderMain {
    public static void main(String[] args) {

        // 配置指定采用类的Java包名
        Configuration configuration = new Configuration();
        configuration.addEventTypeAutoName("org.myapp.event");

        //获取默认的引擎实例
        EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider(configuration);
        String expression = "select avg(price) from org.myapp.event.OrderEvent.win:time(30 sec)";
        //注册EPL，获取statement
        EPStatement statement = epService.getEPAdministrator().createEPL(expression);

        // 添加监听器
        MyListener listener = new MyListener();
        statement.addListener(listener);

        // 发送事件
        List<OrderEvent> list = new ArrayList<OrderEvent>();
        OrderEvent event = new OrderEvent("shirt", 74.50);
        OrderEvent event2 = new OrderEvent("shirt", 80.00);
        OrderEvent event1 = new OrderEvent("shirt", 90.00);
        list.addAll(Arrays.asList(event, event1, event2));
        // 向引擎发送事件
        epService.getEPRuntime().sendEvent(event);
        epService.getEPRuntime().sendEvent(event1);
        epService.getEPRuntime().sendEvent(event2);
    }
}