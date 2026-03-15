package com.pigtransport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pigtransport.entity.Batch;
import com.pigtransport.entity.Task;
import com.pigtransport.entity.Vehicle;
import com.pigtransport.mapper.TaskMapper;
import com.pigtransport.mapper.VehicleMapper;
import com.pigtransport.mapper.BatchMapper;
import com.pigtransport.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task>
        implements TaskService {

    @Autowired
    private VehicleMapper vehicleMapper;

    @Autowired
    private BatchMapper batchMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignTask(Task task) {
        // 参数校验
        if (task == null || task.getBatchId() == null || task.getVehicleId() == null) {
            throw new RuntimeException("批次和车辆不能为空");
        }
        // 修改点：使用新的字段名 getStartPlace() 和 getEndPlace()
        if (!StringUtils.hasText(task.getStartPlace()) || !StringUtils.hasText(task.getEndPlace())) {
            throw new RuntimeException("出发地和目的地不能为空");
        }

        // 检查批次是否存在且可运输
        Batch batch = batchMapper.selectById(task.getBatchId());
        if (batch == null || !"available".equals(batch.getStatus())) {
            throw new RuntimeException("批次不存在或不可运输");
        }

        // 检查车辆是否存在且空闲
        Vehicle vehicle = vehicleMapper.selectById(task.getVehicleId());
        if (vehicle == null || !"free".equals(vehicle.getStatus())) {
            throw new RuntimeException("车辆不存在或非空闲状态");
        }

        // 设置任务信息
        if (task.getTaskCode() == null || task.getTaskCode().isEmpty()) {
            // 生成任务编号
            task.setTaskCode("TASK" + System.currentTimeMillis());
        }
        task.setStatus("pending");
        task.setAssignTime(new Date());
        task.setCreateTime(new Date());
        task.setDeleted(0);

        // 保存任务
        int result = baseMapper.insert(task);
        if (result > 0) {
            // 更新批次状态为已分配
            batchMapper.updateStatus(task.getBatchId(), "transported");

            // 更新车辆状态为运输中
            vehicleMapper.updateStatus(task.getVehicleId(), "transporting");

            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTask(Task task) {
        if (task == null || task.getTaskId() == null) {
            throw new RuntimeException("任务信息不完整");
        }

        Task existTask = baseMapper.selectById(task.getTaskId());
        if (existTask == null) {
            throw new RuntimeException("任务不存在");
        }

        // 更新字段
        if (StringUtils.hasText(task.getTaskCode())) {
            existTask.setTaskCode(task.getTaskCode());
        }
        if (task.getBatchId() != null) {
            existTask.setBatchId(task.getBatchId());
        }
        if (task.getVehicleId() != null) {
            existTask.setVehicleId(task.getVehicleId());
        }
        if (task.getDriverId() != null) {
            existTask.setDriverId(task.getDriverId());
        }
        // 修改点：使用新的字段名 setStartPlace() 和 setEndPlace()
        if (StringUtils.hasText(task.getStartPlace())) {
            existTask.setStartPlace(task.getStartPlace());
        }
        if (StringUtils.hasText(task.getEndPlace())) {
            existTask.setEndPlace(task.getEndPlace());
        }
        if (StringUtils.hasText(task.getStatus())) {
            existTask.setStatus(task.getStatus());
        }
        if (StringUtils.hasText(task.getRemark())) {
            existTask.setRemark(task.getRemark());
        }

        int result = baseMapper.updateById(existTask);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTask(Integer taskId) {
        if (taskId == null) {
            throw new RuntimeException("任务ID不能为空");
        }
        return baseMapper.deleteById(taskId) > 0;


    }

    @Override
    public List<Task> getTasksByStatus(String status) {
        QueryWrapper<Task> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status)
                .eq("deleted", 0)
                .orderByDesc("create_time");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<Task> getTasksByVehicle(Integer vehicleId) {
        QueryWrapper<Task> wrapper = new QueryWrapper<>();
        wrapper.eq("vehicle_id", vehicleId)
                .eq("deleted", 0)
                .orderByDesc("create_time");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<Task> getTasksByDriver(Integer driverId) {
        return baseMapper.selectByDriverId(driverId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTaskStatus(Integer taskId, String status) {
        if (taskId == null || !StringUtils.hasText(status)) {
            throw new RuntimeException("参数不能为空");
        }

        Task task = baseMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }

        task.setStatus(status);
        int result = baseMapper.updateById(task);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startTransport(Integer taskId) {
        Task task = baseMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }

        if (!"pending".equals(task.getStatus())) {
            throw new RuntimeException("只有待分配任务可以开始运输");
        }

        // 更新任务状态为运输中
        task.setStatus("transporting");
        int result = baseMapper.updateById(task);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeTask(Integer taskId) {
        Task task = baseMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }

        if (!"transporting".equals(task.getStatus())) {
            throw new RuntimeException("只有运输中任务可以完成");
        }

        // 更新任务状态为已完成
        task.setStatus("completed");
        task.setFinishTime(new Date());

        // 更新车辆状态为空闲
        Vehicle vehicle = vehicleMapper.selectById(task.getVehicleId());
        if (vehicle != null) {
            vehicle.setStatus("free");
            vehicleMapper.updateById(vehicle);
        }

        int result = baseMapper.updateById(task);
        return result > 0;
    }

    @Override
    public Map<String, Object> getTaskStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 总任务数量
        QueryWrapper<Task> totalWrapper = new QueryWrapper<>();
        totalWrapper.eq("deleted", 0);
        Long totalCount = baseMapper.selectCount(totalWrapper);
        statistics.put("total", totalCount);

        // 各状态任务数量
        List<Map<String, Object>> statusCounts = baseMapper.countByStatus();
        for (Map<String, Object> count : statusCounts) {
            statistics.put((String) count.get("status"), count.get("count"));
        }

        // 今日任务数量
        QueryWrapper<Task> todayWrapper = new QueryWrapper<>();
        todayWrapper.apply("DATE(create_time) = CURDATE()")
                .eq("deleted", 0);
        Long todayCount = baseMapper.selectCount(todayWrapper);
        statistics.put("today", todayCount);

        return statistics;
    }

    @Override
    public Map<String, Object> getTaskDetail(Integer taskId) {
        Task task = baseMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }

        Map<String, Object> detail = new HashMap<>();
        detail.put("task", task);

        // 关联批次信息
        if (task.getBatchId() != null) {
            Batch batch = batchMapper.selectById(task.getBatchId());
            detail.put("batch", batch);
        }

        // 关联车辆信息
        if (task.getVehicleId() != null) {
            Vehicle vehicle = vehicleMapper.selectById(task.getVehicleId());
            detail.put("vehicle", vehicle);

            // 关联司机信息
            if (vehicle.getDriverId() != null) {
                detail.put("driverId", vehicle.getDriverId());
            }
        }

        return detail;
    }
}