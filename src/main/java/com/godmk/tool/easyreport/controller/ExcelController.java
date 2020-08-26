package com.godmk.tool.easyreport.controller;

import com.godmk.tool.easyreport.constant.ExcelConstant;
import com.godmk.tool.easyreport.constant.ContextConstant;
import com.godmk.tool.easyreport.service.IExcelService;
import com.godmk.tool.easyreport.utils.ParamHandler;
import com.godmk.tool.easyreport.write.EasyReportRead;
import com.godmk.tool.easyreport.write.EasyReportWrite;
import com.godmk.tool.easyreport.utils.JsonMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * easy_report
 * 通过配置简化表报
 *
 * @author crazy22
 */
@Api(value = "Excel", tags = "Excel")
@Controller
@RequestMapping("omg")
public class ExcelController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelController.class);

    @Resource
    private IExcelService iExcelService;

    /**
     * poi 颜色对比
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation("poi 颜色对比")
    @RequestMapping(value = "excelColor", method = RequestMethod.GET)
    @ResponseBody
    public String excelColor(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        String html = "<div>";
        html = html + "<table>";
        html = html +
                "<tr style=\"background-color:rgb(252,213,180)\">" +
                "<th>IndexedColors_名称</th>" +
                "<th>IndexedColors_编号</th>" +
                "<th>IndexedColors_颜色值</th>" +
                "<th>IndexedColors_颜色</th>" +
                "</tr>";
        HSSFWorkbook wk = new HSSFWorkbook();
        for (IndexedColors e : IndexedColors.values()) {
            html = html + "<tr><td style=\"width:300px\">";
            html = html + e.name();
            html = html + "</td><td style=\"width:100px\">";
            html = html + e.getIndex();
            html = html + "</td><td style=\"width:100px\">";
            HSSFColor color = wk.getCustomPalette().getColor(e.getIndex());
            if (color != null) {
                short[] rgb = color.getTriplet();
                html = html + rgb[0] + "," + rgb[1] + "," + rgb[2];
                html = html + "</td><td style=\"width:100px;background-color:rgb(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ")\">";
            } else {
                html = html + "RGB获取失败";
                html = html + "</td><td style=\"width:100px;\")\">RGB获取失败";
            }
            html = html + "</tr>";

        }
        html = html + "</table>";
        html = html + "</div>";
        wk.close();
        return html;
    }

    /**
     * poi 颜色对比excel下载
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(" poi 颜色对比excel下载")
    @RequestMapping(value = "excelColorDownload", method = RequestMethod.GET)
    @ResponseBody
    public String excelColorDownload(HttpServletRequest request,
                                     HttpServletResponse response) {

        String excelName = "Excel颜色对照表";
        OutputStream out = null;
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();

        HSSFWorkbook wk = new HSSFWorkbook();
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'" + URLEncoder.encode(excelName + (new SimpleDateFormat("yyyyMMdd")).format(new Date()) + ".xlsx", "UTF-8"));
            out = response.getOutputStream();
            XSSFSheet sheet = xssfWorkbook.createSheet("Excel颜色对照表");

            XSSFRow row = sheet.createRow(0);

            XSSFCell cell1 = row.createCell(0);
            cell1.setCellValue("颜色名称");

            XSSFCell cell2 = row.createCell(1);
            cell2.setCellValue("颜色RGB");

            XSSFCell cell3 = row.createCell(2);
            cell3.setCellValue("颜色Index");

            XSSFCell cell4 = row.createCell(3);
            cell4.setCellValue("颜色示例");

            XSSFCellStyle style = xssfWorkbook.createCellStyle();
            style.setFillForegroundColor(new XSSFColor(new Color(252, 213, 180)));
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cell1.setCellStyle(style);
            cell2.setCellStyle(style);
            cell3.setCellStyle(style);
            cell4.setCellStyle(style);
            int i = 1;
            for (IndexedColors e : IndexedColors.values()) {
                XSSFRow colorRow = sheet.createRow(i++);

                XSSFCell colorCell1 = colorRow.createCell(0);
                colorCell1.setCellValue(e.name());
                HSSFColor hssf_color = wk.getCustomPalette().getColor(e.getIndex());
                if (hssf_color != null) {
                    short[] rgb = hssf_color.getTriplet();
                    XSSFCell colorCell2 = colorRow.createCell(1);
                    colorCell2.setCellValue(rgb[0] + "," + rgb[1] + "," + rgb[2]);
                } else {
                    XSSFCell colorCell2 = colorRow.createCell(1);
                    colorCell2.setCellValue("RGB获取失败");
                }

                XSSFCell colorCell3 = colorRow.createCell(2);
                colorCell3.setCellValue(e.getIndex());

                XSSFCell colorCell4 = colorRow.createCell(3);
                XSSFCellStyle color = xssfWorkbook.createCellStyle();
                color.setFillForegroundColor(e.getIndex());
                color.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                colorCell4.setCellStyle(color);
            }
            xssfWorkbook.write(out);
            response.flushBuffer();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (xssfWorkbook != null) {
                    xssfWorkbook.close();
                }
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return excelName;
    }

    /**
     * easyReport demo 下载
     *
     * @param request
     * @param response
     * @param excel_id
     * @return
     */
    @ApiOperation("easyReport demo 下载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "excel_id", value = "报表id", dataType = "Int", paramType = "query", required = true),
            @ApiImplicitParam(name = "parms", value = "参数", dataType = "String", paramType = "query", required = true,
                    example = "{\"id\":1,\"pid\":2}"),
            @ApiImplicitParam(name = "excelName", value = "报表名称", dataType = "String", paramType = "query", required = true),
    })
    @RequestMapping(value = "easyReport", method = RequestMethod.GET)
    public void easyReport(HttpServletRequest request,
                           HttpServletResponse response,
                           int excel_id,
                           String parms,
                           String excelName) throws IOException {
        Map con = new HashMap();
        con.put(ExcelConstant.EXCEL_ID, excel_id);

        Map excelMsg = iExcelService.getExcelModel(con);
        boolean success = Boolean.valueOf(excelMsg.get(ContextConstant.SUCCESS).toString());
        if (!success) {
            return;
        }
        if (StringUtils.isEmpty(excelName)) {
            excelName = "Excel下载";
        }
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'" +
                URLEncoder.encode(
                        excelName + (new SimpleDateFormat("yyyyMMdd")).format(new Date()) + ".xlsx",
                        "UTF-8")
        );

        con.put(ContextConstant.PARMS, parms);
        Map dataMap = iExcelService.getData(con);
        boolean success2 = Boolean.valueOf(dataMap.get(ContextConstant.SUCCESS).toString());
        if (!success2) {
            return;
        }
        EasyReportWrite.writeExcelXLSX(response.getOutputStream(),
                (Map) excelMsg.get(ContextConstant.RESULT), (Map) dataMap.get(ContextConstant.RESULT));
    }

    /**
     * 报表模板读取并保存到数据库
     *
     * @param request
     * @param file
     * @return
     * @throws IOException
     */
    @ApiOperation("上传并保存报表模板")
    @RequestMapping(value = "readExcel", method = RequestMethod.GET)
    @ResponseBody
    public String readExcel(HttpServletRequest request,
                            @RequestParam("file") MultipartFile file) throws IOException {
        //读取模板信息
        Map modelMsg = EasyReportRead.readExcelModel(file);
        boolean readSuccess = Boolean.valueOf(modelMsg.get(ContextConstant.SUCCESS).toString());
        if (!readSuccess) {
            return JsonMsg.returnMsg(modelMsg.get(ContextConstant.RETURN_CODE) + "",
                    modelMsg.get(ContextConstant.MSG).toString(), null);
        }
        //获得模板信息Map
        Map parm = (Map) modelMsg.get(ContextConstant.RESULT);
        //保存数据到数据库
        Map saveResultMap = iExcelService.createExcelModelData(parm);
        boolean success = Boolean.valueOf(saveResultMap.get(ContextConstant.SUCCESS).toString());
        if (!success) {
            return JsonMsg.returnMsg(ContextConstant.FAIL_CODE_40000, saveResultMap.get(ContextConstant.MSG).toString(), null);
        }
        return JsonMsg.returnMsg(ContextConstant.SUCCESS_CODE,
                saveResultMap.get(ContextConstant.MSG).toString(),
                saveResultMap.get(ContextConstant.RESULT));
    }

    /**
     * TODO 更新报表模板
     *
     * @param request
     * @param excel_id
     * @param file
     * @return
     */
    @ApiOperation("更新报表模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "excel_id", value = "报表id", dataType = "Int", paramType = "query", required = true),
    })

    @RequestMapping(value = "modifyExcel", method = RequestMethod.GET)
    @ResponseBody
    public String modifyExcelModel(HttpServletRequest request,
                                   String excel_id, @RequestParam("file") MultipartFile file) throws IOException {
        if (excel_id != null && !"".equals(excel_id.trim())) {
            Map con = new HashMap();
            con.put(ExcelConstant.EXCEL_ID, excel_id);
            //校验模板是否存在
            Map excelModelMap = iExcelService.checkExcelModel(con);
            boolean modelHas = Boolean.valueOf(excelModelMap.get(ContextConstant.SUCCESS).toString());
            if (!modelHas) {
                return JsonMsg.returnMsg(ContextConstant.FAIL_CODE_40000,
                        excelModelMap.get(ContextConstant.MSG).toString(), null);
            }
            //读取模板信息
            Map modelMsg = EasyReportRead.readExcelModel(file);
            boolean readSuccess = Boolean.valueOf(modelMsg.get(ContextConstant.SUCCESS).toString());
            if (!readSuccess) {
                return JsonMsg.returnMsg(modelMsg.get(ContextConstant.RETURN_CODE) + "",
                        modelMsg.get(ContextConstant.MSG).toString(), null);
            }
            //获得模板信息Map
            Map parm = (Map) modelMsg.get(ContextConstant.RESULT);
            //将修改的 excel_id 添加到参数中
            parm.put(ExcelConstant.EXCEL_ID, excel_id);

            //执行修改模板操作
            Map modifyResultMap = iExcelService.modifyExcelModelData(parm);
            boolean success = Boolean.valueOf(modifyResultMap.get(ContextConstant.SUCCESS).toString());
            if (!success) {
                return JsonMsg.returnMsg(ContextConstant.FAIL_CODE_40000, modifyResultMap.get(ContextConstant.MSG).toString(), null);
            }
            return JsonMsg.returnMsg(ContextConstant.SUCCESS_CODE,
                    modifyResultMap.get(ContextConstant.MSG).toString(),
                    modifyResultMap.get(ContextConstant.RESULT));
        }
        return JsonMsg.returnMsg(ContextConstant.FAIL_CODE_40000,
                "excel_id参数不存在",
                "");
    }

    /*
     * TODO
     * 1.更新查询sql接口
     * 2.更新sheet配置接口
     * 3.更新标题栏配置接口
     * 4.更新正文配置接口
     * 5.更新单元格合并信息配置接口
     */

    /**
     * 删除模型数据
     *
     * @param request
     * @param excel_id
     * @return
     */
    @ApiOperation("删除模型数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "excel_id", value = "报表id", dataType = "Int", paramType = "query", required = true),
    })

    @RequestMapping(value = "deleteModel", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteModel(HttpServletRequest request, String excel_id) {
        if (excel_id != null && !"".equals(excel_id.trim())) {
            Map con = new HashMap();
            con.put(ExcelConstant.EXCEL_ID, excel_id);
            //校验模板是否存在
            Map deleteModel = iExcelService.deleteModel(con);
            boolean success = Boolean.valueOf(deleteModel.get(ContextConstant.SUCCESS).toString());
            if (!success) {
                return JsonMsg.returnMsg(ContextConstant.FAIL_CODE_40000, deleteModel.get(ContextConstant.MSG).toString(), null);
            }
            return JsonMsg.returnMsg(ContextConstant.SUCCESS_CODE,
                    deleteModel.get(ContextConstant.MSG).toString(),
                    deleteModel.get(ContextConstant.RESULT));
        }
        return JsonMsg.returnMsg(ContextConstant.FAIL_CODE_40000,
                "excel_id参数不存在",
                "");
    }

    @ApiOperation("excelModel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "excelName", value = "excelName", dataType = "String", paramType = "query", required = true),
    })

    @RequestMapping(value = "excelModel", method = RequestMethod.GET)
    public String excelModel(HttpServletRequest request, String excelName) {
        if (excelName != null && !"".equals(excelName.trim())) {/*Map queryParam = new HashMap();
            queryParam.put("id", 1);
            iExcelService.excelDataList(queryParam);*/
            return "editOnline/index";
            // return "canvas/index";
        }
        return "comm/error_404";
    }

    private int[] To_RGB(int color) {
        /*int b = color / (256 * 256);
        int g = (color - b * 256 * 256) / 256;
        int r = color - b * 256 * 256 - g * 256;*/

        int r = 0xFF & color;
        int g = 0xFF00 & color;
        g >>= 8;
        int b = 0xFF0000 & color;
        b >>= 16;
        return new int[]{r, g, b};

    }

    /**
     * Color对象转换成字符串
     *
     * @param color Color对象
     * @return 16进制颜色字符串
     */
    private static String toHexFromColor(Color color) {
        String r, g, b;
        StringBuilder su = new StringBuilder();
        r = Integer.toHexString(color.getRed());
        g = Integer.toHexString(color.getGreen());
        b = Integer.toHexString(color.getBlue());
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        r = r.toUpperCase();
        g = g.toUpperCase();
        b = b.toUpperCase();
        su.append("0xFF");
        su.append(r);
        su.append(g);
        su.append(b);
        //0xFF0000FF
        return su.toString();
    }

    /**
     * 字符串转换成Color对象
     *
     * @param colorStr 16进制颜色字符串
     * @return Color对象
     */
    public static Color toColorFromString(String colorStr) {
        colorStr = colorStr.substring(4);
        Color color = new Color(Integer.parseInt(colorStr, 16));
        //java.awt.Color[r=0,g=0,b=255]
        return color;
    }


}
