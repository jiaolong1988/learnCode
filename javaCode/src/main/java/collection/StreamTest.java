package collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * list 在stream中出现 出现线程不安全问题，导致 添加的数据为null
 * @author: jiaolong
 * @date: 2024/07/11 10:48
 **/
public class StreamTest {
    public static void main(String[] args) {
        List<Integer> integerList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            integerList.add(i);
        }

        /* ArrayList不是线程安全的，在并行操作时，会出现多线程操作问题，例如出现null值，有可能是在扩容时，复制出现问题。
         同时也会出现 写入值 被覆盖的情况，导致结果数量不对。*/

        //线程不安全 -
        List<Integer> parallelList = new ArrayList<>();
        //线程安全
        List<Integer> parallelList1 = new Vector<>();
        test(integerList, parallelList);

        testOK(integerList);

   //线程安全问题导致  奇数数量小于50
//        for (int i = 0; i < 10; i++) {
//            List<Integer> parallelList = new ArrayList<>();
//            test(integerList, parallelList);
//        }



    }

    private static void test(List<Integer> integerList, List<Integer> parallelList) {
        integerList.stream()
                .parallel()
                .filter(i -> i % 2 == 1)
                .forEach(i -> {
                    //对多线程写入时，由于arraylist没有指定大小(非线程安全)，数组会扩容，导致数据插入会出现null
                    parallelList.add(i);
                    // System.out.println(Thread.currentThread().getName()+"-->"+i);
                });
        System.out.println(parallelList);
        System.out.println(parallelList.size());
    }

    private static void testOK(List<Integer> integerList) {
       List x= integerList.stream()
                .parallel()
                .filter(i -> i % 2 == 1)
                 .collect(Collectors.toList());
        System.out.println(x);
        System.out.println(x.size());
    }
}
