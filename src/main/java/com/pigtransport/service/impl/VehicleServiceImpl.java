package com.pigtransport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pigtransport.entity.Vehicle;
import com.pigtransport.mapper.VehicleMapper;
import com.pigtransport.service.VehicleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl extends ServiceImpl<VehicleMapper, Vehicle> implements VehicleService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addVehicle(Vehicle vehicle) {
        // 参数校验
        if (vehicle == null || !StringUtils.hasText(vehicle.getLicensePlate())) {
            throw new RuntimeException("车牌号不能为空");
        }

        // 检查车牌号是否已存在
        Vehicle existVehicle = baseMapper.selectByLicensePlate(vehicle.getLicensePlate());
        if (existVehicle != null) {
            throw new RuntimeException("车牌号已存在");
        }

        // 设置默认值
        if (vehicle.getStatus() == null) {
            vehicle.setStatus("free");  // 默认空闲状态
        }
        vehicle.setCreateTime(new Date());
        vehicle.setDeleted(0);

        // 保存到数据库
        int result = baseMapper.insert(vehicle);
        return result > 0;
    }

    // ========== 新增兼容方法：匹配Controller中的 saveVehicle() ==========
    @Override
    public boolean saveVehicle(Vehicle vehicle) {
        // 直接调用你原有实现的 addVehicle 方法
        return this.addVehicle(vehicle);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteVehicle(Integer vehicleId) {
        if (vehicleId == null) {
            throw new RuntimeException("车辆ID不能为空");
        }
        return baseMapper.deleteById(vehicleId) > 0;

    }

    @Override
    public List<Vehicle> getVehiclesByStatus(String status) {
        QueryWrapper<Vehicle> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status)
                .eq("deleted", 0)
                .orderByDesc("create_time");
        return baseMapper.selectList(wrapper);
    }

    // ========== 新增兼容方法：匹配Controller中的 getVehicleList() ==========
    @Override
    public List<Vehicle> getVehicleList(String keyword, String status) {
        // 直接调用你原有实现的 getVehiclesByKeywordAndStatus 方法
        return this.getVehiclesByKeywordAndStatus(keyword, status);
    }

    @Override
    public List<Map<String, Object>> getFreeVehicleList() {
        List<Vehicle> freeVehicles = baseMapper.selectFreeVehicles();
        return freeVehicles.stream().map(vehicle -> {
            Map<String, Object> map = new HashMap<>();
            map.put("vehicleId", vehicle.getVehicleId());
            map.put("licensePlate", vehicle.getLicensePlate());
            map.put("model", vehicle.getModel());
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateVehicleStatus(Integer vehicleId, String status) {
        if (vehicleId == null || !StringUtils.hasText(status)) {
            throw new RuntimeException("参数不能为空");
        }

        int result = baseMapper.updateStatus(vehicleId, status);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeDriver(Integer vehicleId) {
        if (vehicleId == null) {
            throw new RuntimeException("车辆ID不能为空");
        }

        Vehicle vehicle = baseMapper.selectById(vehicleId);
        if (vehicle == null) {
            throw new RuntimeException("车辆不存在");
        }

        vehicle.setDriverId(null);
        vehicle.setStatus("free");  // 移除司机后变为空闲状态

        int result = baseMapper.updateById(vehicle);
        return result > 0;
    }

    // VehicleServiceImpl.java
    @Override
    public Vehicle getVehicleDetail(Integer vehicleId) {
        if (vehicleId == null) {
            throw new RuntimeException("车辆ID不能为空");
        }

        // ✅ MyBatis-Plus会自动添加 deleted=0 条件
        return baseMapper.selectById(vehicleId);


    }
    // ========== 新增兼容方法：匹配Controller中的 getVehicleById() ==========
    @Override
    public Vehicle getVehicleById(Integer vehicleId) {
        // 直接调用你原有实现的 getVehicleDetail 方法
        return this.getVehicleDetail(vehicleId);
    }

    @Override
    public List<Vehicle> getVehiclesByKeywordAndStatus(String keyword, String status) {
        // 处理空值，避免SQL拼接问题
        keyword = StringUtils.hasText(keyword) ? keyword : "";
        status = StringUtils.hasText(status) ? status : "";
        return baseMapper.selectByKeywordAndStatus(keyword, status);
    }

    // 修正：updateVehicle 方法支持更新所有字段
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateVehicle(Vehicle vehicle) {
        if (vehicle == null || vehicle.getVehicleId() == null) {
            throw new RuntimeException("车辆信息不完整");
        }

        Vehicle existVehicle = baseMapper.selectById(vehicle.getVehicleId());
        if (existVehicle == null) {
            throw new RuntimeException("车辆不存在");
        }

        // 覆盖更新所有非空字段（替代原来的逐个判断）
        if (StringUtils.hasText(vehicle.getLicensePlate())) {
            existVehicle.setLicensePlate(vehicle.getLicensePlate());
        }
        if (StringUtils.hasText(vehicle.getModel())) {
            existVehicle.setModel(vehicle.getModel());
        }
        if (StringUtils.hasText(vehicle.getVehicleType())) {
            existVehicle.setVehicleType(vehicle.getVehicleType());
        }
        if (vehicle.getCapacity() != null) {
            existVehicle.setCapacity(vehicle.getCapacity());
        }
        if (vehicle.getPurchaseDate() != null) {
            existVehicle.setPurchaseDate(vehicle.getPurchaseDate());
        }
        if (vehicle.getMileage() != null) {
            existVehicle.setMileage(vehicle.getMileage());
        }
        if (StringUtils.hasText(vehicle.getRemark())) {
            existVehicle.setRemark(vehicle.getRemark());
        }
        if (vehicle.getDriverId() != null) {
            existVehicle.setDriverId(vehicle.getDriverId());
        }
        if (StringUtils.hasText(vehicle.getStatus())) {
            existVehicle.setStatus(vehicle.getStatus());
        }

        int result = baseMapper.updateById(existVehicle);
        return result > 0;
    }

    // 可选：修改assignDriver逻辑，不强制改状态（让前端控制）
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignDriver(Integer vehicleId, Integer driverId, String driverName) {
        if (vehicleId == null) {
            throw new RuntimeException("车辆ID不能为空");
        }

        Vehicle vehicle = baseMapper.selectById(vehicleId);
        if (vehicle == null) {
            throw new RuntimeException("车辆不存在");
        }

        // 同时更新司机ID和司机姓名
        vehicle.setDriverId(driverId);       // 可选：如果需要存ID的话
        vehicle.setDriverName(driverName);   // 核心：直接存司机姓名到vehicle表

        // 可选：取消强制改状态，由前端控制
        // vehicle.setStatus("transporting");

        int result = baseMapper.updateById(vehicle);
        return result > 0;
    }
}