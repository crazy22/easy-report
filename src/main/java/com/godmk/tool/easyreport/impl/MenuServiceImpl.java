package com.godmk.tool.easyreport.impl;

import com.godmk.tool.easyreport.constant.ContextConstant;
import com.godmk.tool.easyreport.constant.MenuConstant;
import com.godmk.tool.easyreport.dao.MenuDao;
import com.godmk.tool.easyreport.service.IMenuService;
import com.godmk.tool.easyreport.utils.MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl implements IMenuService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Resource
    private MenuDao menuDao;

    @Override
    public Map getMenu(Map body) {
        Map result = new HashMap();
        Map judge = MapUtil.judgeData(body, new String[]{MenuConstant.GROUP});
        if (!Boolean.valueOf(judge.get(MapUtil.JUDGE_PASS).toString())) {
            result.put(ContextConstant.SUCCESS, judge.get(MapUtil.JUDGE_PASS));
            result.put(ContextConstant.MSG, "参数异常");
            return result;
        }
        List<Map> menuList = menuDao.getMenu(body);
        if (menuList == null || menuList.size() == 0) {
            result.put(ContextConstant.SUCCESS, ContextConstant.SUCCESS_CODE);
            result.put(ContextConstant.MSG, "未找到菜单信息");
            result.put(ContextConstant.RESULT, null);
            return result;
        }


        result.put(ContextConstant.SUCCESS, ContextConstant.SUCCESS_CODE);
        result.put(ContextConstant.MSG, "成功");
        return result;
    }
}
