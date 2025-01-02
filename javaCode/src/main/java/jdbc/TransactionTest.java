package jdbc;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

/**
 * @author jiaolong
 * @date 2025/01/02 16:52
 **/
public class TransactionTest {
    private String driver;
    private String url;
    private String user;
    private String pass;

    public void initParam(String paramFile) throws Exception {
        // 使用Properties类来加载属性文件
        Properties props = new Properties();
        props.load(new FileInputStream(paramFile));
        driver = props.getProperty("driver");
        url = props.getProperty("url");
        user = props.getProperty("user");
        pass = props.getProperty("pass");
    }

    public void insertInTransaction(String[] sqls) throws Exception {
        // 加载驱动
        Class.forName(driver);
        try ( Connection conn = DriverManager.getConnection(url, user, pass)) {
            boolean autoCommit = conn.getAutoCommit();
            // 关闭自动提交，开启事务
            conn.setAutoCommit(false);

            try (   // 使用Connection来创建一个Statment对象
                    Statement stmt = conn.createStatement()
            ) {
                stmt.addBatch(sqls[0]);
                stmt.addBatch(sqls[1]);
                stmt.executeBatch();
            }
            // 提交事务
            conn.commit();
            // 恢复自动提交设置
            conn.setAutoCommit(autoCommit);
        }
    }

    public static void main(String[] args) throws Exception {
        TransactionTest tt = new TransactionTest();
        tt.initParam("mysql.ini");

        String[] sqls = new String[]{
                "insert into my_test values(null , 'aaa')",
                "insert into my_test values(null , 'bbb')",
        };
        tt.insertInTransaction(sqls);
    }
}