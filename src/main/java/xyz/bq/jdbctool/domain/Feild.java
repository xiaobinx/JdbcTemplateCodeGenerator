package xyz.bq.jdbctool.domain;

import java.sql.JDBCType;

public class Feild {
    private String name;
    private String columnName;
    private Class<?> javaType;
    private String getterName;
    private String setterName;
    private boolean isPrimaryKey = false;

    public Feild(String name, String columnName, Class<?> javaType, String getterName, String setterName) {
        this.name = name;
        this.columnName = columnName;
        this.javaType = javaType;
        this.getterName = getterName;
        this.setterName = setterName;
    }

    public Feild() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    public String getGetterName() {
        return getterName;
    }

    public void setGetterName(String getterName) {
        this.getterName = getterName;
    }

    public String getSetterName() {
        return setterName;
    }

    public void setSetterName(String setterName) {
        this.setterName = setterName;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    @Override
    public String toString() {
        return "Feild{" +
                "name='" + name + '\'' +
                ", columnName='" + columnName + '\'' +
                ", javaType=" + javaType +
                ", getterName='" + getterName + '\'' +
                ", setterName='" + setterName + '\'' +
                ", isPrimaryKey=" + isPrimaryKey +
                '}';
    }
}
