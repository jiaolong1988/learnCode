package reflect;

import java.lang.reflect.Constructor;

/**
 * @author jiaolong
 * @date 2024/12/20 10:44
 **/
public class InnerClassNewObjectTest {
    public static void main(String[] args) {
        //内部类 如何用反射的方式 获取对象实例？
        try {
            // 创建外部类的实例
            InnerClassTestOfOuerClass outer = new InnerClassTestOfOuerClass();
            // 获取内部类的 Class 对象
            Class<?> innerClass = InnerClassTestOfOuerClass.InnerClass.class;

            // 获取内部类的构造函数，参数为【外部类Class】
            Constructor<?> constructor = innerClass.getDeclaredConstructor(outer.getClass());
            // 创建内部类的实例，参数为【外部类的实例】
            Object innerInstance = constructor.newInstance(outer);

            // 调用内部类的方法
            innerClass.getMethod("display").invoke(innerInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
