package jvm;

/**
 * 方法内联（method inlining）
 * 调用一个方法通常要经历压栈和出栈。调用方法是将程序执行顺序转移到存储该方法的内存地址，将方法的内容执行完后，再返回到执行该方法前的位置。
 *  这种执行操作要求在执行前保护现场并记忆执行的地址，执行后要恢复现场，并按原来保存的地址继续执行。 因此，方法调用会产生一定的时间和空间方面的开销。
 *  那么对于那些方法体代码不是很大，又频繁调用的方法来说，这个时间和空间的消耗会很大。
 * 方法内联的优化行为就是把目标方法的代码复制到发起调用的方法之中，避免发生真实的方法调用。
 * @author: jiaolong
 * @date: 2024/08/23 17:10
 **/
public class MethodInliningTest {
    public static void main(String[] args) {
/*      -XX:+PrintCompilation // 在控制台打印编译过程信息
        -XX:+UnlockDiagnosticVMOptions // 解锁对 JVM 进行诊断的选项参数。默认是关闭的，开启后支持一些特定参数对 JVM 进行诊断
        -XX:+PrintInlining // 将内联方法打印出来
 */

        // 方法调用计数器的默认阈值在 C1 模式下是 1500 次，在 C2 模式在是 10000 次，我们循环遍历超过需要阈值
        for(int i=0; i<1000000; i++) {
            add1(1,2,3,4);
        }
        /* 展示结果
            232   40 %     4       jvm.Test::main @ 2 (23 bytes)
                                      @ 12   jvm.Test::add1 (12 bytes)   inline (hot)
                                        @ 2   jvm.Test::add2 (4 bytes)   inline (hot)
                                        @ 7   jvm.Test::add2 (4 bytes)   inline (hot)
        * */
    }

    private static int add1(int x1, int x2, int x3, int x4) {
        return add2(x1, x2) + add2(x3, x4);
    }
    private static int add2(int x1, int x2) {
        return x1 + x2;
    }

}
