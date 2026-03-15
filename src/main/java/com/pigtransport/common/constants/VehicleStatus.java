package com.pigtransport.common.constants;

public class VehicleStatus {
    public static final String FREE = "free";           // 空闲
    public static final String TRANSPORTING = "transporting"; // 运输中
    public static final String MAINTENANCE = "maintenance";   // 维修

    public static String getStatusName(String status) {
        switch (status) {
            case FREE: return "空闲";
            case TRANSPORTING: return "运输中";
            case MAINTENANCE: return "维修中";
            default: return "未知状态";
        }
    }
}