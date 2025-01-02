package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author jiaolong
 * @date 2024/12/31 17:09
 **/
public class ConnMySqlTest {
    public static void main(String[] args) throws Exception {
        // 1.检查驱动类是否存在，可以不写
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (
                // 2.使用DriverManager获取数据库连接,
                // 其中返回的Connection就代表了Java程序和数据库的连接
                // 不同数据库的URL写法需要查驱动文档知道，用户名、密码由DBA分配
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://127.0.0.1:3306/employees"
                        , "root", "1qaz2wsx");
                // 3.使用Connection来创建一个Statment对象
                Statement stmt = conn.createStatement();
                // 4.执行SQL语句
			/*
			Statement有三种执行sql语句的方法：
			1 execute 可执行任何SQL语句。- 返回一个boolean值，
			  如果执行后第一个结果是ResultSet，则返回true，否则返回false
			2 executeQuery 执行Select语句 － 返回查询到的结果集
			3 executeUpdate 用于执行DML语句。－ 返回一个整数，
			  代表被SQL语句影响的记录条数
			*/
            ResultSet rs = stmt.executeQuery("select s.* from departments s ")
        ) {
            // ResultSet有系列的getXxx(列索引 | 列名)，用于获取记录指针
            // 指向行、特定列的值，不断地使用next()将记录指针下移一行，
            // 如果移动之后记录指针依然指向有效行，则next()方法返回true。
            while (rs.next()) {
                System.out.println(
                         rs.getString(1) + "\t"
                        + rs.getString(2) );
            }
        }
    }
}
