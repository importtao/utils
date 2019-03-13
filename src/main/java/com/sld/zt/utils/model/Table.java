package com.sld.zt.utils.model;

import java.util.List;

public class Table {
    private String tableName;
    private List<Colume> colume;
    private String tableCommon;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Colume> getColume() {
        return colume;
    }

    public void setColume(List<Colume> colume) {
        this.colume = colume;
    }

    public String getTableCommon() {
        return tableCommon;
    }

    public void setTableCommon(String tableCommon) {
        this.tableCommon = tableCommon;
    }


}
