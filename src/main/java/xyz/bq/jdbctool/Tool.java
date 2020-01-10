package xyz.bq.jdbctool;

import xyz.bq.jdbctool.domain.Feild;
import xyz.bq.jdbctool.domain.JavaMeta;
import xyz.bq.jdbctool.domain.ResultMeta;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.JDBCType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Tool {

    HashMap<JDBCType, Class<?>> jdbcToJavaTypeMapper = new HashMap<>();

    public Tool() {
        jdbcToJavaTypeMapper.put(JDBCType.VARCHAR, String.class);
        jdbcToJavaTypeMapper.put(JDBCType.DATE, LocalDate.class);
        jdbcToJavaTypeMapper.put(JDBCType.TIMESTAMP, LocalDateTime.class);
        jdbcToJavaTypeMapper.put(JDBCType.NUMERIC, BigDecimal.class);
        jdbcToJavaTypeMapper.put(JDBCType.BIGINT, long.class);
        jdbcToJavaTypeMapper.put(JDBCType.INTEGER, int.class);
    }

    public Optional<Class<?>> parseJavaType(JDBCType colType) {
        return Optional.ofNullable(jdbcToJavaTypeMapper.get(colType));
    }

    public JavaMeta createJavaMetaFromResultMeta(ResultMeta resultMeta, String className) {

        var feilds = new ArrayList<Feild>();
        for (var column : resultMeta.getColumns()) {
            var columnName = column.getColName().toLowerCase();
            var columnWords = columnName.toLowerCase().split("_");
            var feildName = parseJavaBeanVariableName(columnWords);
            var feildCName = parseJavaBeanClassName(columnWords);
            var setterName = "set" + feildCName;
            var getterName = "get" + feildCName;
            var colType = column.getColType();
            var ojavaType = parseJavaType(column.getColType());
            ojavaType.orElseThrow(() -> {
                throw new RuntimeException(String.format("数据库列类型到Java类型没有合适的映射.(JDBCType.%s->?javatype)" +
                        "需要在defaultJdbcToJavaTypeMapper中添加新的映射.", colType.getName()));
            });
            var javaType = ojavaType.get();
            var feild = new Feild(feildName, columnName, javaType, getterName, setterName);
            feild.setPrimaryKey(column.isPrimaryKey());
            feilds.add(feild);
        }

        var javaMeta = new JavaMeta();
        if (null == className) {
            className = parseJavaBeanClassName(resultMeta.getTableName().split("_"));
        }
        javaMeta.setClassName(className);
        javaMeta.setVariableName(LowerCaseFirstLetter(className));
        javaMeta.setFeilds(feilds);
        return javaMeta;
    }


    public String LowerCaseFirstLetter(String str) {
        int a = str.charAt(0);
        if (a >= 65 && a <= 90) {
            char[] cs = str.toCharArray();
            cs[0] += 32;
            return String.valueOf(cs);
        } else {
            return str;
        }
    }

    @NotNull
    public String upperCaseFirstLetter(String str) {
        int a = str.charAt(0);
        if (a >= 97 && a <= 122) {
            char[] cs = str.toCharArray();
            cs[0] -= 32;
            return String.valueOf(cs);
        } else {
            return str;
        }
    }

    @NotNull
    public String parseJavaBeanClassName(String[] tableWords) {
        var sb = new StringBuilder();
        for (var words : tableWords) {
            sb.append(upperCaseFirstLetter(words));
        }
        return sb.toString();
    }

    @NotNull
    public String parseJavaBeanVariableName(String[] tableWords) {
        var sb = new StringBuilder();
        sb.append(tableWords[0]);
        for (var i = 1; i < tableWords.length; i++) {
            sb.append(upperCaseFirstLetter(tableWords[i]));
        }
        return sb.toString();
    }
}
