package com.pigtransport.controller;

import com.pigtransport.mapper.BatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/batch/report")
public class BatchReportController {

    @Autowired
    private BatchMapper batchMapper;

    // 获取各养殖户批次数量报表数据
    @GetMapping("/breederCount")
    public Map<String, Object> getBreederBatchCount() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 直接调用你已有的统计方法
            List<Map<String, Object>> data = batchMapper.countByBreeder();
            result.put("code", 0); // 0表示成功
            result.put("msg", "获取报表数据成功");
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", 1);
            result.put("msg", "获取报表数据失败：" + e.getMessage());
        }
        return result;
    }
}