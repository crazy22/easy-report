package com.godmk.tool.easyreport.constant;

public enum DataTypeEnum {
    STRING(0, ""),
    INT(1, ""),
    FLOAT(2, ""),
    DOUBLE(3, ""),
    DATE(4, "yyyy-MM-dd"),
    TIME_STAMP(5, "yyyy-MM-dd"),

    NUMBER(6, ""),
    //小数点后保留一位
    NUMBER_FORMAT_P1(7, "#,###.0"),
    //小数点后保留两位
    NUMBER_FORMAT_P2(8, "#,###.00"),
    //小数点后保留三位
    NUMBER_FORMAT_P3(9, "#,###.000"),
    //小数点后保留四位
    NUMBER_FORMAT_P4(10, "#,###.0000"),
    //百分表示
    NUMBER_PER(11, "#.##%"),
    //保留整数
    NUMBER_INT(12, "#,##0"),

    MONEY(13, "###,###,###,###,##0"),

    //时间格式化数据格式
    DATE_0(20, "yyyy-MM-dd HH:mm:ss"),
    DATE_1(21, "yyyy-MM-dd"),
    DATE_2(22, "yyyy/MM/dd"),
    DATE_3(23, "yyyyMMdd"),
    DATE_4(24, "HH:mm:ss"),
    DATE_5(25, "yyyy年MM月dd日"),
    DATE_6(26, "yyyyMMddHHmmss"),
    //时间戳格式化数据格式
    TIME_STAMP_0(30, "yyyy-MM-dd HH:mm:ss"),
    TIME_STAMP_1(31, "yyyy-MM-dd"),
    TIME_STAMP_2(32, "yyyy/MM/dd"),
    TIME_STAMP_3(33, "yyyyMMdd"),
    TIME_STAMP_4(34, "HH:mm:ss"),
    TIME_STAMP_5(35, "yyyy年MM月dd日"),
    TIME_STAMP_6(36, "yyyyMMddHHmmss"),


    TEXT(99, ""),
    ;

    public final int index;

    public final String formatPattern;

    DataTypeEnum(int index, String formatPattern) {
        this.index = index;
        this.formatPattern = formatPattern;
    }

    public int getIndex() {
        return this.index;
    }

    public String getFormatPattern() {
        return formatPattern;
    }

    public static DataTypeEnum valueOf(int index) {
        for (DataTypeEnum dataTypeEnum : DataTypeEnum.values()) {
            if (dataTypeEnum.getIndex() == index) {
                return dataTypeEnum;
            }
        }
        return null;
    }
}
