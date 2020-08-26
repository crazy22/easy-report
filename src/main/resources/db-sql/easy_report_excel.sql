/*
Navicat MySQL Data Transfer


Target Server Type    : MYSQL
Target Server Version : 50642
File Encoding         : 65001

Date: 2018-12-24 20:35:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for easy_report_excel
-- ----------------------------
DROP TABLE IF EXISTS `easy_report_excel`;
CREATE TABLE `easy_report_excel` (
  `excel_id` bigint(20) unsigned zerofill NOT NULL AUTO_INCREMENT COMMENT '报表id',
  `excel_name` varchar(50) NOT NULL COMMENT '报表名称',
  `select_sql` text NOT NULL COMMENT '数据查询sql',
  `create_time` int(10) NOT NULL COMMENT '创建时间',
  `update_time` int(10) NOT NULL COMMENT '更新时间',
  `update` int(1) NOT NULL DEFAULT '0' COMMENT '是否更新',
  PRIMARY KEY (`excel_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
