# 🐖 生猪运输管理系统

基于 Spring Boot + MyBatis-Plus 开发，遵循 MVC 分层设计的生猪运输管理系统。

## 📋 项目架构

- **后端框架**：Spring Boot
- **ORM框架**：MyBatis-Plus
- **前端框架**：Layui
- **数据库**：MySQL
- **构建工具**：Maven

## 📁 核心代码目录

```
src/main/java/com/pigtransport/
├── common/                    # 通用工具与常量
│   ├── constants/             # 系统枚举/常量定义（任务状态、用户角色等）
│   ├── BaseEntity.java        # 实体基类（封装公共字段）
│   ├── LayuiTableResult.java  # Layui表格统一返回格式
│   └── Result.java            # 全局统一响应结果
├── config/                    # 配置类
│   ├── MybatisPlusConfig.java # MyBatis-Plus配置
│   └── WebConfig.java         # Web相关配置
├── controller/                # 控制器层（处理HTTP请求）
│   ├── BatchController.java     # 批次管理
│   ├── DisinfectionController.java # 消毒管理
│   ├── TaskController.java      # 运输任务管理
│   ├── UserController.java      # 用户管理
│   └── VehicleController.java   # 车辆管理
├── entity/                    # 实体类（与数据库表映射）
│   ├── Batch.java
│   ├── Disinfection.java
│   ├── Task.java
│   ├── User.java
│   └── Vehicle.java
├── mapper/                    # Mapper层（数据库访问接口）
│   ├── BatchMapper.java
│   ├── DisinfectionMapper.java
│   ├── TaskMapper.java
│   ├── UserMapper.java
│   └── VehicleMapper.java
└── service/                   # 业务逻辑层
    ├── impl/                  # 业务逻辑实现类
    │   ├── BatchServiceImpl.java
    │   └── DisinfectionServiceImpl.java
    ├── BatchService.java
    └── DisinfectionService.java
```

## 🎨 前端与资源目录

```
src/main/resources/
├── static/                    # 静态资源（HTML/CSS/JS/图片）
│   ├── disinfection/          # 消毒模块前端页面
│   ├── layui/                 # Layui前端框架
│   ├── task/                  # 任务模块前端页面
│   ├── user/                  # 用户模块前端页面
│   ├── vehicle/               # 车辆模块前端页面
│   └── index.html             # 系统首页
├── templates/                 # 模板文件（模板引擎使用）
├── application.yml            # 主配置文件
└── application.properties     # 备用配置文件
```

## 📦 其他重要目录

```
├── sql/                       # 数据库初始化脚本
│   └── init.sql
├── img/                       # 项目文档图片资源
│   └── disinfection/
├── logs/                      # 日志输出目录
├── upload/                    # 文件上传目录
│   └── images/
├── target/                    # Maven构建输出目录
├── pom.xml                    # Maven依赖配置
└── README.md                  # 项目说明文档
```

## ⚙️ 核心模块说明

| 模块 | 功能描述 |
|------|---------|
| **用户模块** | 用户信息管理、登录认证 |
| **车辆模块** | 运输车辆信息维护 |
| **任务模块** | 生猪运输任务的创建、状态管理 |
| **消毒模块** | 运输前后的消毒记录管理 |
| **批次模块** | 生猪批次信息管理 |

## 🚀 快速开始

1. **导入数据库**
   ```bash
   mysql -u root -p < sql/init.sql
   ```

2. **修改配置文件**
   编辑 `src/main/resources/application.yml`，配置数据库连接信息

3. **运行项目**
   ```bash
   mvn spring-boot:run
   ```

4. **访问系统**
   打开浏览器访问 `http://localhost:8080`
