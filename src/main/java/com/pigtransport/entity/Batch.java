package com.pigtransport.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

@Data
@TableName("pig_batch")
public class Batch {
    @TableId(value = "batch_id", type = IdType.AUTO)
    private Integer id;

    @TableField("breeder_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer breederId;

    @TableField("batch_no")
    @JsonProperty("batchCode")
    private String batchNo;

    @TableField("variety")
    @JsonProperty("breed")
    private String variety;

    @TableField("source")
    private String source;

    @TableField("quantity")
    @JsonProperty("count")
    private Integer quantity;

    @TableField("status")
    private String status;

    @TableField("remark")
    private String remark;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField("deleted")
    @TableLogic  // 必须要有这个注解！
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer deleted;
}