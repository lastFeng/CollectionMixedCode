package com.example.quartzdemo.mapper;

import com.example.quartzdemo.entity.ScheduleJobLogBean;
import com.example.quartzdemo.entity.ScheduleJobLogExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface ScheduleJobLogMapper {
    int countByExample(ScheduleJobLogExample example);

    int deleteByExample(ScheduleJobLogExample example);

    int deleteByPrimaryKey(Long logId);

    int insert(ScheduleJobLogBean record);

    int insertSelective(ScheduleJobLogBean record);

    List<ScheduleJobLogBean> selectByExample(ScheduleJobLogExample example);

    ScheduleJobLogBean selectByPrimaryKey(Long logId);

    int updateByExampleSelective(@Param("record") ScheduleJobLogBean record, @Param("example") ScheduleJobLogExample example);

    int updateByExample(@Param("record") ScheduleJobLogBean record, @Param("example") ScheduleJobLogExample example);

    int updateByPrimaryKeySelective(ScheduleJobLogBean record);

    int updateByPrimaryKey(ScheduleJobLogBean record);
}