package com.godmk.tool.easyreport.controller;


import com.godmk.tool.easyreport.constant.ContextConstant;
import com.godmk.tool.easyreport.constant.MenuConstant;
import com.godmk.tool.easyreport.service.IMenuService;
import com.godmk.tool.easyreport.utils.EasyTools;
import com.godmk.tool.easyreport.utils.JsonMsg;
import com.godmk.tool.easyreport.utils.MapUtil;
import com.godmk.tool.easyreport.utils.ParamHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * easy_report
 * 模型管理
 *
 * @author crazy22
 */
@Api(value = "Easy", tags = "Easy")
@Controller
@RequestMapping("easy")
public class EasyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EasyController.class);

    @Resource
    EasyTools easyTools;

    @ApiOperation("删除菜单")
    @RequestMapping(value = "deleteMenu", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteMenu(HttpServletRequest request) {
  /*      Map body = ParamHandler.bodyParam(request);


        String html = "<div>";*/
        LOGGER.debug("================================================================");
        return easyTools.getSheetName();
    }
}
