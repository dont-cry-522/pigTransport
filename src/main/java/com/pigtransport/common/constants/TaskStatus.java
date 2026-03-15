package com.pigtransport.common.constants;

public class TaskStatus {
    public static final String PENDING = "pending";         // 待分配
    public static final String TRANSPORTING = "transporting"; // 运输中
    public static final String COMPLETED = "completed";     // 已完成

    public static String getStatusName(String status) {
        switch (status) {
            case PENDING: return "待分配";
            case TRANSPORTING: return "运输中";
            case COMPLETED: return "已完成";
            default: return "未知状态";
        }
    }
}