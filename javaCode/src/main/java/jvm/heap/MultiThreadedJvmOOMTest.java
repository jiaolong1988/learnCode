package jvm.heap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author jiaolong
 * @date 2025/06/16 10:51
 **/
public class MultiThreadedJvmOOMTest {
    public static void main(String[] args) {
        for(int index = 0; index < 3; index++) {
            new Thread(() -> {

                List<Object> list = new ArrayList<>(10000);
                for (int i = 0; i < 1000000; i++) {
                    String str = "";
                    for (int j = 0; j < 100; j++) {
                        str += UUID.randomUUID().toString();
                    }
                    list.add(str);
                }

            }).start();
        }

    }
}
