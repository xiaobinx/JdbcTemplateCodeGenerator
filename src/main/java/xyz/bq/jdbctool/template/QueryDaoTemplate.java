package xyz.bq.jdbctool.template;

import xyz.bq.jdbctool.TemplateTool;
import xyz.bq.jdbctool.domain.Column;
import xyz.bq.jdbctool.domain.Feild;
import xyz.bq.jdbctool.domain.JavaMeta;
import xyz.bq.jdbctool.domain.ResultMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class QueryDaoTemplate {
    static final String indents = TemplateTool.indents;

    public static String formate(JavaMeta jm, ResultMeta rm) {
        var fields = jm.getFeilds();
        var className = jm.getClassName();
        var tableName = rm.getTableName();
        var variableName = jm.getVariableName();
        var primaryKeys = jm.getFeilds().stream().filter(Feild::isPrimaryKey).collect(Collectors.toList());
        var hasPrimaryKeys = primaryKeys.size() > 0;
        var colsStr = rm.getColumns().stream().map(Column::getColName).collect(Collectors.joining(","));
        var insert = formateInsertSql(jm, tableName);
        var insertSql = insert[0];
        var insertParam = insert[1];
        var primaryKeysConditionColstr = primaryKeys.stream().map(f -> f.getColumnName() + " = ? ").collect(Collectors.joining("and "));
        var primaryKeysParamsstr = primaryKeys.stream().map(f -> variableName + "." + f.getGetterName() + "()").collect(Collectors.joining(", "));

        var src = new StringBuilder();
        // import
        src.append("import javax.annotation.Resource;\n");
        src.append("import org.springframework.jdbc.core.JdbcTemplate;\n");
        src.append("import org.springframework.jdbc.core.RowMapper;\n");
        src.append("import org.springframework.stereotype.Repository;\n");
        src.append("import java.util.List;\n");
        src.append("import java.util.Optional;\n");
        // class
        src.append('\n');
        src.append("@Repository\n");
        src.append("public class ").append(className).append("Dao {\n");
        // template
        src.append(indents).append("@Resource\n");
        src.append(indents).append("private JdbcTemplate template;\n");
        // RowMapper
        src.append('\n');
        src.append(indents).append("private RowMapper<").append(className).append("> ")
                .append("rowMapper = (rs, i) -> {\n");
        src.append(indents).append(indents).append("var ").append(variableName)
                .append(" = new ").append(className).append("();\n");
        // RowMapper -> setters
        for (var field : fields) {
            TemplateTool.appendSetter(src, variableName, field);
        }
        src.append(indents).append(indents).append("return ").append(variableName).append(";\n");
        src.append(indents).append("};\n");
        // query func
        src.append('\n');
        src.append(indents).append("public List<").append(className).append("> query() {\n");
        if (tableName != null && tableName.length() > 0) {
            src.append(indents).append(indents).append("var sql = \"select ").append(colsStr).append(" from \\\"").append(tableName).append("\\\"\";\n");
        } else {
            src.append(indents).append(indents).append("var sql = \"").append(rm.getSql()).append("\";\n");
        }
        src.append(indents).append(indents).append("return template.query(sql, rowMapper);\n");
        src.append(indents).append("}\n");
        if (hasPrimaryKeys) {
            // queryOne func            src.append('\n');
            src.append(indents).append("public Optional<").append(className).append("> queryOne(").append(className).append(" ").append(variableName).append(") {\n");
            src.append(indents).append(indents).append("var sql = \"select ").append(colsStr).append(" \" +\n")
                    .append(indents).append(indents).append(indents).append(indents)
                    .append("\"from \\\"").append(tableName).append("\\\" where ")
                    .append(primaryKeysConditionColstr).append("\";\n");
            src.append(indents).append(indents).append("var args = new Object[]{").append(primaryKeysParamsstr).append("};\n");
            src.append(indents).append(indents).append("var one = template.queryForObject(sql, rowMapper, args);\n");
            src.append(indents).append(indents).append("return Optional.ofNullable(one);\n");
            src.append(indents).append("}\n");
        }

        if (tableName != null && tableName.length() > 0) {
            src.append('\n');
            src.append(indents).append("public int insert(").append(className).append(" ").append(variableName).append(") {\n");
            src.append(indents).append(indents).append("var sql = \"").append(insertSql).append("\";\n");
            src.append(indents).append(indents).append(insertParam).append('\n');
            src.append(indents).append(indents).append("return template.update(sql, args);\n");
            src.append(indents).append("}\n");
        }

        if (hasPrimaryKeys) {
            // update func
            var normalFeilds = jm.getFeilds().stream().filter(f -> !f.isPrimaryKey()).collect(Collectors.toList());
            var updateColsStr = normalFeilds.stream().map(feild -> feild.getColumnName() + " = ?").collect(Collectors.joining(", "));
            var updateSql = String.format("update \\\"%s\\\" set\" +\n" + indents + indents + indents + indents +
                    "\"%s\" +\n" + indents + indents + indents + indents +
                    "\"where %s", tableName, updateColsStr, primaryKeysConditionColstr);
            var argslist = new ArrayList<Feild>();
            argslist.addAll(normalFeilds);
            argslist.addAll(primaryKeys);
            var argsStr = argslist.stream().map(feild -> String.format("%s.%s()", jm.getVariableName(), feild.getGetterName()))
                    .collect(Collectors.joining(", "));
            var updateArgsStr = String.format("var args = new Object[]{%s};", argsStr);
            src.append('\n');
            src.append(indents).append("public int update(").append(className).append(" ").append(variableName).append(") {\n");
            src.append(indents).append(indents).append("var sql = \"").append(updateSql).append("\";\n");
            src.append(indents).append(indents).append(updateArgsStr).append('\n');
            src.append(indents).append(indents).append("return template.update(sql, args);\n");
            src.append(indents).append("}\n");


            // del func
            src.append('\n');
            src.append(indents).append("public int del(").append(className).append(" ").append(variableName).append(") {\n");
            src.append(indents).append(indents).append("var sql = \"delete from \\\"").append(tableName)
                    .append("\\\" where ").append(primaryKeysConditionColstr).append("\";\n");
            src.append(indents).append(indents).append("var args = new Object[]{").append(primaryKeysParamsstr).append("};\n");
            src.append(indents).append(indents).append("return template.update(sql, args);\n");
            src.append(indents).append("}\n");
        }

        src.append("}");
        return src.toString();
    }

    private static String[] formateInsertSql(JavaMeta jm, String tableName) {
        var cols = jm.getFeilds().stream().map(Feild::getColumnName).collect(Collectors.joining(", "));
        var colsParam = Arrays.stream(new String[jm.getFeilds().size()]).map(i -> " ?").collect(Collectors.joining(","));
        var insertSql = String.format("insert into \\\"%s\\\"(%s) \" +\n" + indents + indents + indents + indents + "\"values(%s)", tableName, cols, colsParam);

        var args = jm.getFeilds().stream().map(feild -> String.format("%s.%s()", jm.getVariableName(), feild.getGetterName()))
                .collect(Collectors.joining(", "));
        var insertParam = String.format("var args = new Object[]{%s};", args);
        return new String[]{insertSql, insertParam};
    }
}
