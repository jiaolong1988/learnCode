package org.example.pool.completionService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 *  4.并行地调用多个查询服务，只要有一个成功返回结果，整个服务就可以返回.
 * @author jiaolong
 * @date 2023-07-10 02:38:20
 */
public class CompletionServiceTest4 {

    public static void main(String[] args) throws Exception {

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);
        // 创建 CompletionService
        CompletionService<Integer> cs = new ExecutorCompletionService<>(executor);

        // 用于保存 Future 对象
        List<Future<Integer>> futures = new ArrayList<>(3);
        // 提交异步任务，并保存 future 到 futures
        futures.add(cs.submit(() -> getPriceByS1()));
        futures.add(cs.submit(() -> getPriceByS2()));
        futures.add(cs.submit(() -> getPriceByS3()));

        // 获取最快返回的任务执行结果
        Integer r = 0;
        try {
            // 只要有一个成功返回，则 break
            for (int i = 0; i < 3; ++i) {
                r = cs.take().get();
                // 简单地通过判空来检查是否成功返回
                if (r != null) {
                    break;
                }
            }
        } finally {
            // 取消所有任务
            for (Future<Integer> f : futures)
                f.cancel(true);
        }


        // 返回结果
        System.out.println("返回结果："+r);
        executor.shutdown();

    }

    public static int getPriceByS1() {
        sleep(30);
        System.out.println("getPriceByS1: 1");
        sleep(5);
        System.out.println("getPriceByS1: 1-2");
        return 1;
    }

    public static int getPriceByS2() {
        sleep(30);
        System.out.println("getPriceByS2: 2");
        return 2;
    }

    public static int getPriceByS3() {
        sleep(5);
        System.out.println("getPriceByS3: 3");
        return 3;
    }

    public static void save(int r) {
        System.out.println("save " + r);
    }

    public static void sleep(int time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            //此处中断异常忽略
        }
    }
}