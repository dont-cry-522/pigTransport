package com.pigtransport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pigtransport.entity.Location;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface LocationMapper extends BaseMapper<Location> {

    // 根据任务ID查询签到记录
    @Select("SELECT * FROM location_sign WHERE task_id = #{taskId} AND deleted = 0 ORDER BY sign_time DESC")
    List<Location> selectByTaskId(@Param("taskId") Integer taskId);

    // 根据司机ID查询签到记录
    @Select("SELECT * FROM location_sign WHERE driver_id = #{driverId} AND deleted = 0 ORDER BY sign_time DESC")
    List<Location> selectByDriverId(@Param("driverId") Integer driverId);

    // 查询今日签到记录
    @Select("SELECT * FROM location_sign WHERE DATE(sign_time) = CURDATE() AND deleted = 0 ORDER BY sign_time DESC")
    List<Location> selectTodaySigns();

    // 统计签到次数
    @Select("SELECT driver_id, COUNT(*) as sign_count FROM location_sign WHERE deleted = 0 GROUP BY driver_id")
    List<Map<String, Object>> countByDriver();
}