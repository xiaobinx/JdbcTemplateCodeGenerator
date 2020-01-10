package xyz.bq.jdbctool.generator;

import xyz.bq.jdbctool.QueryRunner;
import xyz.bq.jdbctool.Tool;
import xyz.bq.jdbctool.domain.Column;
import xyz.bq.jdbctool.domain.ResultMeta;
import xyz.bq.jdbctool.template.JavaBeanTemplate;
import xyz.bq.jdbctool.template.QueryDaoTemplate;

import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TableCodeGenerator {

    Tool tool = new Tool();

    private String tableName;

    public TableCodeGenerator(String tableName) {
        this.tableName = tableName;
    }

    public String generateBean(QueryRunner runner) {
        var resultMeta = runner.conn(this::getResultMeta);
        var javaMeta = tool.createJavaMetaFromResultMeta(resultMeta, null);
        return JavaBeanTemplate.formate(javaMeta);
    }

    @NotNull
    public String generateDao(QueryRunner runner) {
        var resultMeta = runner.conn(this::getResultMeta);
        var javaMeta = tool.createJavaMetaFromResultMeta(resultMeta, null);
        return QueryDaoTemplate.formate(javaMeta, resultMeta);
    }

    private ResultMeta getResultMeta(Connection conn) throws SQLException {
        var connmd = conn.getMetaData();

        var pkcolNames = new ArrayList<String>();
        var primaryKeysRs = connmd.getPrimaryKeys(null, null, tableName);
        while (primaryKeysRs.next()) {
            pkcolNames.add(primaryKeysRs.getString("column_name").toLowerCase());
        }
        primaryKeysRs.close();


        var columns = new ArrayList<Column>();
        ResultSet columnsRs = conn.getMetaData().getColumns(null, null, tableName, "%");
        while (columnsRs.next()) {
//            var md = columnsRs.getMetaData();
//            var count = md.getColumnCount();
//            for (var i = 1; i <= count; i++) {
//                var type = md.getColumnTypeName(i);
//                var columnName = md.getColumnName(i);
//                var value = columnsRs.getString(i);
//                System.err.println(String.format("%s, %s, %s", type, columnName, value));
//            }
            var colName = columnsRs.getString("COLUMN_NAME").toLowerCase();
            var colType = columnsRs.getInt("DATA_TYPE");
            var column = new Column(colName, JDBCType.valueOf(colType));
            column.setPrimaryKey(pkcolNames.contains(colName));
            columns.add(column);
        }
        columnsRs.close();

        var resultMeta = new ResultMeta();
        resultMeta.setTableName(tableName);
        resultMeta.setColumns(columns);
        return resultMeta;
    }
}
