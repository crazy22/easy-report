package com.godmk.tool.easyreport.dao;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public interface MenuDao {

    List<Map> getMenu(Map con);

    int addMenu(Map con);

    int modifyMenu(Map con);

    int deleteMenu(Map con);


}
