package xyz.bq.jdbctool.domain;

import java.sql.JDBCType;

public class Column {
    private String colName;
    private JDBCType colType;
    private boolean isPrimaryKey = false;

    public Column() {
    }

    public Column(String colName, JDBCType colType) {
        this.colName = colName;
        this.colType = colType;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public JDBCType getColType() {
        return colType;
    }

    public void setColType(JDBCType colType) {
        this.colType = colType;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    @Override
    public String toString() {
        return "Column{" +
                "colName='" + colName + '\'' +
                ", colType=" + colType +
                ", isPrimaryKey=" + isPrimaryKey +
                '}';
    }
}
