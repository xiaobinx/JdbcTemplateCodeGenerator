package xyz.bq.jdbctool.generator;

import xyz.bq.jdbctool.QueryRunner;
import xyz.bq.jdbctool.template.JavaBeanTemplate;
import xyz.bq.jdbctool.template.QueryDaoTemplate;
import xyz.bq.jdbctool.Tool;
import xyz.bq.jdbctool.domain.Column;
import xyz.bq.jdbctool.domain.ResultMeta;


import javax.validation.constraints.NotNull;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QueryCodeGenerator {

    Tool tool = new Tool();

    private String javaBeanClassName;
    private String sql;

    public QueryCodeGenerator(String javaBeanClassName, String sql) {
        this.javaBeanClassName = javaBeanClassName;
        this.sql = sql;
    }

    @NotNull
    public String generateBean(QueryRunner runner) {
        var resultMeta = runner.query(sql, this::createResultMetaFromRs);
        resultMeta.setSql(sql);
        var javaMeta = tool.createJavaMetaFromResultMeta(resultMeta, javaBeanClassName);
        return JavaBeanTemplate.formate(javaMeta);
    }

    @NotNull
    public String generateDao(QueryRunner runner) {
        var resultMeta = runner.query(sql, this::createResultMetaFromRs);
        resultMeta.setSql(sql);
        var javaMeta = tool.createJavaMetaFromResultMeta(resultMeta, javaBeanClassName);
        return QueryDaoTemplate.formate(javaMeta, resultMeta);
    }


    @NotNull
    private ResultMeta createResultMetaFromRs(ResultSet rs) throws SQLException {
        var meta = rs.getMetaData();
        var count = meta.getColumnCount();
        var rm = new ResultMeta();
        var columns = new ArrayList<Column>();
        rm.setColumns(columns);
        for (var i = 1; i <= count; i++) {
            var colName = meta.getColumnName(i);
            var colType = meta.getColumnType(i);
            var column = new Column(colName, JDBCType.valueOf(colType));
            columns.add(column);
        }
        return rm;
    }
}
