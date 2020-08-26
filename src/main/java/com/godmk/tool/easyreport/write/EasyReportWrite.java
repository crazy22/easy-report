package com.godmk.tool.easyreport.write;

import com.alibaba.excel.EasyExcel;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.handler.AbstractSheetWriteHandler;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.merge.OnceAbsoluteMergeStrategy;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.godmk.tool.easyreport.constant.DataTypeEnum;
import com.godmk.tool.easyreport.constant.ExcelConstant;
import com.godmk.tool.easyreport.utils.FormatUtil;
import com.godmk.tool.easyreport.utils.MapUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;


/**
 * 写 -- Excel
 */
public class EasyReportWrite {


    /**
     * 生成报表
     *
     * @param out
     * @param headData
     * @param bodyData
     * @throws IOException
     */
    public static void writeExcelXLSX(OutputStream out, Map headData, Map<String, List> bodyData) {
        //校验数据
        Map judge = MapUtil.judgeData(headData, new String[]{
                ExcelConstant.SHEET,
                ExcelConstant.HEAD,
                ExcelConstant.BODY,
                ExcelConstant.MERGE
        });
        if (!Boolean.valueOf(judge.get(MapUtil.JUDGE_PASS).toString())) {//参数校验未通过
            return;
        }

        Object sheetObj = headData.get(ExcelConstant.SHEET);
        List<Map> sheetList = new ArrayList<Map>();
        if (sheetObj instanceof List) {
            sheetList = (List<Map>) sheetObj;
        } else {//表格参数格式有误
            return;
        }

        boolean hasHead = Boolean.valueOf(headData.get(ExcelConstant.HEAD).toString());
        boolean hasMerge = Boolean.valueOf(headData.get(ExcelConstant.MERGE).toString());
        boolean hasBody = Boolean.valueOf(headData.get(ExcelConstant.BODY).toString());

        Map headMap = new HashMap();
        Map mergeMap = new HashMap();
        Map bodyMap = new HashMap();

        if (hasHead) {
            headMap = (Map) headData.get(ExcelConstant.HEAD_MAP);
        }
        if (hasHead) {
            mergeMap = (Map) headData.get(ExcelConstant.MERGE_MAP);
        }
        if (hasBody) {
            bodyMap = (Map) headData.get(ExcelConstant.BODY_MAP);
        }
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(out).autoCloseStream(false).build();
            //操作表格
            for (Map sheetMap : sheetList) {
                // sheet个数
                int sheet_num = Integer.parseInt(sheetMap.get(ExcelConstant.SHEET_NUM).toString());
                // 标题行数
                int head_num = Integer.parseInt(sheetMap.get(ExcelConstant.HEAD_ROW).toString());
                // 标题列数
                int head_col = Integer.parseInt(sheetMap.get(ExcelConstant.HEAD_COL).toString());

                // excel sheet对象
                WriteSheet writeSheet = EasyExcel.writerSheet(sheet_num, sheetMap.get(ExcelConstant.SHEET_NAME).toString()).build();

                List<WriteHandler> customWriteHandlerList = new ArrayList<>();
                if (head_num > 0) {
                    List<Map> headList = (List) headMap.get(ExcelConstant.SHEET_NUM + "_" + sheet_num);
                    List<List<String>> head = getHead(headList, head_col, head_num);
                    // 设置头
                    if (head != null && head.size() > 0) {
                        writeSheet.setHead(head);
                    }
                    // 表格样式
                    customWriteHandlerList.add(tableStyle(sheetMap));
                }
                // 合并单元格
                if (hasMerge) {
                    List<Map> mergeList = (List) mergeMap.get(ExcelConstant.SHEET_NUM + "_" + sheet_num);
                    // 不知道咋回事，自动合并了，下面的代码会导致excel损坏
                    /*List<AbstractSheetWriteHandler> abstractSheetWriteHandlers = merge(mergeList);
                    if (abstractSheetWriteHandlers != null && abstractSheetWriteHandlers.size() > 0) {
                        customWriteHandlerList.addAll(abstractSheetWriteHandlers);
                    }*/
                } else {
                    customWriteHandlerList.add(new LongestMatchColumnWidthStyleStrategy());
                }
                writeSheet.setCustomWriteHandlerList(customWriteHandlerList);

                List<List<Object>> data = new ArrayList<>();
                if (hasBody) {
                    List<Map> bodyList = (List) bodyMap.get(ExcelConstant.SHEET_NUM + "_" + sheet_num);
                    List sheet_data = bodyData.get(ExcelConstant.SHEET_NUM + "_" + sheet_num);
                    data = getBody(sheet_data, bodyList);
                }
                //填充HashMap
                excelWriter.write(data, writeSheet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 组装标题栏信息
     *
     * @param headList
     * @param head_col
     * @param head_num
     * @return
     */
    public static List<List<String>> getHead(List<Map> headList, int head_col, int head_num) {
        List<List<String>> headLLS = new ArrayList<List<String>>();
        if (headList == null || headList.size() == 0) {
            return headLLS;
        }
        for (int i = 0; i < head_col; i++) {
            List<String> colTitleList = new ArrayList<>();
            int j = 0;
            for (Map head : headList) {
                String title_name = head.get(ExcelConstant.TITLE_NAME) + "";
                int col = Integer.parseInt(head.get(ExcelConstant.COL) + "");
                int row = Integer.parseInt(head.get(ExcelConstant.ROW) + "");
                if (col == i && j == row) {
                    colTitleList.add(title_name);
                    if (j == head_num) {
                        break;
                    }
                    j++;
                }
            }
            if (colTitleList == null || colTitleList.size() == 0) {
                colTitleList.add("");
            }
            headLLS.add(colTitleList);
        }
        return headLLS;
    }

    /**
     * 组装正文数据
     *
     * @param bodyData
     * @param bodyList
     * @return
     */
    public static List<List<Object>> getBody(List<Map> bodyData, List<Map> bodyList) {
        List<List<Object>> data = new ArrayList<List<Object>>();
        if (bodyData == null || bodyData.size() == 0 || bodyList == null || bodyList.size() == 0) {
            return data;
        }

        Boolean wildcard_row = false;
        //列通配情况
        for (Map bodyDataMap : bodyData) {
            List<Object> rowList = new ArrayList<>();
            int i = 0;
            for (Map bodyMsg : bodyList) {
                int coordinate_row = Integer.parseInt(bodyMsg.get(ExcelConstant.COO_ROW).toString());
                int coordinate_col = Integer.parseInt(bodyMsg.get(ExcelConstant.COO_COL).toString());
                String vk = bodyMsg.get(ExcelConstant.VALUE_KEY) + "";
                int vt = Integer.parseInt(bodyMsg.get(ExcelConstant.VALUE_TYPE) + "");
                if (coordinate_col == -1) {
                    wildcard_row = true;
                    break;
                }
                if (coordinate_row == -1) {
                    if (i == coordinate_col) {
                        if (
                                vt == DataTypeEnum.NUMBER_FORMAT_P1.getIndex() ||
                                        vt == DataTypeEnum.NUMBER_FORMAT_P2.getIndex() ||
                                        vt == DataTypeEnum.NUMBER_FORMAT_P3.getIndex() ||
                                        vt == DataTypeEnum.NUMBER_FORMAT_P4.getIndex() ||
                                        vt == DataTypeEnum.NUMBER_PER.getIndex() ||
                                        vt == DataTypeEnum.NUMBER_INT.getIndex() ||
                                        vt == DataTypeEnum.MONEY.getIndex()
                        ) {
                            Object value = bodyDataMap.get(vk);
                            if (value == null || "".equals(value)) {
                                rowList.add("");
                            } else {
                                double num = Double.parseDouble(bodyDataMap.get(vk) + "");
                                String number = FormatUtil.formatNumber(num, DataTypeEnum.valueOf(vt));
                                rowList.add(number);
                            }
                        } else if (
                                vt == DataTypeEnum.DATE.getIndex() ||
                                        vt == DataTypeEnum.DATE_0.getIndex() ||
                                        vt == DataTypeEnum.DATE_1.getIndex() ||
                                        vt == DataTypeEnum.DATE_2.getIndex() ||
                                        vt == DataTypeEnum.DATE_3.getIndex() ||
                                        vt == DataTypeEnum.DATE_4.getIndex() ||
                                        vt == DataTypeEnum.DATE_5.getIndex() ||
                                        vt == DataTypeEnum.DATE_6.getIndex()

                        ) {
                            Object value = bodyDataMap.get(vk);
                            if (value == null || "".equals(value)) {
                                rowList.add("");
                            } else {
                                Date date = (Date) bodyDataMap.get(vk);
                                String time = FormatUtil.formatDate(date, DataTypeEnum.valueOf(vt));
                                rowList.add(time);
                            }
                        } else if (
                                vt == DataTypeEnum.TIME_STAMP.getIndex() ||
                                        vt == DataTypeEnum.TIME_STAMP_0.getIndex() ||
                                        vt == DataTypeEnum.TIME_STAMP_1.getIndex() ||
                                        vt == DataTypeEnum.TIME_STAMP_2.getIndex() ||
                                        vt == DataTypeEnum.TIME_STAMP_3.getIndex() ||
                                        vt == DataTypeEnum.TIME_STAMP_4.getIndex() ||
                                        vt == DataTypeEnum.TIME_STAMP_5.getIndex() ||
                                        vt == DataTypeEnum.TIME_STAMP_6.getIndex()
                        ) {
                            Object value = bodyDataMap.get(vk);
                            if (value == null || "".equals(value)) {
                                rowList.add("");
                            } else {
                                int date = Integer.parseInt(bodyDataMap.get(vk) + "");
                                String time = FormatUtil.stampToDate(date, DataTypeEnum.valueOf(vt));
                                rowList.add(time);
                            }
                        } else {
                            rowList.add(bodyDataMap.get(vk));
                        }
                        i++;
                    }
                }
            }
            if (wildcard_row) {
                break;
            }
            data.add(rowList);
        }
        if (wildcard_row) {
            // 行通配情况 ，TODO 未处理标题栏问题，格式化数据问题
            int i = 0;
            for (Map bodyMsg : bodyList) {
                List<Object> colList = new ArrayList<>();
                int coordinate_row = Integer.parseInt(bodyMsg.get(ExcelConstant.COO_ROW).toString());
                int coordinate_col = Integer.parseInt(bodyMsg.get(ExcelConstant.COO_COL).toString());
                String vk = bodyMsg.get(ExcelConstant.VALUE_KEY) + "";
                String vt = bodyMsg.get(ExcelConstant.VALUE_TYPE) + "";
                //System.out.println(" ########## vt: " + vt + " ########## ");
                if (coordinate_col == -1) {
                    // 列通配情况 （大多数情况）
                    if (i == coordinate_row) {
                        //取出map中所有vk的值组成list
                        bodyData.forEach(n -> colList.add((n.get(vk))));
                        //List<String> orders = bodyData.stream().map(String::toString).collect(Collectors.toList());
                    }
                    data.add(colList);
                    i++;
                }
            }
        }
        return data;
    }

    /**
     * 合并单元格
     *
     * @param mergeList
     */
    public static List<AbstractSheetWriteHandler> merge(List<Map> mergeList) {
        if (mergeList == null || mergeList.size() == 0) {
            return null;
        }
        List<AbstractSheetWriteHandler> mergeStrategy = new ArrayList<>();
        for (Map mergeMap : mergeList) {
            int sc = Integer.parseInt(mergeMap.get(ExcelConstant.START_COL).toString());
            int sr = Integer.parseInt(mergeMap.get(ExcelConstant.START_ROW).toString());
            int ec = Integer.parseInt(mergeMap.get(ExcelConstant.END_COL).toString());
            int er = Integer.parseInt(mergeMap.get(ExcelConstant.END_ROW).toString());
            if (sc < 0 || sr < 0 || ec < 0 || er < 0) {
                throw new IllegalArgumentException("All parameters must be greater than 0");
            }
            OnceAbsoluteMergeStrategy onceAbsoluteMergeStrategy = new OnceAbsoluteMergeStrategy(sr, er, sc, ec);
            mergeStrategy.add(onceAbsoluteMergeStrategy);
        }
        return mergeStrategy;
    }

    /**
     * 获取表格样式
     *
     * @param sheetMap
     * @return
     */
    public static HorizontalCellStyleStrategy tableStyle(Map sheetMap) {
        //校验数据
        Map judge = MapUtil.judgeData(sheetMap, new String[]{
                ExcelConstant.BACK_COLOR_BODY,
                ExcelConstant.BACK_COLOR_HEAD,
                ExcelConstant.FONT_SIZE_HEAD,
                ExcelConstant.FONT_BOLD_HEAD,
                ExcelConstant.FONT_NAME_HEAD,
                ExcelConstant.FONT_SIZE_BODY,
                ExcelConstant.FONT_BOLD_BODY,
                ExcelConstant.FONT_NAME_BODY
        });

        if (!Boolean.valueOf(judge.get(MapUtil.JUDGE_PASS).toString())) {
            return new HorizontalCellStyleStrategy(new WriteCellStyle(), new WriteCellStyle());
        }

        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();


        short headColor = IndexedColors.fromInt(
                Integer.parseInt(
                        sheetMap.get(ExcelConstant.BACK_COLOR_HEAD).toString()
                )
        ).getIndex();
        short bodyColor = IndexedColors.fromInt(
                Integer.parseInt(
                        sheetMap.get(ExcelConstant.BACK_COLOR_BODY).toString()
                )
        ).getIndex();
        // 标题背景颜色设置
        headWriteCellStyle.setFillForegroundColor(headColor);
        //正文背景颜色设置
        contentWriteCellStyle.setFillForegroundColor(bodyColor);
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 标题栏部分字体样式
        WriteFont headFont = new WriteFont();
        int fsh = Integer.parseInt(sheetMap.get(ExcelConstant.FONT_SIZE_HEAD).toString());
        int fbh = Integer.parseInt(sheetMap.get(ExcelConstant.FONT_BOLD_HEAD).toString());
        headFont.setBold(fbh == 1);
        headFont.setFontName(sheetMap.get(ExcelConstant.FONT_NAME_HEAD).toString());
        headFont.setFontHeightInPoints((short) fsh);
        headWriteCellStyle.setWriteFont(headFont);

        //正文部分字体样式
        WriteFont bodyFont = new WriteFont();
        int fsb = Integer.parseInt(sheetMap.get(ExcelConstant.FONT_SIZE_BODY).toString());
        int fbb = Integer.parseInt(sheetMap.get(ExcelConstant.FONT_BOLD_BODY).toString());
        bodyFont.setBold(fbb == 1);
        bodyFont.setFontName(sheetMap.get(ExcelConstant.FONT_NAME_BODY).toString());
        bodyFont.setFontHeightInPoints((short) fsb);
        contentWriteCellStyle.setWriteFont(bodyFont);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        return horizontalCellStyleStrategy;
    }


}
