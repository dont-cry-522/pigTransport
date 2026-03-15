package com.pigtransport.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pigtransport.common.LayuiTableResult;
import com.pigtransport.common.Result;
import com.pigtransport.entity.User;
import com.pigtransport.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器（适配前端RESTful风格 + Layui表格格式）
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {
        try {
            User user = userService.login(username, password);

            session.setAttribute("userId", user.getUserId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("realName", user.getRealName());
            session.setAttribute("role", user.getRole());
            session.setAttribute("phone", user.getPhone());

            Map<String, Object> data = new HashMap<>();
            data.put("userId", user.getUserId());
            data.put("username", user.getUsername());
            data.put("realName", user.getRealName());
            data.put("role", user.getRole());
            data.put("phone", user.getPhone());

            return Result.success("登录成功", data);

        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    /**
     * 用户注销
     */
    @PostMapping("/logout")
    public Result logout(HttpSession session) {
        session.invalidate();
        return Result.success("退出成功");
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/current")
    public Result getCurrentUser(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return Result.error("用户未登录");
        }

        try {
            User user = userService.getById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }
            user.setPassword(null);
            return Result.success("获取成功", user);
        } catch (Exception e) {
            return Result.error("获取用户信息失败");
        }
    }

    /**
     * 获取用户列表（适配Layui表格，支持分页）
     */
    @GetMapping("")
    public LayuiTableResult list(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            if (username != null && !username.isEmpty()) {
                wrapper.like("username", username);
            }
            if (role != null && !role.isEmpty()) {
                wrapper.eq("role", role);
            }
            if (status != null) {
                wrapper.eq("status", status);
            }
            wrapper.eq("deleted", 0);
            wrapper.orderByDesc("create_time");

            Page<User> pageParam = new Page<>(page, limit);
            IPage<User> userPage = userService.page(pageParam, wrapper);

            // 计算正确的分页数据
            List<User> records = userPage.getRecords();
            records.forEach(user -> user.setPassword(null));

            return LayuiTableResult.success(userPage.getTotal(), records);
        } catch (Exception e) {
            e.printStackTrace();
            return LayuiTableResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增用户
     */
    @PostMapping("")
    public Result add(@RequestBody User user) {
        try {
            // 设置默认值
            if (user.getStatus() == null || user.getStatus().isEmpty()) {
                user.setStatus("1"); // 默认启用
            }
            boolean success = userService.addUser(user);
            return success ? Result.success("添加用户成功") : Result.error("添加用户失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询用户详情
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id) {
        try {
            User user = userService.getById(id);
            if (user == null) {
                return Result.error("用户不存在");
            }
            // 返回统一格式的数据
            Map<String, Object> data = new HashMap<>();
            data.put("userId", user.getUserId());
            data.put("username", user.getUsername());
            data.put("realName", user.getRealName());
            data.put("phone", user.getPhone());
            data.put("role", user.getRole());
            data.put("status", user.getStatus());
            data.put("remark", user.getRemark());
            data.put("createTime", user.getCreateTime());

            return Result.success("查询成功", data);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }


    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    public Result update(@PathVariable Integer id, @RequestBody User user) {
        try {
            user.setUserId(id);
            boolean success = userService.updateUser(user);
            return success ? Result.success("更新成功") : Result.error("更新失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误: " + e.getMessage());
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        try {
            boolean success = userService.deleteUser(id);
            return success ? Result.success("删除成功") : Result.error("删除失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误: " + e.getMessage());
        }
    }

    /**
     * 切换用户状态
     */
    @PutMapping("/{id}/status")
    public Result updateStatus(@PathVariable Integer id, @RequestBody Map<String, Integer> params) {
        try {
            Integer status = params.get("status");
            if (status == null) {
                return Result.error("状态值不能为空");
            }

            User user = userService.getById(id);
            if (user == null) {
                return Result.error("用户不存在");
            }

            user.setStatus(String.valueOf(status));
            boolean success = userService.updateById(user);
            return success ? Result.success(status == 1 ? "启用成功" : "禁用成功")
                    : Result.error("状态切换失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("操作失败: " + e.getMessage());
        }
    }

    /**
     * 导出用户数据
     */
    @GetMapping("/export")
    public Result export(@RequestParam(required = false) String username,
                         @RequestParam(required = false) String role,
                         @RequestParam(required = false) Integer status) {
        // 暂时返回示例路径，实际开发中需实现Excel导出逻辑
        return Result.success("导出成功", "/export/users.xlsx");
    }

    /**
     * 根据角色获取用户列表
     */
    @GetMapping("/listByRole")
    public Result listByRole(@RequestParam String role) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("role", role).eq("deleted", 0);
            List<User> users = userService.list(wrapper);
            users.forEach(user -> user.setPassword(null));
            return Result.success("查询成功", users);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取司机列表
     */
    @GetMapping("/drivers")
    public Result getDrivers() {
        try {
            List<Map<String, Object>> drivers = userService.getDriverList();
            return Result.success("查询成功", drivers);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败");
        }
    }

    /**
     * 获取用户统计
     */
    @GetMapping("/statistics")
    public Result getStatistics() {
        try {
            Map<String, Long> statistics = userService.getUserStatistics();
            return Result.success("查询成功", statistics);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败");
        }
    }

    /**
     * 检查用户名是否可用
     */
    @GetMapping("/checkUsername")
    public Result checkUsername(@RequestParam String username) {
        try {
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("username", username).eq("deleted", 0);
            long count = userService.count(wrapper);
            return Result.success("查询成功", count == 0);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败");
        }
    }

    /**
     * 跳转主页面
     */
    @GetMapping("/main")
    public String main() {
        return "forward:/main.html";
    }
}