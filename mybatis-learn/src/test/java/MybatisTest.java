import dao.EmployeeMapper;
import model.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.List;

/**
 * @author: jiaolong
 * @date: 2024/06/25 11:59
 **/
public class MybatisTest {
    private static Logger logger = Logger.getLogger(MybatisTest.class);

    public static void main(String[] args) throws Exception {

//��һ������ȡmybatis-config.xml�����ļ�
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");

//�ڶ���������SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

//����������SqlSession
        SqlSession session = sqlSessionFactory.openSession();

//���Ĳ�����ȡMapper�ӿڶ���
        EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);

//���岽������Mapper�ӿڶ���ķ����������ݿ⣻
        List<Employee> employee = mapper.getQuery();
        System.out.println(employee);

//��������ҵ����
        for(Employee e :employee){
            logger.info("��ѯ���: " + e.getId() + "--" + e.getEmail());
        }

    }
}
