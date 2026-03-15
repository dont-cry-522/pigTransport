package com.pigtransport.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pigtransport.entity.User;
import com.pigtransport.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.pigtransport.service.UserService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户服务实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    @Transactional(readOnly = true)
    public User login(String username, String password) {
        // 参数校验
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new RuntimeException("用户名和密码不能为空");
        }

        // 查询用户
        User user = baseMapper.selectByUsernameAndPassword(username, password);
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        return user;
    }

    @Override
    public List<User> getUsersByRole(String role) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("role", role)
                .eq("deleted", 0)
                .orderByDesc("create_time");
        return baseMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUser(User user) {
        // 1. 参数校验
        if (user == null) {
            throw new RuntimeException("用户信息不能为空");
        }
        if (!StringUtils.hasText(user.getUsername())) {
            throw new RuntimeException("用户名不能为空");
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new RuntimeException("密码不能为空");
        }
        if (!StringUtils.hasText(user.getRole())) {
            throw new RuntimeException("角色不能为空");
        }

        // 2. 检查用户名是否已存在
        User existUser = baseMapper.selectByUsername(user.getUsername());
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 3. 设置默认值
        user.setCreateTime(new Date());
        user.setDeleted(0);

        // 4. 保存到数据库
        int result = baseMapper.insert(user);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(User user) {
        // 参数校验
        if (user == null || user.getUserId() == null) {
            throw new RuntimeException("用户信息不完整");
        }

        // 检查用户是否存在
        User existUser = baseMapper.selectById(user.getUserId());
        if (existUser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 更新用户信息（不更新密码，密码单独修改）
        if (StringUtils.hasText(user.getRealName())) {
            existUser.setRealName(user.getRealName());
        }
        if (StringUtils.hasText(user.getPhone())) {
            existUser.setPhone(user.getPhone());
        }
        if (StringUtils.hasText(user.getRole())) {
            existUser.setRole(user.getRole());
        }

        int result = baseMapper.updateById(existUser);
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Integer userId) {
        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }

        // ✅ 关键修复：使用MyBatis-Plus的删除方法，它会自动处理逻辑删除
        // 根据配置文件，会自动设置 deleted = 1
        return baseMapper.deleteById(userId) > 0;

    }
    @Override
    public List<Map<String, Object>> getDriverList() {
        List<User> drivers = baseMapper.selectAllDrivers();
        return drivers.stream().map(driver -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", driver.getUserId());
            map.put("name", StringUtils.hasText(driver.getRealName()) ?
                    driver.getRealName() : driver.getUsername());
            map.put("phone", driver.getPhone());
            return map;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> getUserStatistics() {
        Map<String, Long> statistics = new HashMap<>();

        // 查询各角色用户数量
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0);

        // 总用户数
        Long total = baseMapper.selectCount(wrapper);
        statistics.put("total", total);

        // 各角色数量
        String[] roles = {"operator", "admin", "driver"};
        for (String role : roles) {
            QueryWrapper<User> roleWrapper = new QueryWrapper<>();
            roleWrapper.eq("role", role).eq("deleted", 0);
            Long count = baseMapper.selectCount(roleWrapper);
            statistics.put(role, count);
        }

        return statistics;
    }
}