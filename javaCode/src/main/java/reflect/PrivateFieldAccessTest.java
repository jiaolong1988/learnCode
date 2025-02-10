package reflect;

import java.lang.reflect.Field;

/**
 * 通过反射获取私有变量的值
 * @author jiaolong
 * @date 2024-12-20 11:08
 */
public class PrivateFieldAccessTest {
    private String testName = "testName";

    public static void main(String[] args) throws Exception {

        Class clazz = User.class;
        User u = new User();
        u.setName("jl");

        //1.获取私有字段的Field对象
        Field ff = clazz.getDeclaredField("name");
        System.out.println("name是私有变量，是否可以访问：" + ff.isAccessible());//这里的结果是false

        ff.setAccessible(true);
        System.out.println("name是私有变量,获name字段的值：" + ff.get(u));

		System.out.println("---");

        // 2.反射name字段是否可以访问
        Class<?> studentClass = Student.class;
        Student student = new Student();

        for (Field field : studentClass.getDeclaredFields()) {
            System.out.println("字段名称：" + field.getName()+"  是否可以访问："+field.isAccessible());// 这里的结果是false
            field.setAccessible(true);

            System.out.println("获取对象字段的值：" + field.get(student));
        }

        System.out.println("---");

        //3.获取父类的变量
        Class<?> parentClass = Child.class.getSuperclass();
        // 获取父类的静态字段
        Field parentStaticField = parentClass.getDeclaredField("name");
        parentStaticField.setAccessible(true);
        System.out.println("父类的静态字段的值：" + parentStaticField.get(new Child()));
    }
}
class Child extends User {

}

class User {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class Student {
    private String studentName = "xiaoming";
    private String studentAge = "18";
    private String address = "beijing";
}
