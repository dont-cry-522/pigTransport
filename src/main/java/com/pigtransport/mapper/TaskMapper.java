package com.pigtransport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pigtransport.entity.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {

    // 根据状态查询任务
    @Select("SELECT * FROM transport_task WHERE status = #{status} AND deleted = 0 ORDER BY create_time DESC")
    List<Task> selectByStatus(@Param("status") String status);

    // 根据车辆ID查询任务
    @Select("SELECT * FROM transport_task WHERE vehicle_id = #{vehicleId} AND deleted = 0 ORDER BY create_time DESC")
    List<Task> selectByVehicleId(@Param("vehicleId") Integer vehicleId);

    // 根据批次ID查询任务
    @Select("SELECT * FROM transport_task WHERE batch_id = #{batchId} AND deleted = 0")
    Task selectByBatchId(@Param("batchId") Integer batchId);

    // 更新任务状态
    @Select("UPDATE transport_task SET status = #{status} WHERE task_id = #{taskId}")
    int updateStatus(@Param("taskId") Integer taskId, @Param("status") String status);


    @Select("SELECT status, COUNT(*) as task_count FROM pig_task WHERE deleted = 0 GROUP BY status")
    List<Map<String, Object>> countByStatus();

    // 查询司机的任务
    @Select("SELECT t.* FROM transport_task t " +
            "JOIN vehicle v ON t.vehicle_id = v.vehicle_id " +
            "WHERE v.driver_id = #{driverId} AND t.deleted = 0 " +
            "ORDER BY t.create_time DESC")
    List<Task> selectByDriverId(@Param("driverId") Integer driverId);


}