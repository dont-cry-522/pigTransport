package com.pigtransport.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("vehicle")
public class Vehicle {
    @TableId(value = "vehicle_id", type = IdType.AUTO)
    private Integer vehicleId;

    @TableField("license_plate")
    private String licensePlate;  // 车牌号

    @TableField("model")
    private String model;         // 车型

    @TableField("vehicle_type")
    private String vehicleType;   // 车辆类型（truck/van/refrigerated/other）

    @TableField("capacity")
    private Double capacity;      // 载重能力（吨）

    @TableField("purchase_date")
    private Date purchaseDate;    // 购买日期

    @TableField("mileage")
    private Integer mileage;      // 行驶里程（km）

    @TableField("remark")
    private String remark;        // 备注信息

    @TableField("driver_id")
    private Integer driverId;     // 司机ID（可选，保留用于关联）
    @TableField("driver_name")    // 新增：直接存储司机姓名，无需关联查询
    private String driverName;

    @TableField("status")
    private String status;        // free/transporting/maintenance/resting

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;


    @TableField("deleted")
    @TableLogic  // 这个可以保留，会和yml配置协同工作
    private Integer deleted;
}