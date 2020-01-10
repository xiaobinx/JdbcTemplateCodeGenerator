package xyz.bq.jdbctool.domain;

import java.util.ArrayList;

public class JavaMeta {
    private String className;
    private String variableName;
    private ArrayList<Feild> feilds;

    public JavaMeta(String className, String variableName, ArrayList<Feild> feilds) {
        this.className = className;
        this.variableName = variableName;
        this.feilds = feilds;
    }

    public JavaMeta() {
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public ArrayList<Feild> getFeilds() {
        return feilds;
    }

    public void setFeilds(ArrayList<Feild> feilds) {
        this.feilds = feilds;
    }

    @Override
    public String toString() {
        return "JavaMeta{" +
                "className='" + className + '\'' +
                ", variableName='" + variableName + '\'' +
                ", feilds=" + feilds +
                '}';
    }
}
