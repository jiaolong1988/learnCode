package org.example.pool.completionService;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 2.原理
 * CompletionService 原理
 * @author jiaolong
 * @date 2023-07-10 02:40:16
 */
public class CompletionServiceTest2 {

    public static void main(String[] args) throws Exception {

        // 创建阻塞队列
        BlockingQueue<Integer> bq = new LinkedBlockingQueue<>();

        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);
        // 异步向电商 S1 询价
        Future<Integer> f1 = executor.submit(() -> getPriceByS1());
        // 异步向电商 S2 询价
        Future<Integer> f2 = executor.submit(() -> getPriceByS2());
        // 异步向电商 S3 询价
        Future<Integer> f3 = executor.submit(() -> getPriceByS3());

        // 电商 S1 报价异步进入阻塞队列
        executor.execute(() -> {
            try {
                bq.put(f1.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // 电商 S2 报价异步进入阻塞队列
        executor.execute(() -> {
            try {
                bq.put(f2.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // 电商 S3 报价异步进入阻塞队列
        executor.execute(() -> {
            try {
                bq.put(f3.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 异步保存所有报价
        for (int i = 0; i < 3; i++) {
            Integer r = bq.take();
            executor.execute(() -> save(r));
        }

        executor.shutdown();
    }

    public static int getPriceByS1() {
        sleep(30);
        return 1;
    }
    public static int getPriceByS2() {
        sleep(30);
        return 2;
    }
    public static int getPriceByS3() {
        sleep(5);
        return 3;
    }
    public static void save(int r) {
        System.out.println("save "+r);
    }
    public static void sleep(int time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}