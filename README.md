# PigTransport — 生猪调运管理系统

> 基于 Spring Boot + MyBatis-Plus 的生猪运输全流程管理平台，涵盖批次调度、车辆管理、消毒记录、任务追踪等功能。

[![Java](https://img.shields.io/badge/Java-17+-blue)](https://www.java.com) [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)](https://spring.io/projects/spring-boot) [![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.x-orange)](https://baomidou.com) [![Layui](https://img.shields.io/badge/Layui-2.x-1E9FFF)](https://layui.dev) [![License](https://img.shields.io/badge/License-MIT-yellow)](./LICENSE)

## 项目简介

**PigTransport** 面向生猪调运行业，实现运输批次、车辆调度、消毒检疫、任务分配的全流程数字化管理。采用经典 MVC 分层架构，后端 Spring Boot + MyBatis-Plus，前端 Layui。

## 核心功能

| 模块 | 功能描述 |
|------|---------|
| 批次管理 | 创建运输批次、设置起止地点、跟踪批次状态 |
| 车辆管理 | 车辆信息登记、状态维护（空闲/运输中/维修） |
| 任务管理 | 运输任务分配、司机绑定、任务进度跟踪 |
| 消毒管理 | 消毒记录登记、消毒结果记录 |
| 地点管理 | 起运地/目的地信息维护 |
| 数据看板 | Dashboard 首页展示关键统计指标 |

## 技术栈

| 层 | 技术 |
|------|------|
| 后端框架 | Spring Boot 3.x |
| ORM | MyBatis-Plus |
| 前端 | Layui |
| 数据库 | MySQL |
| 构建工具 | Maven |

## 项目结构

```
src/main/java/com/pigtransport/
├── common/                    # 通用工具与常量
│   ├── constants/             # 状态枚举（任务、车辆、消毒、用户角色等）
│   ├── BaseEntity.java        # 实体基类
│   ├── LayuiTableResult.java  # Layui 表格统一返回格式
│   └── Result.java            # 全局统一响应体
├── config/                    # 配置类（MyBatis-Plus、Web）
├── controller/                # 接口层
│   ├── BatchController.java   # 批次管理
│   ├── VehicleController.java # 车辆管理
│   ├── TaskController.java    # 运输任务管理
│   ├── DisinfectionController.java # 消毒管理
│   ├── UserController.java    # 用户管理
│   └── DashboardController.java   # 数据看板
├── entity/                    # 数据实体（Batch/Vehicle/Task/Disinfection/User/Location）
├── mapper/                    # MyBatis Mapper 接口
└── service/                   # 业务接口与实现（impl/）
```

## 快速开始

```bash
# 1. 导入数据库
mysql -u root -p < sql/init.sql

# 2. 修改数据库配置
# 编辑 src/main/resources/application.yml

# 3. 启动项目
mvn spring-boot:run

# 4. 访问系统
# 浏览器打开 http://localhost:8080
```

## License

MIT
