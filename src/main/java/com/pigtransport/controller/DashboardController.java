package com.pigtransport.controller;

import com.pigtransport.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/summary")
    public Result getSummary() {
        Map<String, Object> data = new HashMap<>();

        // 1. 用户总数
        Integer userCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM sys_user WHERE deleted = 0", Integer.class);
        data.put("userCount", userCount);

        // 2. 车辆总数
        Integer vehicleCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM vehicle WHERE deleted = 0", Integer.class);
        data.put("vehicleCount", vehicleCount);

        // 3. 批次总数
        Integer batchCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM pig_batch WHERE deleted = 0", Integer.class);
        data.put("batchCount", batchCount);

        // 4. 任务总数
        Integer taskCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM transport_task WHERE deleted = 0", Integer.class);
        data.put("taskCount", taskCount);

        // 5. 今日任务数
        Integer todayTaskCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM transport_task WHERE DATE(create_time) = CURDATE() AND deleted = 0",
                Integer.class);
        data.put("todayTaskCount", todayTaskCount);

        return Result.success("获取成功", data);
    }
}