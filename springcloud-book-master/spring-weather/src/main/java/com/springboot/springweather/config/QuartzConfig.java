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
package com.springboot.springweather.config;

import com.springboot.springweather.task.WeatherScheduleSimpleTask;
import com.springboot.springweather.task.WeatherScheduleTask;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

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
     * 以前的配置, 配置第一个JobDetail
     * */
    @Bean(name = "jobDetail")
    public MethodInvokingJobDetailFactoryBean jobDetail(WeatherScheduleSimpleTask task){
        MethodInvokingJobDetailFactoryBean factoryBean = new MethodInvokingJobDetailFactoryBean();

        // 设置是否开启并发
        factoryBean.setConcurrent(false);
        // 设置factoryBean名称
        factoryBean.setName("js-simple");
        // 设置factoryBean组名称
        factoryBean.setGroup("js-group");

        // 设置invoke的目标方法
        factoryBean.setTargetObject(task);
        factoryBean.setTargetMethod("weatherScheduler");

        return factoryBean;
    }

    /**
     * 配置第一个触发器
     * */
    @Bean(name = "jobTrigger")
    public CronTriggerFactoryBean jobTrigger(JobDetail jobDetail){
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();

        // 设置jobDetail
        factoryBean.setJobDetail(jobDetail);
        // 设置时间表达式
        //factoryBean.setCronExpression("0 0/1 * * * ?");
        factoryBean.setCronExpression("0 0 5 * * ?");
        // 设置名称
        factoryBean.setName("cronTrigger");

        return factoryBean;
    }

    /**
     * 配置第二个Job
     */
    @Primary
    @Bean(name = "jobSecondDetail")
    public MethodInvokingJobDetailFactoryBean jobSecondDetail(WeatherScheduleTask task){
        MethodInvokingJobDetailFactoryBean factoryBean = new MethodInvokingJobDetailFactoryBean();
        factoryBean.setConcurrent(false);
        factoryBean.setName("js-all-city");
        factoryBean.setGroup("js-group");
        factoryBean.setTargetObject(task);
        factoryBean.setTargetMethod("getAllCityList");
        return factoryBean;
    }

    /**
     * 配置第二个触发器
     * */
    @Bean(name = "jobSecondTrigger")
    public CronTriggerFactoryBean jobSecondTrigger(JobDetail jobSecondDetail){
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobSecondDetail);
        //factoryBean.setCronExpression("0 0 5 * * ? ");
        factoryBean.setCronExpression("0 30 * * * ? ");
        factoryBean.setName("secondJobTrigger");
        return factoryBean;
    }

    @Bean(name = "scheduler")
    public SchedulerFactoryBean schedulerFactoryBean(Trigger jobTrigger, Trigger jobSecondTrigger){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        // cluster
        schedulerFactoryBean.setOverwriteExistingJobs(false);

        schedulerFactoryBean.setStartupDelay(1);

        schedulerFactoryBean.setTriggers(jobTrigger, jobSecondTrigger);
        return schedulerFactoryBean;
    }

    ///**
    // * 现在的配置
    // * 定义JobDetail
    // * */
    //@Bean
    //public JobDetail weatherJobDetail(){
    //    return JobBuilder.newJob(WeatherDataSyncJob.class).withIdentity("weatherJob")
    //        .storeDurably().build();
    //}
    ///**
    // * 定义Trigger
    // * */
    //@Bean
    //public Trigger weatherTrigger(){
    //    SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().
    //        withIntervalInSeconds(60).repeatForever();
    //    return TriggerBuilder.newTrigger().forJob(weatherJobDetail()).withIdentity("weatherTrigger")
    //        .withSchedule(scheduleBuilder).build();
    //}
}