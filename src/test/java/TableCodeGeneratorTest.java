import org.junit.jupiter.api.Test;
import xyz.bq.jdbctool.Config;
import xyz.bq.jdbctool.QueryRunner;
import xyz.bq.jdbctool.generator.TableCodeGenerator;

public class TableCodeGeneratorTest {

    private Config config = new Config();
    private QueryRunner runner = new QueryRunner(config);

    private String tableName = "k_day_data";

    TableCodeGenerator generator = new TableCodeGenerator(tableName);

    @Test
    void generateBean() {
        var src = generator.generateBean(runner);
        System.err.println(src);
    }


    @Test
    void generateDao() {
        var src = generator.generateDao(runner);
        System.err.println(src);
    }

}
