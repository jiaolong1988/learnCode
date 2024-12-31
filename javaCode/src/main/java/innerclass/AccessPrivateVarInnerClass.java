package innerclass;
/**
 * 外部类 可以访问 内部类的一切变量和方法，不论变量与方法是否私有
 * 内部类本质
 *         内部类本质 相当于一个 外部类的一个变量，不论内部类变量、方法 是否是 私有的，对于外部类而言都可以访问(内部类本身就是外部类的一部分)。
 *
 *         非静态内部类
 *         1.不能定义任何static变量和方法。但可以引用外部类的任何方法和变量，包括static变量和方法。
 *         2.只能在外部类的实例方法中 进行new实例化。
 *
 *         静态内部类
 *         1.	可以定义任何变量和方法(包括非静态变量变量和方法)。但不能引用【实例变量和实例方法】。
 *         2.	可以在外部类的 任何方法中 实例化对象。
 *
 * @author jiaolong
 * @date 2023-07-07 11:42:01
 */
public class AccessPrivateVarInnerClass {

    class A {
        private String x = "x";

        private void test() {
            System.out.println("A Private Method");
        }
    }

    static class B {
        private String y = "y";

        private void test() {
            System.out.println("B Private Method");
        }
    }

    public void test() {
        A a = new A();
        System.out.println("A private filed:" + a.x);
        a.test();
    }

    public static void main(String[] args) {
        AccessPrivateVarInnerClass t = new AccessPrivateVarInnerClass();
        t.test();

        System.out.println();

        B b = new B();
        System.out.println("B private filed:" + b.y);
        b.test();
    }
}
