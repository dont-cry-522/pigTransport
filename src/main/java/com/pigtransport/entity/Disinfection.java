package com.pigtransport.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("disinfection_record")
public class Disinfection {
    @TableId(value = "disinfection_id", type = IdType.AUTO)
    private Integer disinfectionId;

    @TableField("task_id")
    private Integer taskId;

    @TableField("driver_id")
    private Integer driverId;
    

    @TableField("car_id")
    private Integer carId;

    @TableField("disinfection_type")
    private String disinfectionType;

    @TableField("location")
    private String location;

    @TableField("disinfectant")
    private String disinfectant;

    @TableField("disinfection_time")
    private Date disinfectionTime;

    @TableField("operator")
    private String operator;

    @TableField("remark")
    private String remark;

    // 可选添加
    @TableField("result")
    private String result;  // 或使用枚举

    @TableField("photo_url")
    private String photoUrl;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField("deleted")
    @TableLogic
    private Integer deleted;
}