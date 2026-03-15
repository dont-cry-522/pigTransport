package com.pigtransport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pigtransport.entity.Location;
import com.pigtransport.mapper.LocationMapper;
import com.pigtransport.service.LocationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LocationServiceImpl extends ServiceImpl<LocationMapper, Location>
        implements LocationService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addLocation(Location location) {
        if (location == null || location.getDriverId() == null) {
            throw new RuntimeException("司机ID不能为空");
        }
        if (!StringUtils.hasText(location.getLocation())) {
            throw new RuntimeException("签到位置不能为空");
        }

        location.setSignTime(new Date());
        if (location.getDeleted() == null) {
            location.setDeleted(0);
        }

        return this.save(location);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLocation(Location location) {
        if (location == null || location.getSignId() == null) {
            throw new RuntimeException("签到记录信息不完整");
        }

        Location existSign = this.getById(location.getSignId());
        if (existSign == null) {
            throw new RuntimeException("签到记录不存在");
        }

        if (StringUtils.hasText(location.getLocation())) {
            existSign.setLocation(location.getLocation());
        }
        if (location.getLatitude() != null) {
            existSign.setLatitude(location.getLatitude());
        }
        if (location.getLongitude() != null) {
            existSign.setLongitude(location.getLongitude());
        }
        if (StringUtils.hasText(location.getRemark())) {
            existSign.setRemark(location.getRemark());
        }

        return this.updateById(existSign);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLocation(Integer signId) {
        try {
            System.out.println("=== 开始删除逻辑 ===");
            System.out.println("要删除的signId：" + signId);

            // 方法1：使用UpdateWrapper直接更新deleted字段
            UpdateWrapper<Location> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("sign_id", signId)
                    .set("deleted", 1);

            boolean result = this.update(updateWrapper);
            System.out.println("删除结果（UpdateWrapper方式）：" + result);

            // 方法2：或者使用baseMapper直接执行SQL
            // int rows = baseMapper.deleteById(signId); // 物理删除
            // boolean result = rows > 0;

            return result;
        } catch (Exception e) {
            System.out.println("删除过程中出错：" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<Location> getSignsByTaskId(Integer taskId) {
        QueryWrapper<Location> wrapper = new QueryWrapper<>();
        wrapper.eq("task_id", taskId)
                .eq("deleted", 0)
                .orderByDesc("sign_time");
        return this.list(wrapper);
    }

    @Override
    public List<Location> getSignsByDriverId(Integer driverId) {
        QueryWrapper<Location> wrapper = new QueryWrapper<>();
        wrapper.eq("driver_id", driverId)
                .eq("deleted", 0)
                .orderByDesc("sign_time");
        return this.list(wrapper);
    }

    @Override
    public List<Location> getTodaySigns() {
        QueryWrapper<Location> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0)
                .apply("DATE(sign_time) = CURDATE()")
                .orderByDesc("sign_time");
        return this.list(wrapper);
    }

    @Override
    public Map<String, Object> getSignStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 总签到次数
        QueryWrapper<Location> totalWrapper = new QueryWrapper<>();
        totalWrapper.eq("deleted", 0);
        Long totalCount = this.count(totalWrapper);
        statistics.put("total", totalCount);

        // 今日签到次数
        List<Location> todaySigns = this.getTodaySigns();
        statistics.put("today", todaySigns.size());

        // 如果需要各司机签到次数，可以在这里添加
        // List<Map<String, Object>> driverCounts = baseMapper.countByDriver();
        // statistics.put("byDriver", driverCounts);

        return statistics;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean driverSign(Integer driverId, Integer taskId, String location, String remark) {
        try {
            Location sign = new Location();
            sign.setDriverId(driverId);
            sign.setTaskId(taskId);
            sign.setLocation(location);
            sign.setRemark(remark);
            sign.setSignTime(new Date());
            sign.setDeleted(0);

            return this.save(sign);
        } catch (Exception e) {
            throw new RuntimeException("签到失败: " + e.getMessage());
        }
    }
}