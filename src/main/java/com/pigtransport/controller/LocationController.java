package com.pigtransport.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pigtransport.common.LayuiTableResult;
import com.pigtransport.common.Result;
import com.pigtransport.entity.Location;
import com.pigtransport.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    @Autowired
    private LocationService locationService;

    /**
     * 签到记录列表（适配Layui表格，支持分页+多条件搜索）
     * 关键：必须加上 deleted=0 条件，只查询未删除的记录
     */
    @GetMapping("/list")
    public LayuiTableResult list(
            @RequestParam(required = false) Integer taskId,
            @RequestParam(required = false) Integer driverId,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            // 1. 构建查询条件 - 必须加上 deleted=0！！！
            QueryWrapper<Location> wrapper = new QueryWrapper<>();
            wrapper.eq("deleted", 0);  // 只查询未删除的记录

            if (taskId != null) {
                wrapper.eq("task_id", taskId);
            }
            if (driverId != null) {
                wrapper.eq("driver_id", driverId);
            }
            if (location != null && !location.isEmpty()) {
                wrapper.like("location", location);
            }
            if (date != null && !date.isEmpty()) {
                wrapper.apply("DATE(sign_time) = DATE('" + date + "')");
            }

            wrapper.orderByDesc("sign_time");

            // 2. 分页查询
            Page<Location> pageParam = new Page<>(page, limit);
            IPage<Location> locationPage = locationService.page(pageParam, wrapper);

            // 3. 返回Layui要求的格式
            return LayuiTableResult.success(locationPage.getTotal(), locationPage.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            return LayuiTableResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 测试接口：查看查询条件是否生效
     */
    @GetMapping("/testList")
    public Result testList() {
        try {
            QueryWrapper<Location> wrapper = new QueryWrapper<>();
            wrapper.eq("deleted", 0);
            List<Location> list = locationService.list(wrapper);

            System.out.println("==== 测试查询结果 ====");
            System.out.println("查询条件：deleted = 0");
            System.out.println("查询到 " + list.size() + " 条记录");
            for (Location loc : list) {
                System.out.println("ID: " + loc.getSignId() +
                        ", 地点: " + loc.getLocation() +
                        ", deleted: " + loc.getDeleted());
            }

            return Result.success("查询成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据任务ID查询签到记录
     */
    @GetMapping("/listByTask")
    public Result listByTask(@RequestParam Integer taskId) {
        try {
            QueryWrapper<Location> wrapper = new QueryWrapper<>();
            wrapper.eq("task_id", taskId)
                    .eq("deleted", 0)  // 加上 deleted=0
                    .orderByDesc("sign_time");
            List<Location> signs = locationService.list(wrapper);
            return Result.success("查询成功", signs);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据司机ID查询签到记录
     */
    @GetMapping("/listByDriver")
    public Result listByDriver(@RequestParam Integer driverId) {
        try {
            QueryWrapper<Location> wrapper = new QueryWrapper<>();
            wrapper.eq("driver_id", driverId)
                    .eq("deleted", 0)  // 加上 deleted=0
                    .orderByDesc("sign_time");
            List<Location> signs = locationService.list(wrapper);
            return Result.success("查询成功", signs);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取今日签到记录
     */
    @GetMapping("/today")
    public Result getTodaySigns() {
        try {
            QueryWrapper<Location> wrapper = new QueryWrapper<>();
            wrapper.eq("deleted", 0)  // 加上 deleted=0
                    .apply("DATE(sign_time) = CURDATE()")
                    .orderByDesc("sign_time");
            List<Location> signs = locationService.list(wrapper);
            return Result.success("查询成功", signs);
        } catch (Exception e) {
            return Result.error("查询失败");
        }
    }

    /**
     * 添加签到记录
     */
    @PostMapping("/add")
    public Result add(@RequestBody Location location) {
        try {
            // 确保新添加的记录deleted=0
            if (location.getDeleted() == null) {
                location.setDeleted(0);
            }
            boolean success = locationService.addLocation(location);
            return success ? Result.success("签到成功") : Result.error("签到失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("系统错误");
        }
    }

    /**
     * 司机快速签到
     */
    @PostMapping("/driverSign")
    public Result driverSign(@RequestParam Integer driverId,
                             @RequestParam(required = false) Integer taskId,
                             @RequestParam String location,
                             @RequestParam(required = false) String remark) {
        try {
            boolean success = locationService.driverSign(driverId, taskId, location, remark);
            return success ? Result.success("签到成功") : Result.error("签到失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("系统错误");
        }
    }

    /**
     * 更新签到记录
     */
    @PutMapping("/update")
    public Result update(@RequestBody Location location) {
        try {
            boolean success = locationService.updateLocation(location);
            return success ? Result.success("更新成功") : Result.error("更新失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("系统错误");
        }
    }

    /**
     * RESTful风格删除 - DELETE请求，路径参数
     * 前端调用：DELETE /api/location/delete/1
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteByPath(@PathVariable Integer id) {
        try {
            System.out.println("=== DELETE路径参数方式删除 ===");
            System.out.println("删除ID: " + id);
            boolean success = locationService.deleteLocation(id);
            return success ? Result.success("删除成功") : Result.error("删除失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("系统错误");
        }
    }

    /**
     * 前端当前方式 - POST请求，JSON参数
     * 前端调用：POST /api/location/delete
     * 请求体：{"id": 1}
     */
    @PostMapping("/delete")
    public Result deleteByPost(@RequestBody Map<String, Object> params) {
        try {
            System.out.println("=== POST JSON方式删除 ===");
            System.out.println("收到参数: " + params);

            if (params == null || params.get("id") == null) {
                return Result.error("ID参数不能为空");
            }

            Integer id;
            Object idObj = params.get("id");

            try {
                if (idObj instanceof Integer) {
                    id = (Integer) idObj;
                } else if (idObj instanceof String) {
                    id = Integer.valueOf((String) idObj);
                } else if (idObj instanceof Number) {
                    id = ((Number) idObj).intValue();
                } else {
                    return Result.error("ID参数格式错误");
                }
            } catch (Exception e) {
                return Result.error("ID参数格式错误：" + e.getMessage());
            }

            System.out.println("删除ID: " + id);
            boolean success = locationService.deleteLocation(id);

            return success ? Result.success("删除成功") : Result.error("删除失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("系统错误：" + e.getMessage());
        }
    }

    /**
     * 表单提交方式删除
     * 前端调用：POST /api/location/deleteForm
     * Content-Type: application/x-www-form-urlencoded
     */
    @PostMapping("/deleteForm")
    public Result deleteByForm(@RequestParam Integer id) {
        try {
            System.out.println("=== 表单方式删除 ===");
            System.out.println("删除ID: " + id);
            boolean success = locationService.deleteLocation(id);
            return success ? Result.success("删除成功") : Result.error("删除失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("系统错误");
        }
    }

    /**
     * 获取签到统计
     */
    @GetMapping("/statistics")
    public Result getStatistics() {
        try {
            // 总签到次数（未删除的）
            QueryWrapper<Location> wrapper = new QueryWrapper<>();
            wrapper.eq("deleted", 0);
            long total = locationService.count(wrapper);

            // 今日签到次数（未删除的）
            QueryWrapper<Location> todayWrapper = new QueryWrapper<>();
            todayWrapper.eq("deleted", 0)
                    .apply("DATE(sign_time) = CURDATE()");
            long today = locationService.count(todayWrapper);

            Map<String, Object> statistics = new java.util.HashMap<>();
            statistics.put("total", total);
            statistics.put("today", today);

            return Result.success("查询成功", statistics);
        } catch (Exception e) {
            return Result.error("查询失败");
        }
    }

    /**
     * 获取数据库所有数据（包括已删除的，仅用于调试）
     */
    @GetMapping("/debugAll")
    public Result debugAll() {
        try {
            List<Location> all = locationService.list();
            System.out.println("==== 数据库所有数据 ====");
            System.out.println("总记录数: " + all.size());
            for (Location loc : all) {
                System.out.println("ID: " + loc.getSignId() +
                        ", 地点: " + loc.getLocation() +
                        ", deleted: " + loc.getDeleted() +
                        ", 司机ID: " + loc.getDriverId());
            }
            return Result.success("查询成功", all);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 直接测试删除逻辑
     */
    @GetMapping("/testDelete/{id}")
    public Result testDelete(@PathVariable Integer id) {
        try {
            System.out.println("==== 直接测试删除 ====");

            // 先查询原始数据
            Location before = locationService.getById(id);
            System.out.println("删除前记录: " + before);
            System.out.println("删除前deleted值: " + (before != null ? before.getDeleted() : "null"));

            // 执行删除
            boolean result = locationService.deleteLocation(id);
            System.out.println("删除结果: " + result);

            // 再次查询
            Location after = locationService.getById(id);
            System.out.println("删除后记录: " + after);
            System.out.println("删除后deleted值: " + (after != null ? after.getDeleted() : "null"));

            return Result.success("测试完成", result);
        } catch (Exception e) {
            return Result.error("测试出错: " + e.getMessage());
        }
    }
}