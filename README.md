# 🐖 生猪运输管理系统 - 目录结构说明

## 项目架构
基于 Spring Boot + MyBatis-Plus 开发，遵循 MVC 分层设计，目录结构如下：

## 核心代码目录   
src/main/java/com/pigtransport/
├── common/ # 通用工具与常量
│ ├── constants/ # 系统枚举 / 常量定义（任务状态、用户角色等）
│ ├── BaseEntity.java # 实体基类（封装公共字段）
│ ├── LayuiTableResult.java # Layui 表格统一返回格式
│ └── Result.java # 全局统一响应结果
├── config/ # 配置类
│ ├── MybatisPlusConfig.java # MyBatis-Plus 配置
│ └── WebConfig.java # Web 相关配置
├── controller/ # 控制器层（处理 HTTP 请求）
│ ├── BatchController.java # 批次管理
│ ├── DisinfectionController.java # 消毒管理
│ ├── TaskController.java # 运输任务管理
│ ├── UserController.java # 用户管理
│ └── VehicleController.java # 车辆管理
├── entity/ # 实体类（与数据库表映射）
│ ├── Batch.java
│ ├── Disinfection.java
│ ├── Task.java
│ ├── User.java
│ └── Vehicle.java
├── mapper/ # Mapper 层（数据库访问接口）
│ ├── BatchMapper.java
│ ├── DisinfectionMapper.java
│ ├── TaskMapper.java
│ ├── UserMapper.java
│ └── VehicleMapper.java
└── service/ # 业务逻辑层
├── impl/ # 业务逻辑实现类
│ ├── BatchServiceImpl.java
│ └── DisinfectionServiceImpl.java
├── BatchService.java
└── DisinfectionService.java  

## 前端与资源目录
src/main/resources/
├── static/ # 静态资源（HTML/CSS/JS/ 图片）
│ ├── disinfection/ # 消毒模块前端页面
│ ├── layui/ # Layui 前端框架
│ ├── task/ # 任务模块前端页面
│ ├── user/ # 用户模块前端页面
│ ├── vehicle/ # 车辆模块前端页面
│ └── index.html # 系统首页
├── templates/ # 模板文件（模板引擎使用）
├── application.yml # 主配置文件
└── application.properties # 备用配置文件

## 其他重要目录 
├── sql/ # 数据库初始化脚本
│ └── init.sql
├── img/ # 项目文档图片资源
│ └── disinfection/
├── logs/ # 日志输出目录
├── upload/ # 文件上传目录
│ └── images/
├── target/ # Maven 构建输出目录
├── pom.xml # Maven 依赖配置
└── README.md # 项目说明文档

## 核心模块说明
1. **用户模块**：用户信息管理、登录认证
2. **车辆模块**：运输车辆信息维护
3. **任务模块**：生猪运输任务的创建、状态管理
4. **消毒模块**：运输前后的消毒记录管理
5. **批次模块**：生猪批次信息管理