package com.pigtransport.controller;

import com.pigtransport.common.Result;
import com.pigtransport.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/task/report") // 关键：路径改为 /api/task/report，和前端权限校验的前缀一致
public class TaskReportController {

    @Autowired
    private TaskMapper taskMapper;

    // 按状态统计任务数量（前端调用接口）
    @GetMapping("/statusCount")
    public Result getTaskStatusCount() {
        try {
            List<Map<String, Object>> data = taskMapper.countByStatus();
            // 用你系统的 Result 类返回，code=200 是成功标识（和你其他接口一致）
            return Result.success("获取任务报表数据成功", data);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取任务报表数据失败：" + e.getMessage());
        }
    }
}