package jdbc;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * ResultSet分为：可滚动、可更新结果集
 *
 * resultSetType：控制ResultSet的类型，该参数可以取如下三个值。
 * 	    ·ResultSet.TYPE_FORWARD_ONLY：该常量控制记录指针只能向前移动。这是JDK1.4以前的【默认值】。
 * 	    ·ResultSet.TYPE_SCROLL_INSENSITIVE：该常量控制记录指针可以自由移动（可滚动结果集），但底层数据的改变不会影响ResultSet的内容。
 * 	    .ResultSet.TYPE_SCROLL_SENSITIVE：  该常量控制记录指针可以自由移动（可滚动结果集），而且底层数据的改变会影响ResultSet的内容。
 *
 * 	    TYPE_SCROLL_INSENSITIVE、TYPE_SCROLL_SENSITIVE两个常量的作用需要【底层数据库驱动的支持】，
 * 			对于有些数据库驱动来说，这两个常量并【没有太大的区别】。
 *
 * resultSetConcurrency：控制ResultSet的并发类型，该参数可以接收如下两个值。
 * 	    ·ResultSet.CONCURREAD_ONLY：该常量指示ResultSet是只读的并发模式（默认）。
 * 	    .ResultSet.CONCURUPDATABLE：该常量指示ResultSet是可更新的并发模式。
 *
 * @author jiaolong
 * @date 2025/01/02 14:10
 **/
public class ResultSetTest {
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

    public void query(String sql) throws Exception {
        // 加载驱动
        Class.forName(driver);
        try (   // 获取数据库连接
                Connection conn = DriverManager.getConnection(url, user, pass);
                // 使用Connection来创建一个PreparedStatement对象
                // 传入控制结果集可滚动，可更新的参数。
                PreparedStatement pstmt = conn.prepareStatement(sql
                        , ResultSet.TYPE_SCROLL_INSENSITIVE
                        , ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = pstmt.executeQuery()
        ) {
            //结果集指针移动到最后一行，并获取总行数
            rs.last();
            int rowCount = rs.getRow();

            for (int i = rowCount; i > 0; i--) {
                // 移动指针到第i条记录 与 next previous 类似
                rs.absolute(i);
                System.out.println(rs.getString(1) + "\t" + rs.getString(2) );

                // 修改记录指针所有记录、第2列的值
                rs.updateString(2, "学生名xx" + i);
                // 提交修改
                rs.updateRow();

            }
        }
    }

    public static void main(String[] args) throws Exception {
        ResultSetTest rt = new ResultSetTest();
        rt.initParam("mysql.ini");
        rt.query("select * from my_test");
    }
}

