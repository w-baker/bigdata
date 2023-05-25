/*
 Navicat Premium Data Transfer

 Source Server         : ydles
 Source Server Type    : MySQL
 Source Server Version : 50709
 Source Host           : node01:3306
 Source Schema         : db_tpcc

 Target Server Type    : MySQL
 Target Server Version : 50709
 File Encoding         : 65001

 Date: 25/05/2023 11:29:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for userinfo
-- ----------------------------
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo`  (
  `userid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`userid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of userinfo
-- ----------------------------
INSERT INTO `userinfo` VALUES (1, 'admin', 'admin', 'rain@qq.com');

SET FOREIGN_KEY_CHECKS = 1;
