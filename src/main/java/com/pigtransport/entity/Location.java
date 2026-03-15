package com.pigtransport.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("location_sign")
public class Location {
    @TableId(value = "sign_id", type = IdType.AUTO)
    private Integer signId;

    @TableField("task_id")
    private Integer taskId;          // 任务ID

    @TableField("driver_id")
    private Integer driverId;        // 司机ID

    @TableField("location")
    private String location;         // 签到位置

    @TableField("latitude")
    private Double latitude;         // 纬度（可选）

    @TableField("longitude")
    private Double longitude;        // 经度（可选）

    @TableField(value = "sign_time", fill = FieldFill.INSERT)
    private Date signTime;           // 签到时间

    @TableField("remark")
    private String remark;           // 备注信息

    @TableField("deleted")
    @TableLogic
    private Integer deleted;
}