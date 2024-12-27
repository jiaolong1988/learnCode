import dao.EmployeeMapper;
import model.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * MyBatis foreach 标签
 * http://www.mybatis.cn/archives/51.html
 * @author: jiaolong
 * @date: 2024/06/25 11:59
 **/
public class MybatisTest {
    private static Logger logger = Logger.getLogger(MybatisTest.class);

    public static void main(String[] args) throws Exception {
//第一步：读取mybatis-config.xml配置文件
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");

//第二步：构建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

//第三步：打开SqlSession
        SqlSession session = sqlSessionFactory.openSession();

//第四步：获取Mapper接口对象
        EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);

//第五步：调用Mapper接口对象的方法操作数据库；
        List<Integer> list =new ArrayList<>();
        list.add(6);
        list.add(7);
        list.add(8);
        List<Employee> employee = mapper.getQuery(list);

//第六步：业务处理
        for(Employee e :employee){
            logger.info("查询结果: " + e.getId() + "--" + e.getEmail());
        }

    }
}
