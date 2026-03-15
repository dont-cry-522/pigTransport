package com.pigtransport.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pigtransport.common.LayuiTableResult;
import com.pigtransport.common.Result;
import com.pigtransport.entity.Batch;
import com.pigtransport.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/batch")
public class BatchController {

    @Autowired
    private BatchService batchService;

    /**
     * 批次列表（适配Layui表格，支持分页+搜索）
     */
    @GetMapping("/list")
    public LayuiTableResult list(
            @RequestParam(required = false) String batchCode,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            QueryWrapper<Batch> wrapper = new QueryWrapper<>();

            if (batchCode != null && !batchCode.isEmpty()) {
                wrapper.like("batch_no", batchCode);
            }
            if (breed != null && !breed.isEmpty()) {
                wrapper.like("variety", breed);
            }
            if (status != null && !status.isEmpty()) {
                wrapper.eq("status", status);
            }

            wrapper.orderByDesc("create_time");

            Page<Batch> pageParam = new Page<>(page, limit);
            IPage<Batch> batchPage = batchService.page(pageParam, wrapper);

            return LayuiTableResult.success(batchPage.getTotal(), batchPage.getRecords());
        } catch (Exception e) {
            e.printStackTrace();
            return LayuiTableResult.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据养殖户查询批次
     */
    @GetMapping("/listByBreeder")
    public Result listByBreeder(@RequestParam Integer breederId) {
        try {
            List<Batch> batches = batchService.getBatchesByBreeder(breederId);
            return Result.success("查询成功", batches);
        } catch (Exception e) {
            return Result.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 添加批次（自动设置养殖户ID）
     */
    @PostMapping("/add")
    public Result add(@RequestBody Batch batch, HttpServletRequest request) {
        try {
            // 获取当前登录的养殖户ID（这里需要根据你的认证系统实现）
            Integer currentBreederId = getCurrentBreederId(request);
            if (currentBreederId == null) {
                return Result.error("未获取到养殖户信息，请先登录");
            }

            // 设置养殖户ID
            batch.setBreederId(currentBreederId);

            // 设置默认状态
            if (batch.getStatus() == null || batch.getStatus().isEmpty()) {
                batch.setStatus("available");
            }

            boolean success = batchService.addBatch(batch);
            return success ? Result.success("添加成功") : Result.error("添加失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误");
        }
    }

    /**
     * 更新批次
     */
// BatchController.java 修改
// @PutMapping("/update")  // 原来的
    @PostMapping("/update")    // 改为POST
    public Result update(@RequestBody Batch batch) {
        try {
            boolean success = batchService.updateBatch(batch);
            return success ? Result.success("更新成功") : Result.error("更新失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误");
        }
    }

    /**
     * 删除批次
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        try {
            boolean success = batchService.deleteBatch(id);
            return success ? Result.success("删除成功") : Result.error("删除失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误");
        }
    }

    /**
     * 获取批次详情（用于编辑）
     */
    @GetMapping("/detail")
    public Result detail(@RequestParam Integer id) {
        try {
            Batch batch = batchService.getById(id);
            if (batch == null || batch.getDeleted() == 1) {
                return Result.error("批次不存在");
            }
            return Result.success("查询成功", batch);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败");
        }
    }

    /**
     * 获取可运输批次列表
     */
    @GetMapping("/availableList")
    public Result getAvailableBatches() {
        try {
            List<Map<String, Object>> batches = batchService.getAvailableBatchList();
            return Result.success("查询成功", batches);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败");
        }
    }

    /**
     * 更新批次状态
     */
    @PutMapping("/updateStatus")
    public Result updateStatus(@RequestParam Integer batchId,
                               @RequestParam String status) {
        try {
            boolean success = batchService.updateBatchStatus(batchId, status);
            return success ? Result.success("状态更新成功") : Result.error("状态更新失败");
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误");
        }
    }

    /**
     * 获取批次统计
     */
    @GetMapping("/statistics")
    public Result getStatistics() {
        try {
            Map<String, Object> statistics = batchService.getBatchStatistics();
            return Result.success("查询成功", statistics);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询失败");
        }
    }

    /**
     * 获取当前登录养殖户ID（示例方法，需要根据你的认证系统实现）
     */
    private Integer getCurrentBreederId(HttpServletRequest request) {
        // 实现方式1：从session获取
        // Object breederObj = request.getSession().getAttribute("breederId");
        // if (breederObj != null) return (Integer) breederObj;

        // 实现方式2：从token获取（JWT）
        // String token = request.getHeader("Authorization");
        // 解析token获取breederId

        // 实现方式3：临时方案 - 固定返回一个值（测试用）
        return 2; // 临时返回养殖户ID=2，实际应该从认证信息获取
    }
}