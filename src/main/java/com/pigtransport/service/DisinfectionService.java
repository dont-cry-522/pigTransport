package com.pigtransport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pigtransport.entity.Disinfection;
import java.util.List;
import java.util.Map;

public interface DisinfectionService extends IService<Disinfection> {

    // 添加消毒记录
    boolean addDisinfection(Disinfection Disinfection);

    // 更新消毒记录
    boolean updateDisinfection(Disinfection Disinfection);

    // 删除消毒记录（逻辑删除）
    boolean deleteDisinfection(Integer disinfectionId);

    // 根据任务ID查询消毒记录
    List<Disinfection> getDisinfectionsByTaskId(Integer taskId);

    // 根据司机ID查询消毒记录
    List<Disinfection> getDisinfectionsByDriverId(Integer driverId);

    // 根据车辆ID查询消毒记录
    List<Disinfection> getDisinfectionsByCarId(Integer carId);

    // 获取今日消毒记录
    List<Disinfection> getTodayDisinfections();

    // 获取消毒统计
    Map<String, Object> getDisinfectionStatistics();

    // 快速添加消毒记录（简化版）
    boolean addQuickDisinfection(Integer driverId, Integer taskId, Integer carId, String disinfectionType,
                                 String location, String disinfectant, String remark);
}