package innerclass;

/**
 * 非静态内部类
 * 1.不能定义任何static变量和方法。但可以引用外部类的任何方法和变量，包括static变量和方法。
 * 2.只能在外部类的实例方法中 进行new实例化。
 *
 * @author jiaolong
 * @date 2023-06-30 11:05:14
 */
public class NoStaticInnerClassRule {
    static String staticField = "xx";

    static void staticTest() {
        System.out.println("static method");
    }

    //非静态内部类
    class A {
        //不能定义静态变量
        //static String c="xx";
        //不能定义static方法
        //static void aa() {}

        //可以引用静态变量
        String c = staticField;

        //可以引用 静态方法
        void notStaticMethod() {
            staticTest();
        }
    }

    static {
        //不能 实例化 非静态内部类
        //A a = new A();
    }

    static void staticMethod() {
        //不能在静态方法 实例化 非静态内部类
        //A a = new A();
    }

    void testInnnerClass() {
        //只能在 非静态方法 实例化
        A a = new A();
    }

}
