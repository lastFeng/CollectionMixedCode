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
package com.example.quartzdemo.utils;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo Weifeng
 * @version: 1.0
 * @create: 2019/10/29 12:26
 */

import com.example.quartzdemo.entity.ScheduleJobBean;
import com.example.quartzdemo.entity.ScheduleJobLogBean;
import com.example.quartzdemo.service.ScheduleJobLogService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定时器执行日志记录
 */
public class TaskJobLog extends QuartzJobBean {

    private static final Logger LOG = LoggerFactory.getLogger(TaskJobLog.class);
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void executeInternal(JobExecutionContext context) {
        ScheduleJobBean jobBean = (ScheduleJobBean)context.getMergedJobDataMap().get(ScheduleJobBean.JOB_PARAM_KEY) ;
        ScheduleJobLogService scheduleJobLogService = (ScheduleJobLogService)SpringContextUtil.getBean("scheduleJobLogService") ;
        // 定时器日志记录
        ScheduleJobLogBean logBean = new ScheduleJobLogBean () ;
        logBean.setJobId(jobBean.getJobId());
        logBean.setBeanName(jobBean.getBeanName());
        logBean.setParams(jobBean.getParams());
        logBean.setCreateTime(new Date());
        long beginTime = System.currentTimeMillis() ;
        try {
            // 加载并执行定时器的 run 方法
            Object target = SpringContextUtil.getBean(jobBean.getBeanName());
            Method method = target.getClass().getDeclaredMethod("run", String.class);
            method.invoke(target, jobBean.getParams());
            long executeTime = System.currentTimeMillis() - beginTime;
            logBean.setTimes((int)executeTime);
            logBean.setStatus(0);
            String cron = format.format(context.getNextFireTime());
            LOG.info("context的上一次时间： " + format.format(context.getPreviousFireTime()));
            LOG.info("context的下一次时间： " + cron);
            LOG.info("定时器 === >> "+jobBean.getJobId()+"执行成功,耗时 === >> " + executeTime);
            System.out.println();
        } catch (Exception e){
            // 异常信息
            long executeTime = System.currentTimeMillis() - beginTime;
            logBean.setTimes((int)executeTime);
            logBean.setStatus(1);
            logBean.setError(e.getMessage());
        } finally {
            scheduleJobLogService.insert(logBean) ;
        }
    }
}
