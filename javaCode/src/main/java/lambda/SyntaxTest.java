package lambda;

import java.util.function.Function;

/**
 * lambda 的 function函数，变量定义规则
 * @author jiaolong
 * @date 2025/04/29 16:06
 **/
public class SyntaxTest {
    //实例变量
    private int instanceVar = 1;
    //局部静态变量
    private static int staticVar = 2;

    public void method(String a) {
        int localVar = 3; // 必须为final或事实上final, 建议写法： final int localVar = 3;

        /* 必须为final或事实上final,此时在给对象赋值，程序报错。
           localVar=8;
           a="hh";
        */;

        Function<String,String> r = (b) -> {
            System.out.println(a);
            System.out.println(this.instanceVar);  // 可以访问实例变量
            System.out.println(SyntaxTest.staticVar);     // 可以访问静态变量
            System.out.println(localVar);      // ===>【可以访问局部变量（必须为final）】
            return b;
        };


       r.apply("");
    }

    public static void main(String[] args) {
        final String a = "a";
        new SyntaxTest().method(a);
    }
}
