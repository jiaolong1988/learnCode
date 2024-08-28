package jvm;

/**
 * 打印gc信息，及生成dump文件[内存溢出时]
 * 原文链接：https://blog.csdn.net/weixin_44606481/article/details/136038078
 * @author: jiaolong
 * @date: 2024/08/27 10:34
 **/
public class GCLogIngTest {
    static final int SIZE = 2 * 1024 * 1024;

    public static void main(String[] args) {
        printGcLog();
        OutOfMemoryErrorDumpFile();
    }

    public static void OutOfMemoryErrorDumpFile() {
        for(int i=0;i<100000000;i++){
            int[] a = new int[SIZE];
        }

        /*
            -XX:+HeapDumpOnOutOfMemoryError        OutOfMemory异常时生成dump文件
            -XX:HeapDumpPath=./logs/jvm.hprof      设置dump文件的保存路径

            -XX:+HeapDumpBeforeFullGC              当JVM 执行 FullGC 前执行 dump
            -XX:+HeapDumpAfterFullGC               当JVM 执行 FullGC 后执行 dump

            -Xmx12m -Xms12m -XX:+HeapDumpOnOutOfMemoryError  -XX:HeapDumpPath=./logs/jvm.hprof
            -Xmx13m -Xms13m -XX:+HeapDumpBeforeFullGC  -XX:HeapDumpPath=./logs/jvm.hprof

        */
    }
    public static void printGcLog() {
        /*
            -XX:+PrintGCDetails 输出GC 的详细日志
            -XX:+PrintGCDateStamps 输出GC 的时间戳（以日期的形式，如 2013-05-04T21:53:59.234+0800）
            -XX:+PrintHeapAtGC 在进行GC的前后打印出堆的信息
            -Xloggc:./logs/gc.log 日志文件的输出路径

            -XX:+PrintGCDateStamps -XX:+PrintGCDetails -Xloggc:./logs/gc-%t.log
         */
    }

}
