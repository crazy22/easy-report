/*
Navicat MySQL Data Transfer


Target Server Type    : MYSQL
Target Server Version : 50642
File Encoding         : 65001

Date: 2018-12-24 20:34:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for easy_report_body
-- ----------------------------
DROP TABLE IF EXISTS `easy_report_body`;
CREATE TABLE `easy_report_body` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `excel_id` bigint(20) NOT NULL COMMENT 'excel id',
  `sheet_num` int(3) NOT NULL COMMENT 'sheet编号',
  `coordinate_col` int(11) NOT NULL DEFAULT '-1' COMMENT '列坐标(通配-1)',
  `coordinate_row` int(11) NOT NULL DEFAULT '-1' COMMENT '行坐标(通配-1)',
  `value_key` varchar(50) NOT NULL DEFAULT '' COMMENT '结果集key字段',
  `value_type` int(2) NOT NULL DEFAULT '0' COMMENT '值类型',
  `float` int(2) NOT NULL DEFAULT '0' COMMENT '0,-1,1居中，左对齐，右对齐',
  `color_back` int(3) NOT NULL DEFAULT '9' COMMENT '背景色',
  `color_word` int(3) NOT NULL DEFAULT '8' COMMENT '字体颜色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
