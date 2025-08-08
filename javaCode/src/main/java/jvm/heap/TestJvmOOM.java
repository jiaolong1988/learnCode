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
        List<Object> list = new ArrayList<>(1000000);
        for (int i = 0; i < 1000; i++) {
            String str = "";
            for (int j = 0; j < 100; j++) {
                str += "jiaolong";
            }
            list.add(str);
        }

        List<Object> list1 = new ArrayList<>(1000000);
        for (int i = 0; i < 1000000; i++) {
            String str = "";
            for (int j = 0; j < 100; j++) {
                str += UUID.randomUUID().toString();
            }
            list1.add(str);
        }
        System.out.println("ok");
    }
}