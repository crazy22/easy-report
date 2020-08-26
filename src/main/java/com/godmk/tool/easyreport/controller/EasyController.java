package com.godmk.tool.easyreport.controller;


import com.godmk.tool.easyreport.constant.ContextConstant;
import com.godmk.tool.easyreport.constant.MenuConstant;
import com.godmk.tool.easyreport.service.IMenuService;
import com.godmk.tool.easyreport.utils.*;
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
    RedisTools redisTools;

    @ApiOperation("test add")
    @RequestMapping(value = "add", method = RequestMethod.GET)
    @ResponseBody
    public void doAdd(HttpServletRequest request) {
        redisTools.testAdd();
    }
    @ApiOperation("test get")
    @RequestMapping(value = "get", method = RequestMethod.GET)
    @ResponseBody
    public String doGet(HttpServletRequest request) {
        return redisTools.testGet();
    }
}
