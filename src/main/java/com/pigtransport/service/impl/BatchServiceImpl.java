package com.pigtransport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pigtransport.entity.Batch;
import com.pigtransport.mapper.BatchMapper;
import com.pigtransport.service.BatchService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BatchServiceImpl extends ServiceImpl<BatchMapper, Batch> implements BatchService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addBatch(Batch batch) {
        // 参数校验
        if (batch == null || batch.getBreederId() == null) {
            throw new RuntimeException("养殖户ID不能为空");
        }
        if (!StringUtils.hasText(batch.getVariety())) {
            throw new RuntimeException("品种不能为空");
        }
        if (batch.getQuantity() == null || batch.getQuantity() <= 0) {
            throw new RuntimeException("数量必须大于0");
        }

        // 设置默认值
        if (batch.getStatus() == null) {
            batch.setStatus("available");  // 默认可运输状态
        }
        if (batch.getCreateTime() == null) {
            batch.setCreateTime(new Date());
        }

        // 设置source字段的默认值
        if (!StringUtils.hasText(batch.getSource())) {
            batch.setSource("未指定来源");  // 设置默认值
        }

        batch.setDeleted(0);

        // 保存到数据库
        boolean result = this.save(batch);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatch(Batch batch) {
        if (batch == null || batch.getId() == null) {  // 注意：改为id
            throw new RuntimeException("批次信息不完整");
        }

        Batch existBatch = this.getById(batch.getId());
        if (existBatch == null) {
            throw new RuntimeException("批次不存在");
        }

        // 更新字段（只更新非空字段）
        if (batch.getBreederId() != null) {
            existBatch.setBreederId(batch.getBreederId());
        }
        if (StringUtils.hasText(batch.getVariety())) {
            existBatch.setVariety(batch.getVariety());
        }
        if (StringUtils.hasText(batch.getSource())) {
            existBatch.setSource(batch.getSource());
        }
        if (StringUtils.hasText(batch.getBatchNo())) {
            existBatch.setBatchNo(batch.getBatchNo());
        }
        if (batch.getQuantity() != null) {
            existBatch.setQuantity(batch.getQuantity());
        }
        if (StringUtils.hasText(batch.getStatus())) {
            existBatch.setStatus(batch.getStatus());
        }
        if (StringUtils.hasText(batch.getRemark())) {
            existBatch.setRemark(batch.getRemark());
        }

        boolean result = this.updateById(existBatch);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBatch(Integer batchId) {
        System.out.println("=== 开始逻辑删除，批次ID: " + batchId + " ===");

        if (batchId == null) {
            throw new RuntimeException("批次ID不能为空");
        }

        // 方法1：直接调用removeById（会自动处理逻辑删除）
        boolean result = this.removeById(batchId);
        System.out.println("removeById结果: " + result);

        // 方法2：或者使用lambdaUpdate
        // boolean result = this.lambdaUpdate()
        //         .eq(Batch::getId, batchId)
        //         .set(Batch::getDeleted, 1)
        //         .update();

        return result;
    }

    @Override
    public List<Batch> getBatchesByBreeder(Integer breederId) {
        QueryWrapper<Batch> wrapper = new QueryWrapper<>();
        wrapper.eq("breeder_id", breederId)
                .eq("deleted", 0)
                .orderByDesc("create_time");
        return this.list(wrapper);
    }

    @Override
    public List<Map<String, Object>> getAvailableBatchList() {
        List<Batch> availableBatches = baseMapper.selectAvailableBatches();
        return availableBatches.stream().map(batch -> {
            Map<String, Object> map = new HashMap<>();
            map.put("batchId", batch.getId());
            map.put("variety", batch.getVariety());
            map.put("source", batch.getSource());
            map.put("quantity", batch.getQuantity());
            map.put("breederId", batch.getBreederId());
            map.put("batchNo", batch.getBatchNo());
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchStatus(Integer batchId, String status) {
        if (batchId == null || !StringUtils.hasText(status)) {
            throw new RuntimeException("参数不能为空");
        }

        Batch batch = this.getById(batchId);
        if (batch == null) {
            throw new RuntimeException("批次不存在");
        }

        batch.setStatus(status);
        boolean result = this.updateById(batch);
        return result;
    }

    @Override
    public Map<String, Object> getBatchStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 总批次数量
        QueryWrapper<Batch> totalWrapper = new QueryWrapper<>();
        totalWrapper.eq("deleted", 0);
        Long totalCount = this.count(totalWrapper);
        statistics.put("total", totalCount);

        // 各状态批次数量
        String[] statuses = {"available", "transported"};
        for (String status : statuses) {
            QueryWrapper<Batch> statusWrapper = new QueryWrapper<>();
            statusWrapper.eq("status", status).eq("deleted", 0);
            Long count = this.count(statusWrapper);
            statistics.put(status, count);
        }

        // 总生猪数量
        QueryWrapper<Batch> quantityWrapper = new QueryWrapper<>();
        quantityWrapper.select("SUM(quantity) as total_quantity")
                .eq("deleted", 0);
        List<Map<String, Object>> result = this.listMaps(quantityWrapper);
        if (!result.isEmpty() && result.get(0).get("total_quantity") != null) {
            statistics.put("totalQuantity", result.get(0).get("total_quantity"));
        } else {
            statistics.put("totalQuantity", 0);
        }

        return statistics;
    }
}