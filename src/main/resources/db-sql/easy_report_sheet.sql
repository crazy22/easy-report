/*
Navicat MySQL Data Transfer


Target Server Type    : MYSQL
Target Server Version : 50642
File Encoding         : 65001

Date: 2018-12-24 20:35:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for easy_report_sheet
-- ----------------------------
DROP TABLE IF EXISTS `easy_report_sheet`;
CREATE TABLE `easy_report_sheet` (
  `sheet_id` bigint(20) NOT NULL COMMENT 'sheet id 自增',
  `excel_id` bigint(20) NOT NULL COMMENT 'excel id',
  `sheet_name` varchar(50) NOT NULL COMMENT 'sheet名称',
  `sheet_num` int(3) NOT NULL COMMENT 'sheet编号',
  `head_row` int(2) NOT NULL DEFAULT '0' COMMENT '标题栏列数',
  `head_col` int(2) NOT NULL DEFAULT '0' COMMENT '标题栏行数',
  `back_color_head` int(11) NOT NULL DEFAULT '57' COMMENT '标题栏背景颜色',
  `word_color_head` int(11) NOT NULL DEFAULT '8'  COMMENT '标题栏字体颜色（备用）',
  `back_color_body` int(11) NOT NULL DEFAULT '9'  COMMENT '正文背景颜色',
  `word_color_body` int(11) NOT NULL DEFAULT '8'  COMMENT '正文字体颜色（备用）',
  `font_size_head` int(3) NOT NULL DEFAULT '18' COMMENT '标题栏字体大小',
  `font_size_body` int(3) NOT NULL DEFAULT '14' COMMENT '主体字体大小',
  `font_bold_head` int(1) NOT NULL DEFAULT '0' COMMENT '0-false,1-true',
  `font_bold_body` int(1) NOT NULL DEFAULT '0' COMMENT '0-false,1-true',
  `font_name_head` varchar(20) NOT NULL DEFAULT '楷体',
  `font_name_body` varchar(20) NOT NULL DEFAULT '黑体',
  PRIMARY KEY (`sheet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
