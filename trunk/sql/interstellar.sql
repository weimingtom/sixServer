/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50022
Source Host           : localhost:3306
Source Database       : interstellar

Target Server Type    : MYSQL
Target Server Version : 50022
File Encoding         : 65001

Date: 2016-08-01 03:16:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for loginout
-- ----------------------------
DROP TABLE IF EXISTS `loginout`;
CREATE TABLE `loginout` (
  `id` bigint(20) NOT NULL auto_increment,
  `createTime` datetime default NULL,
  `enter` bit(1) NOT NULL,
  `pid` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of loginout
-- ----------------------------
