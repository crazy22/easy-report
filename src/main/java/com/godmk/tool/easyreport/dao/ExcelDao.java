package com.godmk.tool.easyreport.dao;


import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface ExcelDao {
    /**
     * 获取excel-master样式数据
     *
     * @param queryParam
     * @return
     */
    List<Map> getExcelModel(Map queryParam);

    /**
     * 获取excel-Sheet样式数据
     *
     * @param queryParam
     * @return
     */
    List<Map> getExcelSheet(Map queryParam);

    /**
     * 获取excel-Head样式数据
     *
     * @param queryParam
     * @return
     */
    List<Map> getExcelHead(Map queryParam);

    /**
     * 获取excel-Body样式数据
     *
     * @param queryParam
     * @return
     */
    List<Map> getExcelBody(Map queryParam);

    /**
     * 获取excel单元格合并信息
     *
     * @param queryParam
     * @return
     */
    List<Map> getExcelMerge(Map queryParam);

    /**
     * 获取报表数据
     *
     * @param queryParam
     * @return
     */
    List<Map> getData(Map queryParam);

    /**
     * 插入主表数据
     *
     * @param excel
     * @return
     */
    int insertExcel(Map excel);

    /**
     * 插入sheet数据
     *
     * @param parm
     * @return
     */
    int insertSheet(List parm);

    /**
     * 插入标题栏数据
     *
     * @param head
     * @return
     */
    int insertHead(List<Map> head);

    /**
     * 插入正文配置数据
     *
     * @param body
     * @return
     */
    int insertBody(List<Map> body);

    /**
     * 插入合并单元格数据
     *
     * @param merge
     * @return
     */
    int insertMerge(List<Map> merge);

    /**
     * 修改模板信息主表
     *
     * @param excel
     * @return
     */
    int modifyExcel(Map excel);


    /**
     * 根据 excel_id 删除模板信息
     *
     * @param excel
     * @return
     */
    int deleteExcel(Map excel);

    int deleteSheet(Map excel);

    int deleteHead(Map excel);

    int deleteBody(Map excel);

    int deleteMerge(Map excel);
}
