package xyz.bq.jdbctool;

import xyz.bq.jdbctool.domain.Feild;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TemplateTool {
    public static final String indents = "    ";

    public static void appendSetter(StringBuilder src, String variableName, Feild field) {
        var javaType = field.getJavaType();
        if (javaType == String.class || javaType == Long.class || javaType == BigDecimal.class) {
            simpleSetter(src, variableName, field, javaType.getSimpleName());
        } else if (javaType == long.class) {
            simpleSetter(src, variableName, field, "Long");
        } else if (javaType == Integer.class || javaType == int.class) {
            simpleSetter(src, variableName, field, "Int");
        } else if (javaType == LocalDate.class) {
            src.append(indents).append(indents)
                    .append(variableName).append(".").append(field.getSetterName()).append("(rs.getDate(\"")
                    .append(field.getColumnName()).append("\").toLocalDate());\n");
        } else if (javaType == LocalDateTime.class) {
            src.append(indents).append(indents)
                    .append(variableName).append(".").append(field.getSetterName()).append("(rs.getTimestamp(\"")
                    .append(field.getColumnName()).append("\").toLocalDateTime());\n");
        } else {
            throw new RuntimeException("需要增加新的setter处理方法, javaType: " + javaType.getName());
        }
    }


    private static void simpleSetter(StringBuilder src, String variableName, Feild field, String rsGetName) {
        src.append(indents).append(indents)
                .append(variableName).append(".").append(field.getSetterName()).append("(rs.get")
                .append(rsGetName).append("(\"")
                .append(field.getColumnName()).append("\"));\n");
    }
}
