package xyz.bq.jdbctool;

import xyz.bq.jdbctool.generator.QueryCodeGenerator;

public class Main {
    public static void main(String[] args) {

        var config = new Config();
        var runner = new QueryRunner(config);

        String className = "UserInfo";
        String sql = "SELECT * FROM user_info;";
        var queryCodeGenerator = new QueryCodeGenerator(className, sql);
//        var src = queryCodeGenerator.generateBean(runner);
        var src = queryCodeGenerator.generateDao(runner);
        System.err.println(src);
    }
}
