package com.pigtransport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pigtransport.entity.Location;
import java.util.List;
import java.util.Map;

public interface LocationService extends IService<Location> {

    // 添加位置签到
    boolean addLocation(Location Location);

    // 更新签到记录
    boolean updateLocation(Location Location);

    // 删除签到记录（逻辑删除）
    boolean deleteLocation(Integer signId);

    // 根据任务ID查询签到记录
    List<Location> getSignsByTaskId(Integer taskId);

    // 根据司机ID查询签到记录
    List<Location> getSignsByDriverId(Integer driverId);

    // 获取今日签到记录
    List<Location> getTodaySigns();

    // 获取签到统计
    Map<String, Object> getSignStatistics();

    // 司机签到（简化版）
    boolean driverSign(Integer driverId, Integer taskId, String location, String remark);
}