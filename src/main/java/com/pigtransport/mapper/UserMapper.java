package com.pigtransport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pigtransport.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM sys_user WHERE username = #{username} AND deleted = 0")
    User selectByUsername(@Param("username") String username);

    /**
     * 根据用户名和密码查询用户（登录用）
     */
    @Select("SELECT * FROM sys_user WHERE username = #{username} AND password = #{password} AND deleted = 0")
    User selectByUsernameAndPassword(@Param("username") String username,
                                     @Param("password") String password);

    /**
     * 根据角色查询用户列表
     */
    @Select("SELECT * FROM sys_user WHERE role = #{role} AND deleted = 0 ORDER BY create_time DESC")
    List<User> selectByRole(@Param("role") String role);

    /**
     * 查询所有司机（用于下拉框）
     */
    @Select("SELECT user_id, real_name, phone FROM sys_user WHERE role = 'driver' AND deleted = 0")
    List<User> selectAllDrivers();

    /**
     * 查询所有未删除的用户（测试用）
     */
    @Select("SELECT COUNT(*) FROM sys_user WHERE deleted = 0")
    Integer selectActiveUserCount();
}