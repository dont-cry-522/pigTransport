package com.pigtransport.controller;

import com.pigtransport.common.Result;
import com.pigtransport.entity.Vehicle;
import com.pigtransport.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    // 1. 获取车辆列表（带筛选）
    @GetMapping("/list")
    public Result list(@RequestParam(required = false) String keyword,
                       @RequestParam(required = false) String status) {
        try {
            List<Vehicle> vehicleList = vehicleService.getVehicleList(keyword, status);
            return Result.success(vehicleList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取车辆列表失败：" + e.getMessage());
        }
    }

    // 2. 添加车辆
    @PostMapping("/add")
    public Result addVehicle(@RequestBody Vehicle vehicle) {
        try {
            if (!StringUtils.hasText(vehicle.getLicensePlate())) {
                return Result.error("车牌号不能为空");
            }
            boolean success = vehicleService.saveVehicle(vehicle);
            return success ? Result.success("车辆添加成功") : Result.error("车辆添加失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("添加车辆异常：" + e.getMessage());
        }
    }

    // 3. 编辑车辆
    @PutMapping("/update")
    public Result updateVehicle(@RequestBody Vehicle vehicle) {
        try {
            if (vehicle.getVehicleId() == null) {
                return Result.error("车辆ID不能为空");
            }
            if (!StringUtils.hasText(vehicle.getLicensePlate())) {
                return Result.error("车牌号不能为空");
            }
            boolean success = vehicleService.updateVehicle(vehicle);
            return success ? Result.success("车辆编辑成功") : Result.error("车辆编辑失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("编辑车辆异常：" + e.getMessage());
        }
    }

    // 4. 修改车辆状态（✅ 仅保留这一个，无任何重复）
    @PutMapping("/updateStatus")
    public Result updateStatus(
            @RequestParam("vehicleId") Integer vehicleId,
            @RequestParam("status") String status) {
        try {
            // 参数校验
            if (vehicleId == null || vehicleId <= 0) {
                return Result.error("车辆ID无效");
            }
            List<String> validStatus = Arrays.asList("free", "transporting", "maintenance", "resting");
            if (!StringUtils.hasText(status) || !validStatus.contains(status)) {
                return Result.error("状态值无效（仅支持：free/transporting/maintenance/resting）");
            }

            boolean success = vehicleService.updateVehicleStatus(vehicleId, status);
            return success ? Result.success("车辆状态更新成功") : Result.error("车辆状态更新失败（车辆不存在）");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("服务器内部错误：" + e.getMessage());
        }
    }

    // 5. 分配司机
    @PostMapping("/assignDriver")
    public Result assignDriver(@RequestBody Map<String, Object> params) {
        try {
            Integer vehicleId = (Integer) params.get("vehicleId");
            Integer driverId = (Integer) params.get("driverId");
            String driverName = (String) params.get("driverName");

            if (vehicleId == null) {
                return Result.error("车辆ID不能为空");
            }
            if (!StringUtils.hasText(driverName)) {
                return Result.error("司机姓名不能为空");
            }

            boolean success = vehicleService.assignDriver(vehicleId, driverId, driverName);
            return success ? Result.success("司机分配成功") : Result.error("司机分配失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("分配司机异常：" + e.getMessage());
        }
    }

    // 6. 删除车辆
    @DeleteMapping("/delete/{vehicleId}")
    public Result deleteVehicle(@PathVariable Integer vehicleId) {
        try {
            boolean success = vehicleService.deleteVehicle(vehicleId);
            return success ? Result.success("车辆删除成功") : Result.error("车辆删除失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除车辆异常：" + e.getMessage());
        }
    }

    // 7. 移除司机
    @PutMapping("/removeDriver/{vehicleId}")
    public Result removeDriver(@PathVariable Integer vehicleId) {
        try {
            boolean success = vehicleService.removeDriver(vehicleId);
            return success ? Result.success("司机移除成功") : Result.error("司机移除失败");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("移除司机异常：" + e.getMessage());
        }
    }

    // 8. 查询车辆详情
    @GetMapping("/detail/{vehicleId}")
    public Result getVehicleDetail(@PathVariable Integer vehicleId) {
        try {
            Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
            return vehicle != null ? Result.success(vehicle) : Result.error("车辆不存在");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("查询车辆详情失败：" + e.getMessage());
        }
    }
}