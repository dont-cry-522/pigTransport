package com.pigtransport.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pigtransport.common.LayuiTableResult;
import com.pigtransport.common.Result;
import com.pigtransport.entity.Task;
import com.pigtransport.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // 状态验证列表
    private static final List<String> VALID_STATUSES = Arrays.asList(
            "pending", "transporting", "completed", "canceled"
    );

    /**
     * 任务列表（适配Layui表格，支持分页+多条件搜索）
     */
    @GetMapping("/list")
    public LayuiTableResult list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer vehicleId,
            @RequestParam(required = false) Integer driverId,
            @RequestParam(required = false) String taskCode, // 改为 taskCode
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            QueryWrapper<Task> wrapper = new QueryWrapper<>();
            if (status != null && !status.isEmpty()) {
                wrapper.eq("status", status);
            }
            if (vehicleId != null) {
                wrapper.eq("vehicle_id", vehicleId);
            }
            if (driverId != null) {
                wrapper.eq("driver_id", driverId);
            }
            if (taskCode != null && !taskCode.isEmpty()) {
                wrapper.like("task_code", taskCode); // 改为 task_code
            }
            wrapper.orderByDesc("create_time");

            Page<Task> pageParam = new Page<>(page, limit);
            IPage<Task> taskPage = taskService.page(pageParam, wrapper);

            return LayuiTableResult.success(taskPage.getTotal(), taskPage.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            return LayuiTableResult.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public Result add(@RequestBody(required = false) Map<String, Object> requestData) {
        try {
            if (requestData == null || requestData.isEmpty()) {
                return Result.error("请求数据不能为空");
            }

            Task task = new Task();

            // 任务编号
            if (requestData.containsKey("taskCode") && requestData.get("taskCode") != null) {
                task.setTaskCode(requestData.get("taskCode").toString());
            } else {
                task.setTaskCode("TASK" + System.currentTimeMillis());
            }

            // 批次ID（必填）
            if (!requestData.containsKey("batchId") || requestData.get("batchId") == null) {
                return Result.error("批次ID不能为空");
            }
            try {
                task.setBatchId(Integer.parseInt(requestData.get("batchId").toString()));
            } catch (NumberFormatException e) {
                return Result.error("批次ID必须是数字");
            }

            // 出发地和目的地（必填）
            if (!requestData.containsKey("startPlace") || requestData.get("startPlace") == null) {
                return Result.error("出发地不能为空");
            }
            task.setStartPlace(requestData.get("startPlace").toString());

            if (!requestData.containsKey("endPlace") || requestData.get("endPlace") == null) {
                return Result.error("目的地不能为空");
            }
            task.setEndPlace(requestData.get("endPlace").toString());

            // 状态
            if (requestData.containsKey("status") && requestData.get("status") != null) {
                String status = requestData.get("status").toString();
                if (VALID_STATUSES.contains(status)) {
                    task.setStatus(status);
                } else {
                    return Result.error("无效的状态值: " + status);
                }
            } else {
                task.setStatus("pending");
            }

            // 可选字段
            if (requestData.containsKey("vehicleId") && requestData.get("vehicleId") != null) {
                try {
                    task.setVehicleId(Integer.parseInt(requestData.get("vehicleId").toString()));
                } catch (NumberFormatException e) {
                    // 忽略错误
                }
            }

            if (requestData.containsKey("driverId") && requestData.get("driverId") != null) {
                try {
                    task.setDriverId(Integer.parseInt(requestData.get("driverId").toString()));
                } catch (NumberFormatException e) {
                    // 忽略错误
                }
            }

            if (requestData.containsKey("remark") && requestData.get("remark") != null) {
                task.setRemark(requestData.get("remark").toString());
            }

            task.setCreateTime(new Date());
            task.setDeleted(0);

            boolean success = taskService.save(task);
            return success ? Result.success("任务新增成功", task) : Result.error("任务新增失败");

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误: " + e.getMessage());
        }
    }

    // 开始运输（使用 @RequestParam 方式，最简单）
    @PutMapping("/start/{taskId}")
    public Result startTransport(@PathVariable Integer taskId) {
        try {
            boolean success = taskService.startTransport(taskId);
            return success ? Result.success("运输开始") : Result.error("操作失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误");
        }
    }

    // 其他接口保持不变...
    @PutMapping("/update")
    public Result update(@RequestBody Task task) {
        try {
            boolean success = taskService.updateTask(task);
            return success ? Result.success("更新成功") : Result.error("更新失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误");
        }
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        try {
            boolean success = taskService.deleteTask(id);
            return success ? Result.success("删除成功") : Result.error("删除失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误");
        }
    }

    // 完成任务
    @PutMapping("/complete")
    public Result completeTask(@RequestParam Integer taskId) {
        try {
            boolean success = taskService.completeTask(taskId);
            return success ? Result.success("任务完成") : Result.error("操作失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误");
        }
    }

    @GetMapping("/detail/{id}")
    public Result getDetail(@PathVariable Integer id) {
        try {
            Map<String, Object> detail = taskService.getTaskDetail(id);
            return Result.success("查询成功", detail);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败");
        }
    }
}