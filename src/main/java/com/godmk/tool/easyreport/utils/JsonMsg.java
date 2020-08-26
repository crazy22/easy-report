package com.godmk.tool.easyreport.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

public abstract class JsonMsg {

    //请求数据数据转换Map
    public static Map stringToMap(String info) {
        //判断请求数据是否为json格式
        ObjectMapper mapper = new ObjectMapper(); //转换器
        Map param;
        try {
            param = mapper.readValue(info, Map.class); //json转换成map
        } catch (IOException e) {
            return new HashMap();
        }
        return param;
    }

    //请求数据数据转换List
    public static List stringToList(String info) {
        //判断请求数据是否为json格式
        ObjectMapper mapper = new ObjectMapper(); //转换器
        List param;
        try {
            param = mapper.readValue(info, List.class); //json转换成List
        } catch (IOException e) {
            return new ArrayList();
        }

        return param;

    }

    //对象转json字符串
    public static String objectToString(Object o) {
        //判断请求数据是否为json格式
        ObjectMapper mapper = new ObjectMapper(); //转换器
        String json = ""; //将对象转换成json
        try {
            json = mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;

    }

    //返回数据转换
    public static String returnMsg(String code, String msg, Object o) {

        Map buf = new HashMap();
        Map state = new HashMap();
        if(code.equals("0")){
            code="10000";
        }else {
            if (msg.contains("将字符串转换为 uniqueidentifier 时失败")) {
                msg = "参数异常" ;
            }
            if(msg.contains("SQLServerException")){
                msg = "SQL语句错误" ;
            }
        }
        state.put("code", code);
        state.put("msg", msg);

        buf.put("state", state);

        if (o == null) {
            buf.put("info", new LinkedList());
        } else {
            buf.put("info", o);
        }

        ObjectMapper mapper = new ObjectMapper();

        String json = null; //将对象转换成json
        try {
            json = mapper.writeValueAsString(buf);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;

    }
}
