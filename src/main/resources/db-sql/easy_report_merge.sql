/*
Navicat MySQL Data Transfer


Target Server Type    : MYSQL
Target Server Version : 50642
File Encoding         : 65001

Date: 2018-12-24 20:35:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for easy_report_merge
-- ----------------------------
DROP TABLE IF EXISTS `easy_report_merge`;
CREATE TABLE `easy_report_merge` (
  `id` bigint(20) NOT NULL COMMENT '自增id',
  `excel_id` bigint(20) NOT NULL COMMENT 'excel id',
  `sheet_num` int(3) NOT NULL COMMENT 'sheet编号',
  `start_col` int(11) NOT NULL COMMENT '合并列开始坐标',
  `start_row` int(11) NOT NULL  COMMENT '合并行开始坐标',
  `end_col` int(11) NOT NULL  COMMENT '合并列结束坐标',
  `end_row` int(11) NOT NULL COMMENT '合并行结束坐标',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
