package com.pigtransport.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("transport_task")
public class Task {
    @TableId(value = "task_id", type = IdType.AUTO)
    private Integer taskId;

    @TableField("task_code")
    private String taskCode;          // 任务编号（新增）

    @TableField("batch_id")
    private Integer batchId;          // 生猪批次ID

    @TableField("vehicle_id")
    private Integer vehicleId;        // 车辆ID

    @TableField("driver_id")
    private Integer driverId;         // 司机ID（新增）

    @TableField("start_place")
    private String startPlace;        // 出发地（修改字段名）

    @TableField("end_place")
    private String endPlace;          // 目的地（修改字段名）

    @TableField("status")
    private String status;            // pending/transporting/completed/canceled

    @TableField("remark")
    private String remark;            // 备注（新增）

    @TableField("assign_time")
    private Date assignTime;          // 分配时间

    @TableField("finish_time")
    private Date finishTime;          // 完成时间

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @TableField("deleted")
    @TableLogic
    private Integer deleted;
}