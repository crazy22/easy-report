package com.godmk.tool.easyreport.utils;

/**
 * 字符串工具类
 */
public abstract class StringUtil {
    /**
     * 统计字符在字符串中的数量
     *
     * @param str
     * @param s
     * @return
     */
    public static int countOfChar(String str, char s) {
        int count = 0;
        if (str == null || "".equals(str)) {
            return count;
        }
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == 'j') {
                count++;
            }
        }
        return count;
    }

    /**
     * 字符为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(Object str) {
        if (str == null || "".equals(str.toString())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字符不为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(Object str) {
        if (str == null || "".equals(str.toString())) {
            return false;
        } else {
            return true;
        }
    }

}
