package org.example.pool.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CompletableFutureTest4Other {

    public static CompletableFuture<Void> f1 = null;
    public static CompletableFuture<String> f2 = null;

    public static void main(String[] args) throws Exception {

        create();

        /**
         * get与join的区别
         * 	get：获取结果，必须try catch进行异常处理。
         *  join: 等待线程完成，获取处理结果，不进行强制异常处理。
         */

        //取消任务-指定返回结果
        completeTest();

        CompletableFuture<?> r = CompletableFuture.allOf(f1,f2);
        r.get();

        System.out.println("----");
        CompletableFuture<?> r1 = CompletableFuture.anyOf(f1,f2);
        r1.get();


    }
    private static void create() {
        // 任务 1：洗水壶
        f1 = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("F1: 洗水壶执行完毕...");
        });

        // 任务 2：洗茶壶
        f2 = CompletableFuture.supplyAsync(() -> {

            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("F2: 洗茶壶执行完毕...");

            return "ok";
        });

    }

    /**
     * 终止任务，并指定返回结果
     */
    public static void completeTest() {

        //1.阻塞方式获取任务结果
        new Thread(()->{
            try {
                String result = f2.get();
                System.out.println("返回结果为： -> "+result);

            } catch (InterruptedException e) {
                System.out.println("0000 ");
                e.printStackTrace();
            } catch (ExecutionException e) {
                System.out.println("1111 ");
                e.printStackTrace();
            }
        }).start();;

        //2.中途中断任务并返回 指定结果
        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("中断任务，并设置返回结果。");
            f2.complete("异常重新执行");
        }).start();;
    }

}