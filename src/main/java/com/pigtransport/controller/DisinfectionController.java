package com.pigtransport.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pigtransport.common.LayuiTableResult;
import com.pigtransport.common.Result;
import com.pigtransport.entity.Disinfection;
import com.pigtransport.service.DisinfectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 消毒记录控制器
 */
@RestController
@RequestMapping("/api/disinfection")
public class DisinfectionController {

    @Autowired
    private DisinfectionService disinfectionService;

    @Value("${upload.base-path:D:/pigtransport/upload/images/}")
    private String uploadBasePath;

    @Value("${upload.img-prefix:http://localhost:8080/images/}")
    private String imgPrefix;

    /**
     * 消毒记录列表
     */
    @GetMapping("/list")
    public LayuiTableResult list(
            @RequestParam(required = false) Integer taskId,
            @RequestParam(required = false) Integer driverId,
            @RequestParam(required = false) Integer carId,
            @RequestParam(required = false) String disinfectionType,
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            QueryWrapper<Disinfection> wrapper = new QueryWrapper<>();
            if (taskId != null) wrapper.eq("task_id", taskId);
            if (driverId != null) wrapper.eq("driver_id", driverId);
            if (carId != null) wrapper.eq("car_id", carId);
            if (disinfectionType != null && !disinfectionType.isEmpty()) {
                wrapper.like("disinfection_type", disinfectionType);
            }
            wrapper.orderByDesc("disinfection_time");

            Page<Disinfection> pageParam = new Page<>(page, limit);
            IPage<Disinfection> disinfectionPage = disinfectionService.page(pageParam, wrapper);

            // 转换为前端需要的格式
            List<Map<String, Object>> resultList = disinfectionPage.getRecords().stream().map(d -> {
                Map<String, Object> map = new HashMap<>();
                map.put("disinfectionId", d.getDisinfectionId());
                map.put("carId", d.getCarId());
                map.put("driverId", d.getDriverId());
                map.put("taskId", d.getTaskId());
                map.put("disinfectionType", d.getDisinfectionType());
                map.put("location", d.getLocation());
                map.put("disinfectant", d.getDisinfectant());
                map.put("imgUrl", d.getPhotoUrl()); // photoUrl -> imgUrl
                map.put("disinfectionTime", d.getDisinfectionTime());
                map.put("createTime", d.getCreateTime());
                map.put("remark", d.getRemark());
                map.put("id", d.getDisinfectionId()); // 前端需要的id字段
                map.put("operator", d.getOperator()); // 操作人员
                map.put("result", d.getResult());     // 消毒结果
                return map;
            }).collect(Collectors.toList());

            return LayuiTableResult.success(disinfectionPage.getTotal(), resultList);
        } catch (Exception e) {
            e.printStackTrace();
            return LayuiTableResult.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/listByTask")
    public Result listByTask(@RequestParam Integer taskId) {
        try {
            List<Disinfection> records = disinfectionService.getDisinfectionsByTaskId(taskId);
            return Result.success("查询成功", records);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/listByDriver")
    public Result listByDriver(@RequestParam Integer driverId) {
        try {
            List<Disinfection> records = disinfectionService.getDisinfectionsByDriverId(driverId);
            return Result.success("查询成功", records);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/listByCar")
    public Result listByCar(@RequestParam Integer carId) {
        try {
            List<Disinfection> records = disinfectionService.getDisinfectionsByCarId(carId);
            return Result.success("查询成功", records);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/today")
    public Result getTodayDisinfections() {
        try {
            List<Disinfection> records = disinfectionService.getTodayDisinfections();
            return Result.success("查询成功", records);
        } catch (Exception e) {
            return Result.error("查询失败");
        }
    }

    /**
     * 添加消毒记录
     */
    @PostMapping("/add")
    public Result add(@RequestBody Map<String, Object> params) {
        try {
            System.out.println("添加参数：" + params);

            // 1. 转换参数到实体类
            Disinfection disinfection = new Disinfection();

            // 必填字段
            disinfection.setCarId(Integer.parseInt(params.get("carId").toString()));
            disinfection.setDisinfectionType(params.get("disinfectionType").toString());
            disinfection.setLocation(params.get("location").toString());

            // 设置默认值
            disinfection.setDisinfectionTime(new Date());
            disinfection.setResult("qualified"); // 默认合格

            // 可选字段
            if (params.get("driverId") != null && !params.get("driverId").toString().isEmpty()) {
                disinfection.setDriverId(Integer.parseInt(params.get("driverId").toString()));
            }
            if (params.get("taskId") != null && !params.get("taskId").toString().isEmpty()) {
                disinfection.setTaskId(Integer.parseInt(params.get("taskId").toString()));
            }
            if (params.get("disinfectant") != null) {
                disinfection.setDisinfectant(params.get("disinfectant").toString());
            }
            if (params.get("remark") != null) {
                disinfection.setRemark(params.get("remark").toString());
            }
            if (params.get("operator") != null) {
                disinfection.setOperator(params.get("operator").toString());
            } else {
                disinfection.setOperator("system"); // 默认操作人员
            }
            if (params.get("result") != null) {
                disinfection.setResult(params.get("result").toString());
            }

            // 关键：把前端的imgUrl赋值给实体类的photoUrl
            if (params.get("imgUrl") != null && !params.get("imgUrl").toString().isEmpty()) {
                disinfection.setPhotoUrl(params.get("imgUrl").toString());
            }

            boolean success = disinfectionService.save(disinfection);
            return success ? Result.success("添加成功") : Result.error("添加失败");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误: " + e.getMessage());
        }
    }

    @PostMapping("/quickAdd")
    public Result quickAdd(@RequestParam Integer driverId,
                           @RequestParam(required = false) Integer taskId,
                           @RequestParam Integer carId,
                           @RequestParam String disinfectionType,
                           @RequestParam String location,
                           @RequestParam(required = false) String disinfectant,
                           @RequestParam(required = false) String remark) {
        try {
            boolean success = disinfectionService.addQuickDisinfection(driverId, taskId, carId,
                    disinfectionType, location,
                    disinfectant, remark);
            return success ? Result.success("添加成功") : Result.error("添加失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            return Result.error("系统错误");
        }
    }

    /**
     * 删除消毒记录
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable String id) {
        try {
            System.out.println("删除ID：" + id);

            // 检查参数
            if (id == null || "undefined".equals(id) || "null".equals(id) || id.trim().isEmpty()) {
                return Result.error("参数错误：记录ID不能为空");
            }

            // 转换为整数
            Integer disinfectionId;
            try {
                disinfectionId = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                return Result.error("参数错误：记录ID必须是数字");
            }

            // 检查ID有效性
            if (disinfectionId <= 0) {
                return Result.error("参数错误：记录ID无效");
            }

            // 直接使用 MyBatis-Plus 的逻辑删除
            boolean success = disinfectionService.removeById(disinfectionId);

            return success ? Result.success("删除成功") : Result.error("删除失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败：" + e.getMessage());
        }
    }
    @GetMapping("/statistics")
    public Result getStatistics() {
        try {
            Map<String, Object> statistics = disinfectionService.getDisinfectionStatistics();
            return Result.success("查询成功", statistics);
        } catch (Exception e) {
            return Result.error("查询失败");
        }
    }

    /**
     * 消毒图片上传接口
     */
    @PostMapping("/upload/img")
    public Result uploadImg(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println("=== 项目内图片上传 ===");

            // 获取项目根目录
            String projectRoot = System.getProperty("user.dir");
            System.out.println("项目根目录：" + projectRoot);

            // 在项目内创建 img/disinfection 目录
            String imgDir = projectRoot + File.separator + "img" + File.separator + "disinfection" + File.separator;
            File uploadDir = new File(imgDir);

            System.out.println("图片目录：" + imgDir);
            System.out.println("目录是否存在：" + uploadDir.exists());

            // 创建目录（如果不存在）
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                System.out.println("创建目录结果：" + created);
                if (!created) {
                    return Result.error("图片目录创建失败");
                }
            }

            // 检查目录权限
            System.out.println("目录可写：" + uploadDir.canWrite());

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();

            // 校验文件类型
            if (!suffix.matches("\\.(jpg|jpeg|png|gif|bmp|webp)$")) {
                return Result.error("仅支持图片格式：jpg, png, gif, bmp, webp");
            }

            String newFilename = UUID.randomUUID().toString().replace("-", "") + suffix;
            System.out.println("新文件名：" + newFilename);

            // 保存文件
            File targetFile = new File(uploadDir, newFilename);
            System.out.println("保存路径：" + targetFile.getAbsolutePath());

            file.transferTo(targetFile);

            // 检查文件是否保存成功
            if (!targetFile.exists()) {
                return Result.error("文件保存失败");
            }
            System.out.println("文件保存成功，大小：" + targetFile.length() + " bytes");

            // 返回URL路径
            String imgUrl = "/img/disinfection/" + newFilename;
            System.out.println("图片URL：" + imgUrl);
            System.out.println("=== 上传成功 ===");

            return Result.success("上传成功", Map.of("url", imgUrl));

        } catch (IOException e) {
            System.out.println("IO异常：" + e.getMessage());
            e.printStackTrace();
            return Result.error("文件保存失败：" + e.getMessage());
        } catch (Exception e) {
            System.out.println("未知异常：" + e.getMessage());
            e.printStackTrace();
            return Result.error("上传失败：" + e.getMessage());
        }
    }

    /**
     * 查询详情
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable("id") Integer id) {
        try {
            System.out.println("查询详情ID：" + id);

            if (id == null || id <= 0) {
                return Result.error("参数错误：记录ID无效");
            }

            Disinfection disinfection = disinfectionService.getById(id);
            if (disinfection == null) {
                return Result.error("记录不存在");
            }

            // 构建返回数据
            Map<String, Object> result = new HashMap<>();
            result.put("id", disinfection.getDisinfectionId());
            result.put("disinfectionId", disinfection.getDisinfectionId());
            result.put("carId", disinfection.getCarId());
            result.put("driverId", disinfection.getDriverId());
            result.put("taskId", disinfection.getTaskId());
            result.put("disinfectionType", disinfection.getDisinfectionType());
            result.put("location", disinfection.getLocation());
            result.put("disinfectant", disinfection.getDisinfectant());
            result.put("imgUrl", disinfection.getPhotoUrl());
            result.put("remark", disinfection.getRemark());
            result.put("operator", disinfection.getOperator());
            result.put("result", disinfection.getResult());
            result.put("disinfectionTime", disinfection.getDisinfectionTime());
            result.put("createTime", disinfection.getCreateTime());

            System.out.println("返回详情数据：" + result);
            return Result.success("查询成功", result);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败：" + e.getMessage());
        }
    }
    /**
     * 更新消毒记录
     */
    @PutMapping("/update")
    public Result update(@RequestBody Map<String, Object> params) {
        try {
            System.out.println("更新参数：" + params);

            // 1. 检查ID
            Integer disinfectionId = null;
            if (params.get("disinfectionId") != null) {
                disinfectionId = Integer.parseInt(params.get("disinfectionId").toString());
            } else if (params.get("id") != null) {
                disinfectionId = Integer.parseInt(params.get("id").toString());
            }

            if (disinfectionId == null || disinfectionId <= 0) {
                return Result.error("记录ID不能为空或无效");
            }

            // 2. 先查询记录是否存在
            Disinfection existingRecord = disinfectionService.getById(disinfectionId);
            if (existingRecord == null) {
                return Result.error("记录不存在");
            }

            // 3. 更新字段
            Disinfection disinfection = new Disinfection();
            disinfection.setDisinfectionId(disinfectionId);

            // 必填字段
            if (params.get("carId") != null) {
                disinfection.setCarId(Integer.parseInt(params.get("carId").toString()));
            }
            if (params.get("disinfectionType") != null) {
                disinfection.setDisinfectionType(params.get("disinfectionType").toString());
            }
            if (params.get("location") != null) {
                disinfection.setLocation(params.get("location").toString());
            }

            // 可选字段
            if (params.get("driverId") != null && !params.get("driverId").toString().isEmpty()) {
                disinfection.setDriverId(Integer.parseInt(params.get("driverId").toString()));
            }
            if (params.get("taskId") != null && !params.get("taskId").toString().isEmpty()) {
                disinfection.setTaskId(Integer.parseInt(params.get("taskId").toString()));
            }
            if (params.get("disinfectant") != null) {
                disinfection.setDisinfectant(params.get("disinfectant").toString());
            }
            if (params.get("remark") != null) {
                disinfection.setRemark(params.get("remark").toString());
            }
            if (params.get("operator") != null) {
                disinfection.setOperator(params.get("operator").toString());
            }
            if (params.get("result") != null) {
                disinfection.setResult(params.get("result").toString());
            }

            // 关键：把前端的imgUrl赋值给实体类的photoUrl
            if (params.get("imgUrl") != null) {
                disinfection.setPhotoUrl(params.get("imgUrl").toString());
            }

            // 4. 执行更新
            boolean success = disinfectionService.updateById(disinfection);
            return success ? Result.success("更新成功") : Result.error("更新失败");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误: " + e.getMessage());
        }
    }
}