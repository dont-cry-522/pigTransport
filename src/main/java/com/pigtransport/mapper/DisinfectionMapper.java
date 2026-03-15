package com.pigtransport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pigtransport.entity.Disinfection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface DisinfectionMapper extends BaseMapper<Disinfection> {

    // 根据任务ID查询消毒记录
    @Select("SELECT * FROM disinfection_record WHERE task_id = #{taskId} AND deleted = 0 ORDER BY disinfection_time DESC")
    List<Disinfection> selectByTaskId(@Param("taskId") Integer taskId);

    // 根据司机ID查询消毒记录
    @Select("SELECT * FROM disinfection_record WHERE driver_id = #{driverId} AND deleted = 0 ORDER BY disinfection_time DESC")
    List<Disinfection> selectByDriverId(@Param("driverId") Integer driverId);

    // 根据车辆ID查询消毒记录
    @Select("SELECT * FROM disinfection_record WHERE car_id = #{carId} AND deleted = 0 ORDER BY disinfection_time DESC")
    List<Disinfection> selectByCarId(@Param("carId") Integer carId);

    // 查询今日消毒记录
    @Select("SELECT * FROM disinfection_record WHERE DATE(disinfection_time) = CURDATE() AND deleted = 0 ORDER BY disinfection_time DESC")
    List<Disinfection> selectTodayDisinfections();

    // 统计各类型消毒次数
    @Select("SELECT disinfection_type, COUNT(*) as disinfection_count FROM disinfection_record WHERE deleted = 0 GROUP BY disinfection_type")
    List<Map<String, Object>> countByType();
}