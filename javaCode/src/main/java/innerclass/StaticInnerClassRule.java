package innerclass;

/**
 静态内部类
 1.	可以定义任何变量和方法(包括非静态变量变量和方法)。但不能引用【实例变量和实例方法】。
 2.	可以在外部类的 任何方法中 实例化对象。
 * @author jiaolong
 * @date 2023-06-30 11:05:14
 */
public class StaticInnerClassRule {
///////////////////【外部类开始】///////////////////////////////
    //外部类实例方法
    String a="z";
    void test() {}

    static int staticInt=0;

    //可以在 静态代码块、静态方法、实例方法 中实例化对象
    static {
        B b = new  B();
    }
    static void staticMethod() {
        B b = new  B();
    }
    void testInnnerClass() {
        B b = new  B();
    }
///////////////////【外部类结束】///////////////////////////////

///////////////////【内部类开始】///////////////////////////////
    //静态内部类
    static class B {
        //1.定义任何变量和方法
        String c="cc";
        void xx() {}

        static String d="dd";
        static void yy() {}
        int x = staticInt;

        //2.不能引用外部类 的非静态变量
        //String x =a;

        //3.不能引用外部类 的非静态方法
        //test();
    }
    ///////////////////【内部类结束】///////////////////////////////
}
