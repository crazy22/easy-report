package com.godmk.tool.easyreport.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ParamHandler {

    /**
     * 获取head参数
     * @param request
     * @return
     */
    public static Map<String, Object> headParam(HttpServletRequest request){
        String info = request.getParameter("info");
        if (null == info) {
            return new HashMap<String, Object>() ;
        }else {
            Map param = JsonMsg.stringToMap(info);
            Map<String, Object> head = (Map) param.get("head");
            return head;
        }
    }

    /**
     * 获取body参数
     * @param request
     * @return
     */
    public static Map<String, Object> bodyParam(HttpServletRequest request){
        String info = request.getParameter("info");
        if (null == info) {
            return new HashMap<String, Object>() ;
        }else {
            Map param = JsonMsg.stringToMap(info);
            Map<String, Object> body = (Map) param.get("body");
            return body;
        }
    }

    /**
     * 页数计算
     * @param size
     * @param rows
     * @return
     */
    public static int totalPages(int size,int rows){
        if (size <=0) {
            size = 5 ; //默认5条
        }
        int y = rows % size ;
        if (y > 0) {
            return rows / size + 1 ;
        }else {
            return rows / size ;
        }
    }

    /**
     * 分页参数处理
     * @param map
     * @return
     */
    public static Map<String, Object> fy(Map<String, Object> map) {
        String page = map.get("page") + "" ; //处理分页参数
        String size = map.get("size") + "" ;  //处理分页参数
        try {
            int size_i = Integer.parseInt(size) ;
            if (size_i < 1) {
                map.put("size", 5) ;
            }else {
                map.put("size", size_i) ;
            }
        }catch (Exception e){
            map.put("size", 5) ;  //默认5条
        }
        try {
            int page_i = Integer.parseInt(page) ;
            if (page_i < 1) {
                map.put("page", 1) ;
                map.put("start", 0) ;
            }else {
                map.put("page", page_i) ;
                map.put("start", (page_i - 1)*((Integer)map.get("size"))) ;
            }
        }catch (Exception e){
            map.put("page", 1) ; //默认第一页
            map.put("start", 0) ; //默认起始位置0
        }
        return map ;
    }

    //分页返回值处理
    public static Map<String, Object> fyResult(List<?> resultList,Map requestParam, int totalRows) {
        Map<String, Object> resultMap = new HashMap<String, Object>() ;
        int page = 1;
        int size = 5;
        try {
            page = (Integer) requestParam.get("page") ;
            size = (Integer) requestParam.get("size") ;
        }catch (Exception e) {
            resultMap.put("resultList", resultList) ;
        }
        int totalPages = totalPages(size,totalRows) ;
        if (totalPages > page) {
            resultMap.put("next_page", page + 1) ;
        }else {
            resultMap.put("next_page", totalPages) ;
        }
        resultMap.put("curr_page", page) ;
        resultMap.put("totalPages", totalPages) ;
        resultMap.put("resultList", resultList) ;
        return resultMap ;
    }

    /**
     * 判断一个对象是否为正整数
     * @param object
     * @return
     */
    public static Boolean isInt(Object object) {
        try {
            Integer str1 = Integer.parseInt(object.toString());
            if(str1 <= 0){
                return false;
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 判断一个对象是否是数组
     * @param object
     * @return
     */
    public static Boolean isArr(Object object){
        try {
            ArrayList arr = (ArrayList<String>)object;
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
