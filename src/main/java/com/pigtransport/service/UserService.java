    package com.pigtransport.service;


    import com.baomidou.mybatisplus.extension.service.IService;
    import com.pigtransport.entity.User;

    import java.util.List;
    import java.util.Map;

    /**
     * 用户服务接口
     */
    public interface UserService extends IService<User> {

        /**
         * 用户登录
         * @param username 用户名
         * @param password 密码
         * @return 登录成功的用户信息
         */
        User login(String username, String password);

        /**
         * 根据角色获取用户列表
         * @param role 角色
         * @return 用户列表
         */
        List<User> getUsersByRole(String role);

        /**
         * 添加用户
         * @param user 用户信息
         * @return 是否成功
         */
        boolean addUser(User user);

        /**
         * 更新用户信息
         * @param user 用户信息
         * @return 是否成功
         */
        boolean updateUser(User user);

        /**
         * 删除用户（逻辑删除）
         * @param userId 用户ID
         * @return 是否成功
         */
        boolean deleteUser(Integer userId);

        /**
         * 获取所有司机（用于下拉框）
         * @return 司机列表
         */
        List<Map<String, Object>> getDriverList();

        /**
         * 获取用户统计信息
         * @return 各角色用户数量
         */
        Map<String, Long> getUserStatistics();
    }