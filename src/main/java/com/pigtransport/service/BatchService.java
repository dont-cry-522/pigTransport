package com.pigtransport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pigtransport.entity.Batch;
import java.util.List;
import java.util.Map;

public interface BatchService extends IService<Batch> {

    // 添加生猪批次
    boolean addBatch(Batch batch);

    // 更新批次信息
    boolean updateBatch(Batch batch);

    // 删除批次（逻辑删除）
    boolean deleteBatch(Integer batchId);

    // 根据养殖户查询批次
    List<Batch> getBatchesByBreeder(Integer breederId);

    // 获取可运输批次列表（用于下拉框）
    List<Map<String, Object>> getAvailableBatchList();

    // 更新批次状态
    boolean updateBatchStatus(Integer batchId, String status);

    // 获取批次统计信息
    Map<String, Object> getBatchStatistics();
}