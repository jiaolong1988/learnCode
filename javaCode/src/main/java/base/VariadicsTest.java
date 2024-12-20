package base;

/**
 * 可变参数测试
 * @author jiaolong
 * @date 2024/12/19 15:38
 **/
public class VariadicsTest {
    public static void main (String [] argv) {
        //方式1
        String [] args = {"hello", "world"};
        test(args);

        //方式2
        test("hello", "world");

        //方式3
        test();

        //方式4
        log("User[ %s ]has logged in at[ %s--%s]", "Alice", "10:00 AM","China");
    }

    public static void test(String ... args) {
        for (String arg : args) {
            System.out.println("--> " + arg);
        }
        System.out.println();
    }

    // 自定义的日志打印函数，使用可变参数
    public static void log(String format, Object... args) {
        String info = String.format(format, args);
        System.out.println(info);
    }

}
