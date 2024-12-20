package reflect;


/**
 * The type New object test.
 *
 * @author jiaolong
 * @date 2024 /12/19 15:40
 */
public class NewObjectTest {

    public static void main(String[] args) throws Exception {
        //1. 通过反射创建 无参数对象方式1.
        NewObjectTest instance1 = NewObjectTest.class.newInstance();
        instance1.test();


        //2.创建接口实现类对象，然后将其转换为 接口类型对象
        Object daoImpl = DaoImpl.class.getConstructor(NewObjectTest.class).newInstance(new NewObjectTest());
        Dao dao = getCastedObject(Dao.class, daoImpl);
        dao.daoTest();
    }



    public static void test() {
        System.out.println("test \r\n");
    }

    public static <T> T getCastedObject(Class<T> type, Object instance) throws Exception {
        return type.cast(instance);
    }


    interface Dao{
        void daoTest();
    }

    public class DaoImpl implements Dao{
        @Override
        public void daoTest() {
            System.out.println("interface 【DaoTest】 implement the class 【DaoImpl】");
        }
    }
}
