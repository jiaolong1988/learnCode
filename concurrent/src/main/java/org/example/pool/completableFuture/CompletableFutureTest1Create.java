package org.example.pool.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 对象创建 异步操作
 * @author: jiaolong
 * @date: 2024/07/03 13:48
 **/
public class CompletableFutureTest1Create {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 任务 1：洗水壶 -> 烧开水 没有返回结果
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(()->{
            System.out.println("T1: 洗水壶...");
            sleep(1, TimeUnit.SECONDS);

            System.out.println("T1: 烧开水...");
            sleep(15, TimeUnit.SECONDS);
        });


        // 任务 2：洗茶壶 -> 洗茶杯 -> 拿茶叶 获取返回结果
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(()->{
            System.out.println("T2: 洗茶壶...");
            sleep(1, TimeUnit.SECONDS);

            System.out.println("T2: 洗茶杯...");
            sleep(2, TimeUnit.SECONDS);

            System.out.println("T2: 拿茶叶...");
            sleep(1, TimeUnit.SECONDS);
            return " 龙井 ";
        });

        f1.get(); //get方法为阻塞方法
        System.out.println("end1");

        f2.complete("手动取消任务");
        String res = f2.get(); //get方法为阻塞方法
        System.out.println("end2:" + res);
    }

    public static void sleep(int t, TimeUnit u) {
        try {
            u.sleep(t);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
