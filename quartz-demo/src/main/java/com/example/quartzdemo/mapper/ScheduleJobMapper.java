package com.example.quartzdemo.mapper;

import com.example.quartzdemo.entity.ScheduleJobBean;
import com.example.quartzdemo.entity.ScheduleJobExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScheduleJobMapper {
    int countByExample(ScheduleJobExample example);

    int deleteByExample(ScheduleJobExample example);

    int deleteByPrimaryKey(Long jobId);

    int insert(ScheduleJobBean record);

    int insertSelective(ScheduleJobBean record);

    List<ScheduleJobBean> selectByExample(ScheduleJobExample example);

    ScheduleJobBean selectByPrimaryKey(Long jobId);

    int updateByExampleSelective(@Param("record") ScheduleJobBean record, @Param("example") ScheduleJobExample example);

    int updateByExample(@Param("record") ScheduleJobBean record, @Param("example") ScheduleJobExample example);

    int updateByPrimaryKeySelective(ScheduleJobBean record);

    int updateByPrimaryKey(ScheduleJobBean record);
}