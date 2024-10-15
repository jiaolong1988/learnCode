package jvm.heap;

import java.util.ArrayList;
import java.util.List;

/**
 * VM Args：-Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
 *  生成对文件
 *  -XX:+HeapDumpBeforeFullGC
 *  生成gc日志
 *  -XX:+PrintGCDetails -XX:+PrintHeapAtGC -Xloggc:./logs/gc.log
 *
 * @author zzm
 */
public class HeapOOM {

    static class OOMObject {
    }

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<OOMObject>();

        while (true) {
            list.add(new OOMObject());
        }
    }
}
