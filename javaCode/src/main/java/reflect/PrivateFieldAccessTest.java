package reflect;

import util.seqExecFunction.service.ExecTaskStatus;

import java.lang.reflect.Field;

/**
 * 通过反射获取私有变量的值
 * @author jiaolong
 * @date 2024-12-20 11:08
 */
public class PrivateFieldAccessTest {
    private String testName = "testName";

    public static String publicName;

    public static void main(String[] args) throws Exception {


        getSetFieldValue();
        System.out.println("---");

        privateFieldVist();

        System.out.println("---");

        getObjectValue();

        System.out.println("---");

        getParentValue();
    }

    private static void getParentValue() throws NoSuchFieldException, IllegalAccessException {
        //3.获取父类的变量
        Class<?> parentClass = Child.class.getSuperclass();
        // 获取父类的静态字段
        Field parentStaticField = parentClass.getDeclaredField("name");
        parentStaticField.setAccessible(true);
        System.out.println("父类的静态字段的值：" + parentStaticField.get(new Child()));
    }

    private static void getObjectValue() throws IllegalAccessException {
        // 2.反射name字段是否可以访问
        Class<?> studentClass = Student.class;
        Student student = new Student();

        for (Field field : studentClass.getDeclaredFields()) {
            System.out.println("字段名称：" + field.getName()+"  是否可以访问："+field.isAccessible());// 这里的结果是false
            field.setAccessible(true);

            System.out.println("获取对象字段的值：" + field.get(student));
        }
    }

    private static void privateFieldVist() throws NoSuchFieldException, IllegalAccessException {
        Class clazz = User.class;
        User u = new User();
        u.setName("jl");

        //1.获取私有字段的Field对象
        Field ff = clazz.getDeclaredField("name");
        System.out.println("name是私有变量，是否可以访问：" + ff.isAccessible());//这里的结果是false

        ff.setAccessible(true);
        System.out.println("name是私有变量,获name字段的值：" + ff.get(u));
    }

    private static void getSetFieldValue() {
        try {
            // 获取 Example 类的 Class 对象
            Class<?> exampleClass = PrivateFieldAccessTest.class;

            // 获取静态字段 staticField
            Field staticField = exampleClass.getField("publicName");
            String initialValue = (String) staticField.get(null); // null 因为是静态字段
            System.out.println("Initial Value: " + initialValue);

            // 修改静态字段的值
            staticField.set(null, "New Value");
            // 获取并打印修改后的值
            String newValue = (String) staticField.get(null);
            System.out.println("Modified Value: " + newValue);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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
