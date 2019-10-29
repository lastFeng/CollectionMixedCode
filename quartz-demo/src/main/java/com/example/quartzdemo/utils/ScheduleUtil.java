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

import com.example.quartzdemo.entity.ScheduleJobBean;
import org.quartz.*;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo Weifeng
 * @version: 1.0
 * @create: 2019/10/29 11:00
 * 定时器工具类
 */
public class ScheduleUtil {
    private ScheduleUtil(){}
    private static final String SCHEDULE_NAME = "MY_";

    /**
     * 触发器Key
     * */
    public static TriggerKey getTriggerKey(Long jobId) {
        return TriggerKey.triggerKey(SCHEDULE_NAME + jobId);
    }

    /**
     * 定时器Key
     * */
    public static JobKey getJobKey(Long jobId) {
        return JobKey.jobKey(SCHEDULE_NAME + jobId);
    }

    /**
     * 表达式触发器
     * */
    public static CronTrigger getCronTrigger(Scheduler scheduler, Long jobId) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
        } catch (SchedulerException e) {
            throw new RuntimeException("getCronTrigger Fail", e);
        }
    }

    /**
     * 创建定时器
     * */
    public static void createJob(Scheduler scheduler, ScheduleJobBean scheduleJob) {
        try {
            // 构件定时器
            JobDetail jobDetail = JobBuilder.newJob(TaskJobLog.class).withIdentity(getJobKey(scheduleJob.getJobId())).build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder
                .cronSchedule(scheduleJob.getCronExpression())
                .withMisfireHandlingInstructionDoNothing();

            CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(getTriggerKey(scheduleJob.getJobId()))
                .withSchedule(scheduleBuilder).build();

            jobDetail.getJobDataMap().put(ScheduleJobBean.JOB_PARAM_KEY,scheduleJob);
            scheduler.scheduleJob(jobDetail, trigger);

            // 如果该定时器处于暂停状态
            if (scheduleJob.getStatus() == 0) {
                pauseJob(scheduler, scheduleJob.getJobId());
            }
        } catch (SchedulerException e) {
            throw new RuntimeException("createJob Fail", e);
        }
    }

    /**
     * 更新定时任务
     * */
    public static void updateJob(Scheduler scheduler, ScheduleJobBean scheduleJob) {
        try {
            // 创建定时器
            TriggerKey triggerKey = getTriggerKey(scheduleJob.getJobId());
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
                .withMisfireHandlingInstructionDoNothing();
            CronTrigger trigger = getCronTrigger(scheduler, scheduleJob.getJobId());

            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            trigger.getJobDataMap().put(ScheduleJobBean.JOB_PARAM_KEY,scheduleJob);
            scheduler.rescheduleJob(triggerKey, trigger);

            if (scheduleJob.getStatus() == 0) {
                pauseJob(scheduler, scheduleJob.getJobId());
            }
        } catch (SchedulerException e) {
            throw new RuntimeException("updateJob Fail ", e);
        }
    }

    /**
     * 暂停定时器
     * */
    public static void pauseJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.pauseJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new RuntimeException("pauseJob Fail ", e);
        }
    }

    /**
     * 恢复定时器
     * */
    public static void resumeJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.resumeJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new RuntimeException("resumeJob Fail ", e);
        }
    }

    /**
     * 删除定时器
     * */
    public static void deleteJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.deleteJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new RuntimeException("deleteJob Fail ", e);
        }
    }

    /**
     * 执行定时器
     * */
    public static void run(Scheduler scheduler, ScheduleJobBean scheduleJob) {
        try {
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(ScheduleJobBean.JOB_PARAM_KEY,scheduleJob);
            scheduler.triggerJob(getJobKey(scheduleJob.getJobId()), dataMap);
        } catch (SchedulerException e) {
            throw new RuntimeException("runJob Fail ", e);
        }
    }
}