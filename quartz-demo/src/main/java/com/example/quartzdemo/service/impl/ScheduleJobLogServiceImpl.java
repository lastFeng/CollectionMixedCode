package com.example.quartzdemo.service.impl;

import com.example.quartzdemo.entity.ScheduleJobLogBean;
import com.example.quartzdemo.mapper.ScheduleJobLogMapper;
import com.example.quartzdemo.service.ScheduleJobLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("scheduleJobLogService")
public class ScheduleJobLogServiceImpl implements ScheduleJobLogService {

    @Resource
    private ScheduleJobLogMapper scheduleJobLogMapper ;

    @Override
    public int insert(ScheduleJobLogBean record) {
        return scheduleJobLogMapper.insert(record);
    }
}
