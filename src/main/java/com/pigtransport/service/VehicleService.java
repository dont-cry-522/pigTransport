package com.pigtransport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pigtransport.entity.Vehicle;
import java.util.List;
import java.util.Map;

public interface VehicleService extends IService<Vehicle> {

    // 原有方法（保留不变）
    boolean addVehicle(Vehicle vehicle);
    boolean updateVehicle(Vehicle vehicle);
    boolean deleteVehicle(Integer vehicleId);
    List<Vehicle> getVehiclesByStatus(String status);
    List<Map<String, Object>> getFreeVehicleList();
    boolean updateVehicleStatus(Integer vehicleId, String status);
    boolean assignDriver(Integer vehicleId, Integer driverId, String driverName);
    boolean removeDriver(Integer vehicleId);
    Vehicle getVehicleDetail(Integer vehicleId);
    List<Vehicle> getVehiclesByKeywordAndStatus(String keyword, String status);

    // ========== 新增3个兼容方法定义（匹配Controller调用） ==========
    // 匹配Controller中的 saveVehicle()
    boolean saveVehicle(Vehicle vehicle);

    // 匹配Controller中的 getVehicleList()
    List<Vehicle> getVehicleList(String keyword, String status);

    // 匹配Controller中的 getVehicleById()
    Vehicle getVehicleById(Integer vehicleId);
}