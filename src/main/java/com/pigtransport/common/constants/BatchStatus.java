package com.pigtransport.common.constants;

public class BatchStatus {
    public static final String AVAILABLE = "available";     // 可运输
    public static final String TRANSPORTED = "transported"; // 已运输

    public static String getStatusName(String status) {
        switch (status) {
            case AVAILABLE: return "可运输";
            case TRANSPORTED: return "已运输";
            default: return "未知状态";
        }
    }
}