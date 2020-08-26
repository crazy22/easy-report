package com.godmk.tool.easyreport.impl;

import com.godmk.tool.easyreport.constant.ContextConstant;
import com.godmk.tool.easyreport.constant.ExcelConstant;
import com.godmk.tool.easyreport.dao.ExcelDao;
import com.godmk.tool.easyreport.service.IExcelService;
import com.godmk.tool.easyreport.utils.JsonMsg;
import com.godmk.tool.easyreport.utils.MapUtil;
import com.godmk.tool.easyreport.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author crazy22
 */
@Service
public class ExcelServiceImpl implements IExcelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelServiceImpl.class);

    @Resource
    private ExcelDao excelDao;

    @Override
    public Map checkExcelModel(Map con) {
        Map<String, Object> resultDataMap = new HashMap<>();
        Object excel_id = con.get(ExcelConstant.EXCEL_ID);
        if (excel_id == null || "".equals(excel_id)) {
            resultDataMap.put(ContextConstant.SUCCESS, false);
            resultDataMap.put(ContextConstant.MSG, "excel_id参数为空");
            return resultDataMap;
        }
        List<Map> excelList = excelDao.getExcelModel(con);
        if (null == excelList || excelList.size() == 0) {
            resultDataMap.put(ContextConstant.SUCCESS, false);
            resultDataMap.put(ContextConstant.MSG, "报表模板不存在");
            return resultDataMap;
        }
        resultDataMap.put(ContextConstant.SUCCESS, true);
        resultDataMap.put(ContextConstant.MSG, "模板信息获取成功");
        resultDataMap.put(ContextConstant.RESULT, excelList);
        return resultDataMap;
    }

    @Override
    public Map getExcelModel(Map queryParam) {
        Map resultDataMap = new HashMap();
        Map result = new HashMap();
        Object excel_id = queryParam.get(ExcelConstant.EXCEL_ID);
        if (excel_id == null || "".equals(excel_id)) {
            resultDataMap.put(ContextConstant.SUCCESS, false);
            resultDataMap.put(ContextConstant.MSG, "excel_id参数为空");
            return resultDataMap;
        }
        List<Map> excelList = excelDao.getExcelModel(queryParam);
        if (excelList == null || excelList.size() == 0) {
            resultDataMap.put(ContextConstant.SUCCESS, false);
            resultDataMap.put(ContextConstant.MSG, "报表模板不存在,请先上传或编辑模板");
            return resultDataMap;
        }
        Map excelMap = excelList.get(0);
        String excel_name = excelMap.get(ExcelConstant.EXCEL_NAME) + "";
        result.put(ExcelConstant.EXCEL_NAME, excel_name);

        List<Map> sheetList = excelDao.getExcelSheet(queryParam);
        if (sheetList == null || sheetList.size() < 1) {
            resultDataMap.put(ContextConstant.SUCCESS, false);
            resultDataMap.put(ContextConstant.MSG, "报表模板异常，无sheet");
            return resultDataMap;
        }
        result.put(ExcelConstant.SHEET, sheetList);

        List<Map> headList = excelDao.getExcelHead(queryParam);
        if (headList == null || headList.size() < 1) {
            result.put(ExcelConstant.HEAD, false);
        } else {
            result.put(ExcelConstant.HEAD, true);
            Map sheetHeadMap = groupDataBySheet(headList);
            result.put(ExcelConstant.HEAD_MAP, sheetHeadMap);
        }

        List<Map> bodyList = excelDao.getExcelBody(queryParam);
        if (bodyList == null || bodyList.size() < 1) {
            result.put(ExcelConstant.BODY, false);
        } else {
            result.put(ExcelConstant.BODY, true);
            Map sheetBodyMap = groupDataBySheet(bodyList);
            result.put(ExcelConstant.BODY_MAP, sheetBodyMap);
        }

        List<Map> mergeList = excelDao.getExcelMerge(queryParam);
        if (mergeList == null || mergeList.size() < 1) {
            result.put(ExcelConstant.MERGE, false);
        } else {
            result.put(ExcelConstant.MERGE, true);
            Map sheetMergeMap = groupDataBySheet(mergeList);
            result.put(ExcelConstant.MERGE_MAP, sheetMergeMap);
        }

        resultDataMap.put(ContextConstant.SUCCESS, true);
        resultDataMap.put(ContextConstant.MSG, "模板信息获取成功");
        resultDataMap.put(ContextConstant.RESULT, result);
        return resultDataMap;
    }

    @Override
    public Map getData(Map queryParam) {
        Map resultDataMap = new HashMap();
        //1.获取查询语句
        Object excel_id = queryParam.get(ExcelConstant.EXCEL_ID);
        if (excel_id == null || "".equals(excel_id)) {
            resultDataMap.put(ContextConstant.SUCCESS, false);
            resultDataMap.put(ContextConstant.MSG, "excel_id参数为空");
            return resultDataMap;
        }
        List<Map> excelList = excelDao.getExcelModel(queryParam);
        if (excelList == null || excelList.size() == 0) {
            resultDataMap.put(ContextConstant.SUCCESS, false);
            resultDataMap.put(ContextConstant.MSG, "报表模板不存在");
            return resultDataMap;
        }
        Map excelMap = excelList.get(0);
        //String excel_name = excelMap.get(ExcelConstant.EXCEL_NAME)+"";
        Object select_sqls = excelMap.get(ExcelConstant.SELECT_SQL);
        if (select_sqls == null || "".equals(select_sqls)) {
            resultDataMap.put(ContextConstant.SUCCESS, false);
            resultDataMap.put(ContextConstant.MSG, "查询语句不存在");
            return resultDataMap;
        }
		/*if (select_sqls instanceof List){
			System.out.println(select_sqls);
		}*/
        //String s_sqls = (select_sqls+"").replaceAll("\\\\","");
        List sqls = JsonMsg.stringToList(select_sqls + "");
        if (sqls == null || sqls.size() == 0) {
            resultDataMap.put(ContextConstant.SUCCESS, false);
            resultDataMap.put(ContextConstant.MSG, "数据查询语句不存在");
            return resultDataMap;
        }
        String parms = queryParam.get(ContextConstant.PARMS) + "";
        Map parmsMap = JsonMsg.stringToMap(parms);
        Map data = new HashMap();
        for (int i = 0; i < sqls.size(); i++) {
            Map sqlMsg = JsonMsg.stringToMap(sqls.get(i).toString());
            String sql = sqlMsg.get(ContextConstant.SQL) + "";
            String sheet_num = ExcelConstant.SHEET_NUM + "_" + sqlMsg.get(ExcelConstant.SHEET_NUM);
            Object parameters = sqlMsg.get(ContextConstant.PARAMENTERS);
            int num_parm = StringUtil.countOfChar(sql, '?');
            List params = new ArrayList();
            if (parameters != null &&
                    !"".equals(parameters) &&
                    !"[]".equals(parameters) &&
                    !"[\"\"]".equals(parameters)) {
                params = (List) parameters;
            }
            if (num_parm > 0 && (params == null || params.size() == 0)) {
                resultDataMap.put(ContextConstant.SUCCESS, false);
                resultDataMap.put(ContextConstant.MSG, "参数有误！");
                return resultDataMap;
            }
            //2.拼接查询条件 sql不要太长。。。。。。
            for (int j = 0; j < params.size(); j++) {
                String parm = parmsMap.get(params.get(j) + "") + "";
                sql = sql.replaceFirst("[?]", "'" + parm + "'");
            }
            //System.out.println(sql);
            queryParam.put(ContextConstant.SQL, sql.toString());
            //3.执行查询
            List<Map> sheet_data = excelDao.getData(queryParam);
            data.put(sheet_num, sheet_data);
        }
        //queryParam.put("sql", select_sqls);
        //4.处理查询结果
        resultDataMap.put(ContextConstant.SUCCESS, true);
        resultDataMap.put(ContextConstant.MSG, "数据查询成功");
        resultDataMap.put(ContextConstant.RESULT, data);
        return resultDataMap;
    }

    @Override
    @Transactional
    public Map createExcelModelData(Map parm) {
        Map judge = MapUtil.judgeData(parm, new String[]{
                ContextConstant.EXCEL,
                ContextConstant.SHEET,
                ContextConstant.HEAD,
                ContextConstant.BODY,
                ContextConstant.MERGE});
        Map result = new HashMap();
        if (!Boolean.valueOf(judge.get(MapUtil.JUDGE_PASS).toString())) {
            result.put(ContextConstant.SUCCESS, judge.get(MapUtil.JUDGE_PASS));
            result.put(ContextConstant.MSG, "参数异常");
            return result;
        }
        Map excel = (Map) parm.get(ContextConstant.EXCEL);
        int insert_excel = excelDao.insertExcel(excel);
        if (insert_excel != 1) {
			/*result.put("success", false);
			result.put("msg", "数据插入失败");
			return result;*/
            throw new RuntimeException("数据插入失败");
        }
        //获取插入的excel_id
        String excel_id = excel.get(ExcelConstant.EXCEL_ID) + "";
        if (!writeData(parm, excel_id)) {
            throw new RuntimeException("数据写入失败");
        }
        result.put(ContextConstant.SUCCESS, true);
        result.put(ContextConstant.MSG, "数据模型写入成功");
        result.put(ContextConstant.RESULT, parm);
        return result;
    }

    @Override
    @Transactional
    public Map modifyExcelModelData(Map parm) {
        Map result = new HashMap();
        //检查模板是否存在
        Map excelCheckMap = checkExcelModel(parm);
        boolean modelHas = Boolean.valueOf(excelCheckMap.get(ContextConstant.SUCCESS).toString());
        if (!modelHas) {//不存在，返回校验信息
            result.put(ContextConstant.SUCCESS, false);
            result.put(ContextConstant.MSG, excelCheckMap.get(ContextConstant.MSG));
            return result;
        }
        Map judge = MapUtil.judgeData(parm, new String[]{
                ContextConstant.EXCEL,
                ContextConstant.SHEET,
                ContextConstant.HEAD,
                ContextConstant.BODY,
                ContextConstant.MERGE});
        if (!Boolean.valueOf(judge.get(MapUtil.JUDGE_PASS).toString())) {
            result.put(ContextConstant.SUCCESS, judge.get(MapUtil.JUDGE_PASS));
            result.put(ContextConstant.MSG, "参数异常");
            return result;
        }
        String excel_id = parm.get(ExcelConstant.EXCEL_ID) + "";
        Map excel = (Map) parm.get(ContextConstant.EXCEL);
        excel.put(ExcelConstant.EXCEL_ID, excel_id);
        int modify_excel = excelDao.modifyExcel(excel);
        if (modify_excel != 1) {
            throw new RuntimeException("数据修改失败");
        }

        Map model = getExcelModel(excel);
        boolean success = Boolean.valueOf(model.get(ContextConstant.SUCCESS).toString());
        if (!success) {
            result.put(ContextConstant.SUCCESS, false);
            result.put(ContextConstant.MSG, model.get(ContextConstant.MSG));
            return result;
        }
        //获取模型信息
        Map modelData = (Map) model.get(ContextConstant.RESULT);

        /**
         * 删除原有样式数据
         */
        int delete_sheet = excelDao.deleteSheet(excel);
        int delete_head = excelDao.deleteHead(excel);
        int delete_body = excelDao.deleteBody(excel);
        int delete_merge = excelDao.deleteMerge(excel);
        LOGGER.debug("delete_sheet:" + delete_sheet);
        LOGGER.debug("delete_head:" + delete_head);
        LOGGER.debug("delete_body:" + delete_body);
        LOGGER.debug("delete_merge:" + delete_merge);
        //删除成功记录删除的模型信息

        if (!writeData(parm, excel_id)) {
            throw new RuntimeException("数据写入失败");
        }

        LOGGER.debug(JsonMsg.objectToString("============================== 修改前的模型信息打印 开始 =============================="));
        LOGGER.debug(JsonMsg.objectToString(modelData));
        LOGGER.debug(JsonMsg.objectToString("============================== 修改前的模型信息打印 结束 =============================="));
        result.put(ContextConstant.SUCCESS, true);
        result.put(ContextConstant.MSG, "数据模型写入成功");
        result.put(ContextConstant.RESULT, parm);
        return result;
    }

    @Override
    @Transactional
    public Map deleteModel(Map con) {
        Map result = new HashMap();
        //检查模板是否存在
        Map excelCheckMap = checkExcelModel(con);
        boolean modelHas = Boolean.valueOf(excelCheckMap.get(ContextConstant.SUCCESS).toString());
        if (!modelHas) {//不存在，返回校验信息
            result.put(ContextConstant.SUCCESS, false);
            result.put(ContextConstant.MSG, excelCheckMap.get(ContextConstant.MSG));
            return result;
        }
        Map model = getExcelModel(con);
        boolean success = Boolean.valueOf(model.get(ContextConstant.SUCCESS).toString());
        if (!success) {
            result.put(ContextConstant.SUCCESS, false);
            result.put(ContextConstant.MSG, model.get(ContextConstant.MSG));
            return result;
        }
        //获取模型信息
        Map modelData = (Map) model.get(ContextConstant.RESULT);

        //删除模型信息
        excelDao.deleteExcel(con);
        excelDao.deleteSheet(con);
        excelDao.deleteHead(con);
        excelDao.deleteBody(con);
        excelDao.deleteMerge(con);

        //删除成功记录删除的模型信息
        LOGGER.debug(JsonMsg.objectToString("============================== 删除模型信息打印 开始 =============================="));
        LOGGER.debug(JsonMsg.objectToString(modelData));
        LOGGER.debug(JsonMsg.objectToString("============================== 删除模型信息打印 结束 =============================="));

        result.put(ContextConstant.SUCCESS, true);
        result.put(ContextConstant.MSG, "数据模型写入成功");
        result.put(ContextConstant.RESULT, modelData);
        return result;
    }

    /**
     * 写入配置信息
     *
     * @param parm
     * @param excel_id
     * @return
     */
    private boolean writeData(Map<String, List> parm, String excel_id) {
        //写入sheet配置数据
        List<Map> sheet = (List) parm.get(ContextConstant.SHEET);
        if (sheet == null || sheet.size() == 0) {
            return false;
        }
        sheet.forEach(n -> n.put(ExcelConstant.EXCEL_ID, excel_id));
        int insert_sheet = excelDao.insertSheet(sheet);
        if (insert_sheet <= 0) {
            return false;
        }
        //写入标题栏配置数据
        List<Map> head = (List) parm.get(ContextConstant.HEAD);
        if (head == null || head.size() == 0) {
            //return false;
        } else {
            head.forEach(n -> n.put(ExcelConstant.EXCEL_ID, excel_id));
            int insert_head = excelDao.insertHead(head);
            if (insert_head <= 0) {
                return false;
            }
        }
        //写入正文配置数据
        List<Map> body = (List) parm.get(ContextConstant.BODY);
        if (body == null || body.size() == 0) {
            return false;
        }
        body.forEach(n -> n.put(ExcelConstant.EXCEL_ID, excel_id));
        int insert_body = excelDao.insertBody(body);
        if (insert_body <= 0) {
            return false;
        }
        //写入合并单元格配置数据
        List<Map> merge = (List) parm.get(ContextConstant.MERGE);
        if (merge == null || merge.size() == 0) {
            //return false;
        } else {
            merge.forEach(n -> n.put(ExcelConstant.EXCEL_ID, excel_id));
            int insert_merge = excelDao.insertMerge(merge);
            if (insert_merge <= 0) {
                return false;
            }
        }
        return true;
    }


    /**
     * 根据 sheet_id 对数据分组
     *
     * @param dataList
     * @return
     */
    private Map groupDataBySheet(List<Map> dataList) {
        Map groupDataMap = new HashMap();
        for (int i = 0; i < dataList.size(); ) {
            List gList = new ArrayList();
            Map head = dataList.get(i);
            gList.add(head);
            int sheet_num = Integer.parseInt(head.get(ExcelConstant.SHEET_NUM).toString());
            for (int j = i + 1; j < dataList.size(); j++) {
                Map next_head = dataList.get(j);
                int next_sheet_num = Integer.parseInt(next_head.get(ExcelConstant.SHEET_NUM).toString());
                if (sheet_num == next_sheet_num) {
                    gList.add(next_head);
                    i = j;//下一个值与上一个相同，i++后指向下一个next_head
                } else {
                    i = j - 1;//下一个值与上一个相同，i++后指向当前next_head
                    break;
                }
            }
            groupDataMap.put(ExcelConstant.SHEET_NUM + "_" + sheet_num, gList);
            i++;
        }
        return groupDataMap;
    }


}
