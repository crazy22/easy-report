package com.godmk.tool.easyreport.controller;


import com.godmk.tool.easyreport.constant.ContextConstant;
import com.godmk.tool.easyreport.constant.MenuConstant;
import com.godmk.tool.easyreport.service.IMenuService;
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
@Api(value = "Model", tags = "Model")
@Controller
@RequestMapping("Menu")
public class ModelController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelController.class);

    @Resource
    private IMenuService iModelService;

    @ApiOperation("获取菜单")
    @RequestMapping(value = "getMenu", method = RequestMethod.GET)
    @ResponseBody
    public String getMenu(HttpServletRequest request) {
        Map body = ParamHandler.bodyParam(request);

        Map judge = MapUtil.judgeData(body, new String[]{MenuConstant.STATE});
        if (!Boolean.valueOf(judge.get(MapUtil.JUDGE_PASS).toString())) {
            body.put(MenuConstant.STATE, 1);
        }
        if ("1".equals((body.get(MenuConstant.STATE) + "").trim()) ||
                "0".equals((body.get(MenuConstant.STATE) + "").trim())) {
        } else if ("99".equals((body.get(MenuConstant.STATE) + "").trim())) {
            body.remove(MenuConstant.STATE);
        } else {
            return JsonMsg.returnMsg(ContextConstant.FAIL_CODE_40000, "MENU状态有误", null);
        }
        Map result = iModelService.getMenu(body);

        boolean success = Boolean.valueOf(result.get(ContextConstant.SUCCESS).toString());
        if (!success) {
            return JsonMsg.returnMsg(ContextConstant.FAIL_CODE_40000, result.get(ContextConstant.MSG).toString(), null);
        }
        return JsonMsg.returnMsg(ContextConstant.SUCCESS_CODE,
                result.get(ContextConstant.MSG).toString(),
                result.get(ContextConstant.RESULT));

    }

    @ApiOperation("添加菜单")
    @RequestMapping(value = "addMenu", method = RequestMethod.GET)
    @ResponseBody
    public String addMenu(HttpServletRequest request) {
        Map body = ParamHandler.bodyParam(request);


        String html = "<div>";

        return html;
    }

    @ApiOperation("修改菜单")
    @RequestMapping(value = "modifyMenu", method = RequestMethod.GET)
    @ResponseBody
    public String modifyMenu(HttpServletRequest request) {
        Map body = ParamHandler.bodyParam(request);


        String html = "<div>";

        return html;
    }

    @ApiOperation("删除菜单")
    @RequestMapping(value = "deleteMenu", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteMenu(HttpServletRequest request) {
        Map body = ParamHandler.bodyParam(request);


        String html = "<div>";

        return html;
    }
}
