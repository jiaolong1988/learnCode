package org.example.pool.completionService;


import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CompletionService 使用方法
 * @author jiaolong
 * @date 2023-07-10 02:39:58
 */
public class CompletionServiceTest3 {

    public static void main(String[] args) throws Exception {

        // 创建线程池
        ExecutorService executor =  Executors.newFixedThreadPool(3);
        // 创建 CompletionService
        CompletionService<Integer> cs = new  ExecutorCompletionService<>(executor);

        // 异步向电商 S1 询价
        cs.submit(()->getPriceByS1());
        // 异步向电商 S2 询价
        cs.submit(()->getPriceByS2());
        // 异步向电商 S3 询价
        cs.submit(()->getPriceByS3());

        // 将询价结果异步保存到数据库
        for (int i=0; i<3; i++) {
            Integer r = cs.take().get();
            executor.execute(()->save(r));
        }

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

