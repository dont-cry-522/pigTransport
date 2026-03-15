/*
 Navicat Premium Data Transfer

 Source Server         : tianshengqun
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : pig_transport

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 02/02/2026 16:30:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for disinfection_record
-- ----------------------------
DROP TABLE IF EXISTS `disinfection_record`;
CREATE TABLE `disinfection_record`  (
  `disinfection_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '消毒记录ID',
  `task_id` int(11) NULL DEFAULT NULL COMMENT '任务ID',
  `driver_id` int(11) NULL DEFAULT NULL COMMENT '司机ID',
  `car_id` int(11) NULL DEFAULT NULL COMMENT '车辆ID',
  `disinfection_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消毒类型（整车/车厢/轮胎/场地等）',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消毒位置',
  `disinfectant` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消毒剂名称',
  `disinfection_time` datetime NULL DEFAULT NULL COMMENT '消毒时间',
  `operator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人员',
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '备注信息',
  `result` enum('qualified','unqualified') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'qualified' COMMENT '消毒结果（合格/不合格）',
  `photo_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消毒照片URL',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除标志（0-正常 1-删除）',
  PRIMARY KEY (`disinfection_id`) USING BTREE,
  INDEX `idx_task_id`(`task_id`) USING BTREE,
  INDEX `idx_driver_id`(`driver_id`) USING BTREE,
  INDEX `idx_car_id`(`car_id`) USING BTREE,
  INDEX `idx_disinfection_time`(`disinfection_time`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE,
  INDEX `idx_result`(`result`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '车辆消毒记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of disinfection_record
-- ----------------------------
INSERT INTO `disinfection_record` VALUES (1, 1, 1, 1, '车辆消毒', '1', '1', '2026-01-31 16:20:26', NULL, NULL, 'qualified', NULL, '2026-01-31 16:20:26', 1);
INSERT INTO `disinfection_record` VALUES (2, 2, 2, 2, '车辆消毒', '2', '3', '2026-01-31 16:20:38', NULL, NULL, 'qualified', NULL, '2026-01-31 16:20:38', 1);
INSERT INTO `disinfection_record` VALUES (3, 3, 2, 2, '场地消毒', '2', '2', '2026-01-31 16:37:07', NULL, NULL, 'qualified', '/img/disinfection/76ad70f062a741f89b9c7b57277b7854.jpg', NULL, 1);
INSERT INTO `disinfection_record` VALUES (4, 3, 3, 3, '车辆消毒', '3', '3', '2026-01-31 17:41:30', 'system', NULL, 'qualified', '/img/disinfection/69ebb0ce54914c878bed1c465b1a479f.jpg', NULL, 0);
INSERT INTO `disinfection_record` VALUES (5, 1, 1, 1, '车辆消毒', '1', '1', '2026-02-01 17:59:44', 'system', NULL, 'qualified', '/img/disinfection/780120d7505349a197bbd837eb7af8b5.jpg', NULL, 0);

-- ----------------------------
-- Table structure for location_sign
-- ----------------------------
DROP TABLE IF EXISTS `location_sign`;
CREATE TABLE `location_sign`  (
  `sign_id` int(11) NOT NULL AUTO_INCREMENT,
  `task_id` int(11) NOT NULL COMMENT '任务ID',
  `driver_id` int(11) NOT NULL COMMENT '司机ID',
  `location` varchar(200) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '签到位置',
  `sign_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '备注信息',
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除标记',
  `latitude` decimal(10, 8) NULL DEFAULT NULL COMMENT '纬度',
  `longitude` decimal(11, 8) NULL DEFAULT NULL COMMENT '经度',
  PRIMARY KEY (`sign_id`) USING BTREE,
  INDEX `idx_task`(`task_id`) USING BTREE,
  INDEX `idx_driver`(`driver_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of location_sign
-- ----------------------------
INSERT INTO `location_sign` VALUES (1, 1, 4, '北京市朝阳区养殖场门口', '2026-01-25 23:59:34', '出发签到', 1, NULL, NULL);
INSERT INTO `location_sign` VALUES (2, 1, 4, '河北省石家庄市高速服务区', '2026-01-26 23:59:34', '中途休息', 1, NULL, NULL);
INSERT INTO `location_sign` VALUES (3, 1, 4, '天津市屠宰场门口', '2026-01-27 23:59:34', '到达签到', 1, NULL, NULL);
INSERT INTO `location_sign` VALUES (4, 2, 5, '山东省济南市养殖基地', '2026-01-27 23:59:34', '装货完成出发', 1, NULL, NULL);
INSERT INTO `location_sign` VALUES (5, 1, 1, '2', '2026-02-02 16:13:04', NULL, 1, 34.10707800, 114.16842500);

-- ----------------------------
-- Table structure for pig_batch
-- ----------------------------
DROP TABLE IF EXISTS `pig_batch`;
CREATE TABLE `pig_batch`  (
  `batch_id` int(11) NOT NULL AUTO_INCREMENT,
  `breeder_id` int(11) NOT NULL COMMENT '养殖户ID',
  `variety` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '品种',
  `source` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '未指定来源',
  `quantity` int(11) NOT NULL COMMENT '数量',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除标记',
  `status` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '状态',
  `batch_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '批次编号',
  `remark` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '备注',
  PRIMARY KEY (`batch_id`) USING BTREE,
  INDEX `idx_breeder`(`breeder_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pig_batch
-- ----------------------------
INSERT INTO `pig_batch` VALUES (1, 2, '杜洛克', '河北养殖场', 100, '2026-01-27 17:15:54', 0, NULL, NULL, NULL);
INSERT INTO `pig_batch` VALUES (2, 2, '长白猪', '山东养殖基地', 150, '2026-01-27 17:15:54', 0, NULL, NULL, NULL);
INSERT INTO `pig_batch` VALUES (3, 3, '约克夏', '河南养殖合作社', 80, '2026-01-27 17:15:54', 0, NULL, NULL, NULL);
INSERT INTO `pig_batch` VALUES (4, 2, '皮特兰', '江苏养殖场', 120, '2026-01-27 17:15:54', 1, NULL, NULL, NULL);
INSERT INTO `pig_batch` VALUES (5, 2, '长白猪', '未指定来源', 1, '2026-02-01 16:22:43', 1, '1', '1', '');
INSERT INTO `pig_batch` VALUES (6, 2, '大白猪', '2', 2, '2026-02-01 16:27:08', 0, 'available', '2', '');
INSERT INTO `pig_batch` VALUES (7, 2, '长白猪', '1', 11, '2026-02-01 18:09:54', 0, 'transported', '1', '');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `real_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `role` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '角色：admin(管理员)/operator(操作员)/driver(司机)',
  `status` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT '1' COMMENT '账号状态：1启用，0禁用',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '备注信息',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  INDEX `idx_username`(`username`) USING BTREE,
  INDEX `idx_role`(`role`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', NULL, '123456', 'admin', '1', NULL, '13800138000', '2026-01-27 15:07:15', 0);
INSERT INTO `sys_user` VALUES (2, 'farmer1', NULL, '123456', 'farmer', '1', NULL, '13900139001', '2026-01-27 15:07:15', 0);
INSERT INTO `sys_user` VALUES (3, 'farmer2', NULL, '123456', 'farmer', '1', NULL, '13900139002', '2026-01-27 15:07:15', 0);
INSERT INTO `sys_user` VALUES (4, 'driver1', NULL, '123456', 'driver', '1', NULL, '13700137001', '2026-01-27 15:07:15', 0);
INSERT INTO `sys_user` VALUES (5, 'driver2', NULL, '123456', 'driver', '1', NULL, '13700137002', '2026-01-27 15:07:15', 0);
INSERT INTO `sys_user` VALUES (6, 'driver3', NULL, '123456', 'driver', '1', NULL, '13700137003', '2026-01-27 15:07:15', 0);
INSERT INTO `sys_user` VALUES (7, 'testuser', NULL, '123456', 'driver', '1', NULL, '13888888888', '2026-01-27 16:08:36', 0);
INSERT INTO `sys_user` VALUES (8, '111', '111', '111', 'admin', '1', NULL, '1111', '2026-01-30 18:36:17', 0);
INSERT INTO `sys_user` VALUES (9, '222', '222', '222', 'admin', '1', NULL, '222', '2026-01-31 00:05:52', 0);
INSERT INTO `sys_user` VALUES (10, '333', '333', '333', 'driver', '1', NULL, '333', '2026-01-31 17:03:11', 0);
INSERT INTO `sys_user` VALUES (11, '444', '444', '444', 'admin', '1', NULL, '444', '2026-01-31 17:27:42', 0);
INSERT INTO `sys_user` VALUES (12, '555', '555', '555', 'admin', '1', NULL, '555', '2026-01-31 17:28:41', 0);
INSERT INTO `sys_user` VALUES (13, '666', '666', '666', 'admin', '1', NULL, '666', '2026-01-31 17:40:52', 0);
INSERT INTO `sys_user` VALUES (14, '1', '13', '1', 'admin', '1', '', '13', '2026-02-01 17:10:16', 0);
INSERT INTO `sys_user` VALUES (15, '1333', '22', '1', 'admin', '1', '', '22', '2026-02-01 17:59:18', 0);
INSERT INTO `sys_user` VALUES (16, '444444', '444', '444', 'admin', '1', '', '44444', '2026-02-01 18:09:41', 1);
INSERT INTO `sys_user` VALUES (17, '2222', '222', '2222', 'operator', '1', '', '22', '2026-02-02 15:57:27', 1);
INSERT INTO `sys_user` VALUES (18, '1111111', '1111', '111111', 'operator', '1', '', '1111', '2026-02-02 16:30:29', 1);

-- ----------------------------
-- Table structure for transport_task
-- ----------------------------
DROP TABLE IF EXISTS `transport_task`;
CREATE TABLE `transport_task`  (
  `task_id` int(11) NOT NULL AUTO_INCREMENT,
  `batch_id` int(11) NOT NULL COMMENT '生猪批次ID',
  `vehicle_id` int(11) NOT NULL COMMENT '车辆ID',
  `start_place` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `end_place` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `status` enum('pending','transporting','completed') CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT 'pending' COMMENT '待分配/运输中/已完成',
  `assign_time` datetime NULL DEFAULT NULL COMMENT '分配时间',
  `finish_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(4) NULL DEFAULT 0 COMMENT '逻辑删除标记',
  `task_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '任务编号',
  `driver_id` int(11) NULL DEFAULT NULL COMMENT '司机ID',
  `remark` text CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL COMMENT '备注',
  PRIMARY KEY (`task_id`) USING BTREE,
  INDEX `idx_batch`(`batch_id`) USING BTREE,
  INDEX `idx_vehicle`(`vehicle_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of transport_task
-- ----------------------------
INSERT INTO `transport_task` VALUES (1, 1, 2, '河北养殖场', '北京屠宰场', 'completed', '2026-01-27 17:32:31', NULL, '2026-01-27 17:32:31', 0, NULL, NULL, NULL);
INSERT INTO `transport_task` VALUES (2, 2, 1, '山东养殖基地', '天津加工厂', 'transporting', '2026-01-27 17:32:31', NULL, '2026-01-27 17:32:31', 0, NULL, NULL, NULL);
INSERT INTO `transport_task` VALUES (3, 3, 3, '河南养殖合作社', '河北批发市场', 'transporting', '2026-01-27 17:32:31', NULL, '2026-01-27 17:32:31', 0, NULL, NULL, NULL);
INSERT INTO `transport_task` VALUES (4, 1, 1, '1', '1', 'completed', NULL, NULL, '2026-02-01 17:40:51', 0, '1', 2, '');
INSERT INTO `transport_task` VALUES (5, 1, 2, '2', '2', 'transporting', NULL, NULL, '2026-02-01 17:45:57', 0, '1', 2, '');
INSERT INTO `transport_task` VALUES (6, 3, 3, '3', '3', 'transporting', NULL, NULL, '2026-02-01 18:10:16', 0, '3', 3, '3');

-- ----------------------------
-- Table structure for vehicle
-- ----------------------------
DROP TABLE IF EXISTS `vehicle`;
CREATE TABLE `vehicle`  (
  `vehicle_id` int(11) NOT NULL AUTO_INCREMENT,
  `license_plate` varchar(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '车牌号',
  `model` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '车型',
  `driver_id` int(11) NULL DEFAULT NULL COMMENT '当前司机ID',
  `driver_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '司机姓名',
  `status` enum('free','transporting','maintenance') CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT 'free' COMMENT '空闲/运输中/维修',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
  `vehicle_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '车辆类型（truck/van/refrigerated/other）',
  `capacity` double NULL DEFAULT NULL COMMENT '载重能力（吨）',
  `purchase_date` date NULL DEFAULT NULL COMMENT '购买日期',
  `mileage` int(11) NULL DEFAULT NULL COMMENT '行驶里程（km）',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`vehicle_id`) USING BTREE,
  UNIQUE INDEX `license_plate`(`license_plate`) USING BTREE,
  INDEX `idx_driver`(`driver_id`) USING BTREE,
  INDEX `idx_status`(`status`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of vehicle
-- ----------------------------
INSERT INTO `vehicle` VALUES (1, '京A12345', '重型货车', 4, NULL, 'free', '2026-01-27 16:18:15', 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `vehicle` VALUES (2, '京B67890', '中型货车', 5, NULL, 'transporting', '2026-01-27 16:18:15', 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `vehicle` VALUES (3, '京C24680', '冷藏车', NULL, NULL, 'maintenance', '2026-01-27 16:18:15', 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `vehicle` VALUES (4, '京D13579', '轻型货车', NULL, NULL, 'free', '2026-01-27 16:18:15', 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `vehicle` VALUES (5, '京E97531', '重型货车', NULL, NULL, 'free', '2026-01-27 16:18:15', 0, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `vehicle` VALUES (6, '1', '1', NULL, NULL, 'free', '2026-02-02 02:09:01', 1, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `vehicle` VALUES (7, 'q', '请求', NULL, NULL, 'free', '2026-02-02 02:32:21', 1, NULL, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
