package org.example.pool.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * CompletableFuture-串行方法使用案例
 * 执行方式：
 *  thenApply：该方法会在当前的线程中 执行提供的函数。这意味着，当CompletableFuture完成时，thenApply中的操作会在调用它的线程中立即执行。
 *  thenApplyAsync：该方法会在 一个单独的线程中 异步执行提供的函数。这意味着，当CompletableFuture完成时，thenApplyAsync中的操作会在另一个线程中执行，
 *                  这通常是由ForkJoinPool.commonPool()提供的线程。
 *
 * 使用场景：
 *  thenApply适用于那些不需要进一步异步处理，或者对当前线程没有特别要求的场景。
 *  thenApplyAsync适用于后续[需要并行执行]的操作，或者当前线程需要处理其他任务的场景。
 *
 * @author: jiaolong
 * @date: 2024/07/03 13:53
 **/
public class CompletableFutureSeriesTest {

    public static void main(String[] args) throws Exception {
       seriesExecMethodTest();

     //thenCompose-串行执行解决的问题
   // thenComposeTest();
    }

    /**
     * 串行方法使用案例
     * @throws Exception
     */
    public static void seriesExecMethodTest() throws Exception {
        //可以设置Void类型
        Function<String, String> test = val ->{
            System.out.println("thenApply: Function interface, val:"+val);
            sleep(10, TimeUnit.SECONDS);
            return "return_aa";
        };

        String x ="no...";
        CompletableFuture<?> result = CompletableFuture.supplyAsync(()->{
            System.out.println("T1: 洗水壶..."+x);
            sleep(10, TimeUnit.SECONDS);
            return "f1";

        }).thenApply(
                test
        ).thenAccept(a -> {
            System.out.println("thenAccept: Consumer interface, accept:"+a);

        }).thenAccept(a -> {
            System.out.println("thenAccept: Consumer interface, accept:"+a);

        }).thenRun(() -> {
            System.out.println("thenRun: Runnable interface");

        }).thenCompose(z->{
            return CompletableFuture.supplyAsync(()->{
                System.out.println("thenCompose: function, new CompletableFuture");
                return "new CompletableFuture";
            });
        });

       System.out.println("返回结果："+result.get());
    }

    public static void thenComposeTest() throws Exception {
        /**
         * thenApply返回 CompletableFuture<?>类型的结果
         * thenCompose() 直接 function函数的结果，用于简化返回结果
         */

        CompletableFutureSeriesTest ct = new CompletableFutureSeriesTest();

        //方式2
        CompletableFuture<CompletableFuture<Double>> result = ct.getUsersDetail("xxx")
                .thenApply(user -> ct.getCreditRating(user));
        System.out.println(((CompletableFuture<Double>)result.get()).get());

        //替代方案
        CompletableFuture<Double> result1 = ct.getUsersDetail("xxx")
                .thenCompose(user -> {

                    System.out.println("获取上一次返回结果："+user);
                   return ct.getCreditRating(user);
                });
        System.out.println("获取最终计算结果："+result1.get());
    }

    private CompletableFuture<String> getUsersDetail(String userId) {
        String x = "";
        return CompletableFuture.supplyAsync(() ->"xx");
    }

    private CompletableFuture<Double> getCreditRating(String user) {
        return CompletableFuture.supplyAsync(() -> 10.2);
    }

    public static void sleep(int t, TimeUnit u) {
        try {
            u.sleep(t);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

