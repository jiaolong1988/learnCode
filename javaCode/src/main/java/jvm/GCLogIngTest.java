package jvm;

/**
 * 打印gc日志
 * @author: jiaolong
 * @date: 2024/08/27 10:34
 **/
public class GCLogIngTest {
    public static void main(String[] args) {
        /*
            -XX:+PrintGCDetails 输出 GC 的详细日志
            -Xloggc:./logs/gc.log 日志文件的输出路径
        */
        for (int i = 0; i < 10000; i++) {
            System.out.println(i);
        }

    }
}
