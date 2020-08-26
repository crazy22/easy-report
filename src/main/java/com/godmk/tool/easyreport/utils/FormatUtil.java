package com.godmk.tool.easyreport.utils;

import com.godmk.tool.easyreport.constant.DataTypeEnum;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class FormatUtil {

    /**
     * 格式化数值类型数据
     *
     * @param number
     * @param dataType
     * @return
     */
    public static String formatNumber(double number, DataTypeEnum dataType) {
        String formatPattern = dataType.formatPattern;
        if (formatPattern == null || "".equals(formatPattern)) {
            return "";
        }
        DecimalFormat df = new DecimalFormat(formatPattern);
        String xs = df.format(number);
        return xs;
    }

    /**
     * 格式化时间类型数据
     *
     * @param date
     * @param dateType
     * @return
     */
    public static String formatDate(Date date, DataTypeEnum dateType) {
        String formatPattern = dateType.formatPattern;
        if (formatPattern == null || "".equals(formatPattern)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
        return sdf.format(date);
    }

    /**
     * 将时间戳转化为时间
     *
     * @param timeStamp
     * @param dateType
     * @return
     */
    public static String stampToDate(Integer timeStamp, DataTypeEnum dateType) {
        SimpleDateFormat format = null;
        Long time = 1000L;
        String formatPattern = dateType.formatPattern;
        if (formatPattern == null || "".equals(formatPattern)) {
            return "";
        }
        format = new SimpleDateFormat(formatPattern);
        String d;
        if (timeStamp == 0) {
            d = "";
        } else {
            d = format.format(timeStamp * time);
        }
        return d;
    }

    /**
     * 日期转为时间戳
     *
     * @param time
     * @return
     */
    public static String dateToStamp(String time) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(date.getTime() / 1000);
    }
}
