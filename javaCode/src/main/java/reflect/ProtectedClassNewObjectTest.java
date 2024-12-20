package reflect;

import java.lang.reflect.Constructor;

/**
 * @author jiaolong
 * @date 2024/12/20 10:48
 **/
public class ProtectedClassNewObjectTest {
    public static void main(String[] args) throws Exception {

        /*  getDeclaredConstructor 会返回所有声明的构造方法，包括包级、protected 和 private 的构造方法
            如果你尝试获取的构造方法也是包级的（即没有 public、protected 或 private 修饰符），并且你在不同的包中调用 getConstructor，Java 也会抛出 NoSuchMethodException。
         */
        Constructor<?> constructor =  ProtectedClass.class.getDeclaredConstructor();

        ProtectedClass protectedClass = (ProtectedClass) constructor.newInstance();
        protectedClass.daoTest();
    }
}

class ProtectedClass {
    // 注意：构造方法 默认是 protected 访问权限的。
    //ProtectedClass(){}

    public void daoTest() {
        System.out.println("interface 【DaoTest】 implement the class 【DaoImpl】");
    }
}