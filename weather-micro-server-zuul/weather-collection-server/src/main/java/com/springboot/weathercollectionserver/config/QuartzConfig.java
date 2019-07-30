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
package com.springboot.weathercollectionserver.config;

import com.springboot.weathercollectionserver.job.WeatherDataSyncJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo.Weifeng
 * @version: 1.0
 * @create: 2019/5/24 11:43
 */
@Configuration
@EnableScheduling
public class QuartzConfig {

    /**
     * 现在的配置
     * 定义JobDetail
     * */
    @Bean
    public JobDetail weatherJobDetail(){
        return JobBuilder.newJob(WeatherDataSyncJob.class).withIdentity("weatherJob")
            .storeDurably().build();
    }
    /**
     * 定义Trigger
     * */
    @Bean
    public Trigger weatherTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.repeatHourlyForever(24);
        return TriggerBuilder.newTrigger().forJob(weatherJobDetail()).withIdentity("weatherTrigger")
            .withSchedule(scheduleBuilder).build();
    }
}