package com.godmk.tool.easyreport.utils;

import java.util.HashMap;
import java.util.Map;

public abstract class MapUtil {

    /**
     * map 校验结果
     */
    public static final String JUDGE_PASS = "PASS";
    /**
     * map 校验结果返回信息
     */
    public static final String JUDGE_MSG = "MSG";

    /**
     * 校验map中的数据
     *
     * @param map
     * @param fields
     * @return
     */
    public static Map judgeData(Map map, String[] fields) {
        Map result = new HashMap();
        if (map == null || map.size() == 0) {
            result.put(JUDGE_PASS, false);
            result.put(JUDGE_MSG, "待校验数据为空");
        }
        if (fields == null || fields.length == 0) {
            result.put(JUDGE_PASS, false);
            result.put(JUDGE_MSG, "校验字段为空");
        }
        for (String field : fields) {
            boolean ck = map.containsKey(field);
            if (!ck) {
                result.put(JUDGE_PASS, false);
                result.put(JUDGE_MSG, field + "字段不存在");
                return result;
            }
            Object value = map.get(field);
            if (value == null || "".equals(value)) {
                result.put(JUDGE_PASS, false);
                result.put(JUDGE_MSG, field + "字段的值为空");
                return result;
            }
        }
        result.put(JUDGE_PASS, true);
        result.put(JUDGE_MSG, "数据正常");
        return result;
    }

}
