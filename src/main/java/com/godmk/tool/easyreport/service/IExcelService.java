package com.godmk.tool.easyreport.service;

import java.util.Map;

public interface IExcelService {

    /**
     * 获取excel样式数据
     *
     * @param queryParam
     * @return
     */
    Map getExcelModel(Map queryParam);

    /**
     * 获取报表数据
     *
     * @param queryParam
     * @return
     */
    Map getData(Map queryParam);

    /**
     * 保存excel模板信息
     *
     * @param parm
     * @return
     */
    Map createExcelModelData(Map parm);

    /**
     * 检查excel模板是否存在
     *
     * @param con
     * @return
     */
    Map checkExcelModel(Map con);

    /**
     * 修改excel模板信息
     *
     * @param parm
     * @return
     */
    Map modifyExcelModelData(Map parm);

    /**
     * 删除模型
     *
     * @param con
     * @return
     */
    Map deleteModel(Map con);
}
