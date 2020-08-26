package com.godmk.tool.easyreport.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author crazy22
 */
@Component
public class Excel {
    @Value("${excel.id}")
    public Integer id;
    @Value("${excel.name}")
    public String name;
    @Value("${excel.sql}")
    public String sql;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
