package jvm.heap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author: jiaolong
 * @date: 2024/09/24 15:20
 **/
public class TestJvmOOM {
    //-Xms4m -Xmx4m -XX:+HeapDumpOnOutOfMemoryError
    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            String str = "";
            for (int j = 0; j < 100; j++) {
                str += UUID.randomUUID().toString();
            }
            list.add(str);
        }
        System.out.println("ok");
    }
}