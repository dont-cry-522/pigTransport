package com.pigtransport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pigtransport.entity.Disinfection;
import com.pigtransport.mapper.DisinfectionMapper;
import com.pigtransport.service.DisinfectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DisinfectionServiceImpl extends ServiceImpl<DisinfectionMapper, Disinfection>
        implements DisinfectionService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addDisinfection(Disinfection disinfection) {
        // 参数校验
        if (disinfection == null) {
            throw new RuntimeException("消毒记录不能为空");
        }
        if (disinfection.getCarId() == null) {
            throw new RuntimeException("车辆ID不能为空");
        }
        if (!StringUtils.hasText(disinfection.getDisinfectionType())) {
            throw new RuntimeException("消毒类型不能为空");
        }
        if (!StringUtils.hasText(disinfection.getLocation())) {
            throw new RuntimeException("消毒位置不能为空");
        }

        // 设置默认值
        if (disinfection.getDisinfectionTime() == null) {
            disinfection.setDisinfectionTime(new Date());
        }
        if (disinfection.getCreateTime() == null) {
            disinfection.setCreateTime(new Date());
        }
        if (disinfection.getDeleted() == null) {
            disinfection.setDeleted(0);
        }
        if (!StringUtils.hasText(disinfection.getResult())) {
            disinfection.setResult("qualified"); // 默认合格
        }

        // 保存到数据库
        return this.save(disinfection);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDisinfection(Disinfection disinfection) {
        if (disinfection == null || disinfection.getDisinfectionId() == null) {
            throw new RuntimeException("消毒记录信息不完整");
        }

        // 检查记录是否存在
        Disinfection existRecord = this.getById(disinfection.getDisinfectionId());
        if (existRecord == null) {
            throw new RuntimeException("消毒记录不存在");
        }

        // 更新记录
        return this.updateById(disinfection);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDisinfection(Integer disinfectionId) {
        if (disinfectionId == null || disinfectionId <= 0) {
            throw new RuntimeException("消毒记录ID无效");
        }

        // 检查记录是否存在
        Disinfection record = this.getById(disinfectionId);
        if (record == null) {
            throw new RuntimeException("消毒记录不存在");
        }

        // 逻辑删除
        record.setDeleted(1);
        return this.updateById(record);
    }

    @Override
    public List<Disinfection> getDisinfectionsByTaskId(Integer taskId) {
        QueryWrapper<Disinfection> wrapper = new QueryWrapper<>();
        wrapper.eq("task_id", taskId)
                .eq("deleted", 0)
                .orderByDesc("disinfection_time");
        return this.list(wrapper);
    }

    @Override
    public List<Disinfection> getDisinfectionsByDriverId(Integer driverId) {
        QueryWrapper<Disinfection> wrapper = new QueryWrapper<>();
        wrapper.eq("driver_id", driverId)
                .eq("deleted", 0)
                .orderByDesc("disinfection_time");
        return this.list(wrapper);
    }

    @Override
    public List<Disinfection> getDisinfectionsByCarId(Integer carId) {
        QueryWrapper<Disinfection> wrapper = new QueryWrapper<>();
        wrapper.eq("car_id", carId)
                .eq("deleted", 0)
                .orderByDesc("disinfection_time");
        return this.list(wrapper);
    }

    @Override
    public List<Disinfection> getTodayDisinfections() {
        // 这里调用 Mapper 的自定义方法
        return baseMapper.selectTodayDisinfections();
    }

    @Override
    public Map<String, Object> getDisinfectionStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 总消毒次数
        QueryWrapper<Disinfection> totalWrapper = new QueryWrapper<>();
        totalWrapper.eq("deleted", 0);
        Long totalCount = this.count(totalWrapper);
        statistics.put("total", totalCount);

        // 今日消毒次数
        List<Disinfection> todayRecords = this.getTodayDisinfections();
        statistics.put("today", todayRecords != null ? todayRecords.size() : 0);

        // 各类型消毒次数
        try {
            List<Map<String, Object>> typeCounts = baseMapper.countByType();
            statistics.put("byType", typeCounts);
        } catch (Exception e) {
            statistics.put("byType", new HashMap<>());
        }

        return statistics;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addQuickDisinfection(Integer driverId, Integer taskId, Integer carId,
                                        String disinfectionType, String location,
                                        String disinfectant, String remark) {
        try {
            Disinfection record = new Disinfection();
            record.setDriverId(driverId);
            record.setTaskId(taskId);
            record.setCarId(carId);
            record.setDisinfectionType(disinfectionType);
            record.setLocation(location);
            record.setDisinfectant(disinfectant);
            record.setRemark(remark);
            record.setDisinfectionTime(new Date());
            record.setCreateTime(new Date());
            record.setDeleted(0);
            record.setResult("qualified"); // 默认合格

            return this.save(record);
        } catch (Exception e) {
            throw new RuntimeException("添加消毒记录失败: " + e.getMessage());
        }
    }
}