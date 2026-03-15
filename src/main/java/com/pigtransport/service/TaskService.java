package com.pigtransport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pigtransport.entity.Task;
import java.util.List;
import java.util.Map;

public interface TaskService extends IService<Task> {

    // 分配运输任务
    boolean assignTask(Task task);

    // 更新任务信息
    boolean updateTask(Task task);

    // 删除任务
    boolean deleteTask(Integer taskId);

    // 根据状态查询任务
    List<Task> getTasksByStatus(String status);

    // 根据车辆查询任务
    List<Task> getTasksByVehicle(Integer vehicleId);

    // 根据司机查询任务
    List<Task> getTasksByDriver(Integer driverId);

    // 更新任务状态
    boolean updateTaskStatus(Integer taskId, String status);

    // 开始运输
    boolean startTransport(Integer taskId);

    // 完成任务
    boolean completeTask(Integer taskId);

    // 获取任务统计
    Map<String, Object> getTaskStatistics();

    // 获取任务详情（包含关联信息）
    Map<String, Object> getTaskDetail(Integer taskId);
}