/*
Navicat MySQL Data Transfer


Target Server Type    : MYSQL
Target Server Version : 50642
File Encoding         : 65001

Date: 2018-12-24 20:35:35
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for easy_report_head
-- ----------------------------
DROP TABLE IF EXISTS `easy_report_head`;
CREATE TABLE `easy_report_head` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `excel_id` bigint(20) NOT NULL COMMENT 'excel id',
  `sheet_num` int(3) NOT NULL COMMENT 'sheet编号',
  `title_name` varchar(50) NOT NULL COMMENT '标题名称',
  `col` int(10) NOT NULL COMMENT '列坐标',
  `row` int(10) NOT NULL COMMENT '行坐标',
  `color_back` int(3) NOT NULL DEFAULT '1' COMMENT 'poi IndexedColors index',
  `color_word` int(3) NOT NULL DEFAULT '0' COMMENT 'poi IndexedColors index',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;
