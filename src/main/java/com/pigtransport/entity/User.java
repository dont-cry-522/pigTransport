package com.pigtransport.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

/**
 * 用户实体类
 * 对应表：sys_user
 */
@Data
@TableName("sys_user")
public class User {

    /**
     * 用户ID（主键）
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 用户名（唯一）
     */
    @TableField("username")
    private String username;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 角色：farmer(养殖户)/admin(管理员)/driver(司机)
     */
    @TableField("role")
    private String role;

    /**
     * 联系电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 状态：1-启用，0-禁用
     */
    @TableField("status")
    private String status;

    /**
     * 备注信息
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 逻辑删除标记（0-未删除，1-已删除）
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;
}