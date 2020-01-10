package xyz.bq.jdbctool.domain;

import java.util.ArrayList;

public class ResultMeta {
    private String tableName;
    private String sql;
    private ArrayList<Column> columns;

    public ResultMeta() {
    }

    @Override
    public String toString() {
        return "ResultMeta{" +
                "tableName='" + tableName + '\'' +
                ", sql='" + sql + '\'' +
                ", columns=" + columns +
                '}';
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public ArrayList<Column> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<Column> columns) {
        this.columns = columns;
    }
}
