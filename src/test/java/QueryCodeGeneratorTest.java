import org.junit.jupiter.api.Test;
import xyz.bq.jdbctool.Config;
import xyz.bq.jdbctool.QueryRunner;
import xyz.bq.jdbctool.generator.QueryCodeGenerator;

public class QueryCodeGeneratorTest {

    private Config config = new Config();
    private QueryRunner runner = new QueryRunner(config);

    private String className = "UserInfo";
    private String sql = "select * from user_info;";

    QueryCodeGenerator generator = new QueryCodeGenerator(className, sql);

    @Test
    public void generateBean() {
        var src = generator.generateBean(runner);
        System.err.println(src);
    }


    @Test
    void generateDao() {
        var src = generator.generateDao(runner);
        System.err.println(src);
    }

}
