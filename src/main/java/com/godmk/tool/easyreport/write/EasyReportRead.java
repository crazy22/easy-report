package com.godmk.tool.easyreport.write;

import com.godmk.tool.easyreport.constant.ContextConstant;
import com.godmk.tool.easyreport.constant.ExcelConstant;
import com.godmk.tool.easyreport.utils.JsonMsg;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读取报表数据，写入数据库
 */
public class EasyReportRead {

    private static final Logger LOGGER = LoggerFactory.getLogger(EasyReportRead.class);

    /**
     * 报表模板解析
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Map readExcelModel(MultipartFile file) throws IOException {
        Map result = new HashMap();
        if (file.isEmpty()) {
            result.put(ContextConstant.SUCCESS, false);
            result.put(ContextConstant.RETURN_CODE, ContextConstant.FAIL_CODE_40000);
            result.put(ContextConstant.MSG, "文件为空");
            return result;
        }
        String fileName = file.getOriginalFilename();  //获取文件名
        String suffixName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();  // 获取文件的后缀名
        if (!(suffixName.equals(".xlsx"))) {
            result.put(ContextConstant.SUCCESS, false);
            result.put(ContextConstant.RETURN_CODE, ContextConstant.FAIL_CODE_40000);
            result.put(ContextConstant.MSG, "请上传xlsx格式excel模板");
            return result;
            //return JsonMsg.returnMsg(ContextConstant.FAIL_CODE_40000,"", null);
        }
        Map excelData = new HashMap();
        excelData.put(ExcelConstant.EXCEL_NAME, fileName);
        Long time = System.currentTimeMillis() / 1000;
        excelData.put(ExcelConstant.CREATE_TIME, time);
        excelData.put(ExcelConstant.UPDAYE_TIME, time);
        excelData.put(ExcelConstant.UPDAYE, 1);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(file.getInputStream());
        int sheet_num = xssfWorkbook.getNumberOfSheets();
        List<Map> headData = new ArrayList<>();
        List<Map> bodyData = new ArrayList<>();
        List<Map> sheetData = new ArrayList<>();
        List<Map> mergeData = new ArrayList<>();
        List sql = new ArrayList();
        for (int i = 0; i < sheet_num; i++) {
            XSSFSheet sheet = xssfWorkbook.getSheetAt(i);
            int lastRowNum = sheet.getLastRowNum();
            if (lastRowNum > 30) {
                xssfWorkbook.close();
                result.put(ContextConstant.SUCCESS, false);
                result.put(ContextConstant.RETURN_CODE, ContextConstant.FAIL_CODE_40000);
                result.put(ContextConstant.MSG, "模板数据量过大");
                return result;
                //return JsonMsg.returnMsg(ContextConstant.FAIL_CODE_40000,"模板数据量过大", null);
            }
            Map sheetDataMap = new HashMap();
            boolean head_color_default = true;
            boolean body_color_default = true;
            int back_color_head = 57;
            int back_color_body = 9;
            //字体是否默认
            boolean head_font_default = true;
            boolean body_font_default = true;
            int font_size_head = 18;
            int font_size_body = 14;
            int font_bold_head = 0;
            int font_bold_body = 0;
            String font_name_head = "楷体";
            String font_name_body = "黑体";

            sheetDataMap.put(ExcelConstant.SHEET_NAME, sheet.getSheetName());
            sheetDataMap.put(ExcelConstant.SHEET_NUM, i + 1);
            int head_row = 0;
            int head_col = 0;

            for (int r = 0; r < lastRowNum + 1; r++) {
                XSSFRow xssfRowData = sheet.getRow(r);
                if (xssfRowData == null) {
                    continue;
                }
                int lastCellNum = xssfRowData.getLastCellNum();
                if (head_col == 0) {
                    head_col = lastCellNum;
                }
                if (lastCellNum > 30) {
                    result.put(ContextConstant.SUCCESS, false);
                    result.put(ContextConstant.RETURN_CODE, ContextConstant.FAIL_CODE_40000);
                    result.put(ContextConstant.MSG, "模板数据量过大");
                    return result;
                    //return JsonMsg.returnMsg(ContextConstant.FAIL_CODE_40000,"模板数据量过大", null);
                }
                //获取表格标题、数据信息、背景色信息
                boolean is_h = false;
                for (int c = 0; c < lastCellNum; c++) {
                    XSSFCell xssfCell = xssfRowData.getCell(c);
                    if (xssfCell == null) {
                        continue;
                    }
                    String value = xssfCell.getStringCellValue();
                    boolean is_head = value.startsWith(ContextConstant.HEAD_SET);
                    boolean is_body = value.startsWith(ContextConstant.BODY_SET);
                    boolean is_sql = value.startsWith(ContextConstant.SQL_SET);
                    if (is_head) {
                        String[] headMsgArr = value.split(":");
                        if (headMsgArr == null || headMsgArr.length != 2) {
                            xssfWorkbook.close();
                            result.put(ContextConstant.SUCCESS, false);
                            result.put(ContextConstant.RETURN_CODE, ContextConstant.FAIL_CODE_40000);
                            result.put(ContextConstant.MSG, "模板有误");
                            return result;
                            //	return JsonMsg.returnMsg(ContextConstant.FAIL_CODE_40000,"模板有误", null);
                        }
                        if (head_color_default) {
                            XSSFCellStyle cStyle = xssfCell.getCellStyle();
                            int color = cStyle.getFillForegroundColorColor().getIndex();
                            back_color_head = color;
                        }
                        if (head_font_default) {
                            XSSFCellStyle cStyle = xssfCell.getCellStyle();
                            XSSFFont xssfFont = cStyle.getFont();
                            if (xssfFont.getBold()) {
                                font_bold_head = 1;
                            }
                            font_name_head = xssfFont.getFontName();
                            font_size_head = xssfFont.getFontHeightInPoints();
                        }
                        String title = headMsgArr[1];
                        Map grid = new HashMap();
                        grid.put(ExcelConstant.COL, c);
                        grid.put(ExcelConstant.ROW, r);
                        grid.put(ExcelConstant.TITLE_NAME, title);
                        grid.put(ExcelConstant.COLOR_WORD, 8);
                        grid.put(ExcelConstant.COLOR_BACK, back_color_head);
                        grid.put(ExcelConstant.SHEET_NUM, i + 1);
                        is_h = true;
                        //grid.put("excel_id", i+1);
                        headData.add(grid);
                    } else if (is_body) {
                        value = value.substring(value.indexOf(":") + 1);
                        Map map = JsonMsg.stringToMap(value);
                        if (map == null || map.size() == 0) {
                            xssfWorkbook.close();
                            result.put(ContextConstant.SUCCESS, false);
                            result.put(ContextConstant.RETURN_CODE, ContextConstant.FAIL_CODE_40000);
                            result.put(ContextConstant.MSG, "模板主体参数有误，请检查");
                            return result;
                            //return JsonMsg.returnMsg(ContextConstant.FAIL_CODE_40000,"模板主体参数有误，请检查", null);
                        }
						/*String[] bodyMsgArr = value.split(":");
						if (bodyMsgArr == null || bodyMsgArr.length != 2){
							xssfWorkbook.close();
							return JsonMsg.returnMsg("40000","模板有误", null);
						}*/
                        if (body_color_default) {
                            XSSFCellStyle cStyle = xssfCell.getCellStyle();
                            int color = cStyle.getFillForegroundColorColor().getIndex();
                            back_color_body = color;
                        }
                        if (body_font_default) {
                            XSSFCellStyle cStyle = xssfCell.getCellStyle();
                            XSSFFont xssfFont = cStyle.getFont();
                            if (xssfFont.getBold()) {
                                font_bold_body = 1;
                            }
                            font_size_body = xssfFont.getFontHeightInPoints();
                            font_name_body = xssfFont.getFontName();
                        }
                        //String filed = bodyMsgArr[1];
                        Map msgMap = JsonMsg.stringToMap(value);
                        String coordinate = msgMap.get(ContextConstant.COO) + "";
                        Map grid = new HashMap();

                        if (coordinate.equals(ContextConstant.ROW)) {
                            grid.put(ExcelConstant.COO_COL, -1);
                            grid.put(ExcelConstant.COO_ROW, r);
                        } else if (coordinate.equals(ContextConstant.COL)) {
                            grid.put(ExcelConstant.COO_COL, c);
                            grid.put(ExcelConstant.COO_ROW, -1);
                        } else {
                            grid.put(ExcelConstant.COO_COL, c);
                            grid.put(ExcelConstant.COO_ROW, r);
                        }
                        if (null == msgMap.get(ContextConstant.FIELD) || "".equals(msgMap.get(ContextConstant.FIELD) + "")) {
                            continue;
                        }
                        grid.put(ExcelConstant.VALUE_KEY, msgMap.get(ContextConstant.FIELD));
                        grid.put(ExcelConstant.VALUE_TYPE, msgMap.get(ContextConstant.DATA_TYPE));
                        grid.put(ExcelConstant.SHEET_NUM, i + 1);
                        grid.put(ExcelConstant.COLOR_BACK, back_color_body);
                        grid.put(ExcelConstant.COLOR_WORD, 8);
                        //grid.put("excel_id", i+1);
                        bodyData.add(grid);
                    } else if (is_sql) {
                        value = value.substring(value.indexOf(":") + 1);
                        Map map = JsonMsg.stringToMap(value);
                        if (map == null || map.size() == 0) {
                            xssfWorkbook.close();
                            result.put(ContextConstant.SUCCESS, false);
                            result.put(ContextConstant.RETURN_CODE, ContextConstant.FAIL_CODE_40000);
                            result.put(ContextConstant.MSG, "模板SQL参数有误，请检查");
                            return result;
                            //return JsonMsg.returnMsg(ContextConstant.FAIL_CODE_40000,"模板SQL参数有误，请检查", null);
                        }
                        sql.add(value);
                    } else {
                        LOGGER.debug("未知数据：" + value);
                    }
                }
                if (is_h) {
                    head_row++;
                }
            }
            //获取单元格合并信息
            List<CellRangeAddress> mr = sheet.getMergedRegions();
            for (int ra = 0; ra < mr.size(); ra++) {
                CellRangeAddress cra = mr.get(ra);
                Map merge = new HashMap();
                merge.put(ExcelConstant.SHEET_NUM, i + 1);
                merge.put(ExcelConstant.START_COL, cra.getFirstColumn());
                merge.put(ExcelConstant.START_ROW, cra.getFirstRow());
                merge.put(ExcelConstant.END_COL, cra.getLastColumn());
                merge.put(ExcelConstant.END_ROW, cra.getLastRow());
                mergeData.add(merge);
            }

            sheetDataMap.put(ExcelConstant.BACK_COLOR_HEAD, back_color_head);
            sheetDataMap.put(ExcelConstant.BACK_COLOR_BODY, back_color_body);
            sheetDataMap.put(ExcelConstant.FONT_SIZE_HEAD, font_size_head);
            sheetDataMap.put(ExcelConstant.FONT_SIZE_BODY, font_size_body);
            sheetDataMap.put(ExcelConstant.FONT_BOLD_HEAD, font_bold_head);
            sheetDataMap.put(ExcelConstant.FONT_BOLD_BODY, font_bold_body);
            sheetDataMap.put(ExcelConstant.FONT_NAME_HEAD, font_name_head);
            sheetDataMap.put(ExcelConstant.FONT_NAME_BODY, font_name_body);
            sheetDataMap.put(ExcelConstant.HEAD_ROW, head_row);
            sheetDataMap.put(ExcelConstant.HEAD_COL, head_col);
            sheetData.add(sheetDataMap);
        }
        excelData.put(ExcelConstant.SELECT_SQL, JsonMsg.objectToString(sql));

        //关闭 xssfWorkbook
        xssfWorkbook.close();

        Map parm = new HashMap();
        parm.put(ContextConstant.EXCEL, excelData);
        parm.put(ContextConstant.SHEET, sheetData);
        parm.put(ContextConstant.HEAD, headData);
        parm.put(ContextConstant.BODY, bodyData);
        parm.put(ContextConstant.MERGE, mergeData);

        result.put(ContextConstant.SUCCESS, true);
        result.put(ContextConstant.RETURN_CODE, ContextConstant.SUCCESS_CODE);
        result.put(ContextConstant.MSG, "模板信息读取成功！");
        result.put(ContextConstant.RESULT, parm);
        return result;
    }

    /**
     * TODO 读取较多数据量报表，解析
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static Map readReport(MultipartFile file, Map kv) throws IOException {
        /**
         * TODO sth
         */
        return null;
    }

    public static Map readReport2007(MultipartFile file) throws IOException {
        /**
         * TODO sth
         */
        return null;
    }

    public static Map readReport2003(MultipartFile file) throws IOException {
        /**
         * TODO sth
         */
        return null;
    }


}
