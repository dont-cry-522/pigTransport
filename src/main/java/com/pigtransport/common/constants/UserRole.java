package com.pigtransport.common.constants;

/**
 * 用户角色常量
 */
public class UserRole {

    // 养殖户
    public static final String FARMER = "farmer";

    // 物流管理员
    public static final String ADMIN = "admin";

    // 司机
    public static final String DRIVER = "driver";

    // 获取角色显示名称
    public static String getRoleName(String role) {
        switch (role) {
            case FARMER:
                return "养殖户";
            case ADMIN:
                return "管理员";
            case DRIVER:
                return "司机";
            default:
                return "未知角色";
        }
    }

    // 获取所有角色数组（用于下拉框）
    public static String[] getAllRoles() {
        return new String[]{FARMER, ADMIN, DRIVER};
    }
}