package org.example.pool.completableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * CompletableFuture的对象创建。EXCEPTION处理、AND聚合、OR聚合方法是使用。
 * create: 创建任务
 runAsync()			创建 异步无参数 无返回结果线程任务
 supplyAsync()		创建 异步无参数 有返回结果 线程任务

 * And: 两个任务都完成后，在执行下一个任务
 * thenCombine()		有参数（第一个参数是第一个任务的返回结果，第二个参数是第二个任务的返回结果），有返回结果
 * thenAcceptBoth()		有参数（第一个参数是第一个任务的返回结果，第二个参数是第二个任务的返回结果），无返回结果
 * runAfterBoth()		无参数，无返回结果
 *
 * OR: 两个任务中只要有一个完成，就执行下一个任务
 * applyToEither()		有参数 有返回结果
 * acceptEither()		有参数 无返回结果
 * runAfterEither()		无参数 无返回结果

 * Exception:
 * handle();		相当于finally,无论有无异常都执行，执行get方法返回异常后的处理结果
 * whenComplete;	相当于finally,无论有无异常都执行，执行get方法会抛出异常,程序终止。
 * exceptionally   相当于catch,只有发生异常的时候才执行
 *
 * @author jiaolong
 * @date 2023-07-07 09:12:23
 */
public class CompletableFutureTest3Main {

    public static CompletableFuture<Void> f1 = null;
    public static CompletableFuture<String> f2 = null;

    public static void main(String[] args) throws Exception {
        create();

        System.out.println("\n");
        collectAnd();

        System.out.println("\n");
        collectOR();

        System.out.println("\n");
        collectException();

    }


    /**
     *create: 创建任务
     runAsync()			创建 异步无参数 无返回结果线程任务
     supplyAsync()		创建 异步无参数 有返回结果 线程任务
     */
    public static void create() {
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
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("F2: 洗茶壶执行完毕...");

            return "ok";
        });

    }

    /**
     * And: 两个任务都完成后，在执行下一个任务
     * thenCombine()		有参数（第一个参数是第一个任务的返回结果，第二个参数是第二个任务的返回结果），有返回结果
     * thenAcceptBoth()		有参数（第一个参数是第一个任务的返回结果，第二个参数是第二个任务的返回结果），无返回结果
     * runAfterBoth()		无参数，无返回结果
     *
     * @throws Exception
     */
    public static void collectAnd() throws Exception {
        CompletableFuture<?> res1 = f1.thenCombine(f2, (f,s) -> {
            System.out.println("	thenCombine f1返回结果："+f+" f2:返回结果"+s);
            return "x";
        });

        CompletableFuture<?> res2 = f1.thenAcceptBoth(f2, (f, s) -> {
            System.out.println("	thenAcceptBoth f1返回结果："+f+" f2:返回结果"+s);
        });

        CompletableFuture<?> res3 =f1.runAfterBoth(f2, ()->{
            System.out.println("	runAfterBoth f1 与f2 执行完毕后执行");
        });

        System.out.println("res1 返回结果："+res1.join());
        System.out.println("res2 返回结果："+res2.get());
        System.out.println("res3 返回结果："+res3.get());
    }


    /**
     * OR: 两个任务中只要有一个完成，就执行下一个任务
     * applyToEither()		有参数 有返回结果
     * acceptEither()		有参数 无返回结果
     * runAfterEither()		无参数 无返回结果
     * @throws Exception
     */
    public static void collectOR() throws Exception {

        CompletableFuture<String> res1 = f1.applyToEither(f1, (x) -> {
            System.out.println("	applyToEither ---> 参数：" + x);
            return "hello";
        });

        CompletableFuture<Void> res2 = f2.acceptEither(f2, (x) -> {
            System.out.println("	acceptEither ---> 参数：" + x);
        });

        CompletableFuture<Void> res3 = f1.runAfterEither(f2, () -> {
            System.out.println("	runAfterEither ---> 无参数");
        });


        System.out.println("res1 返回结果："+res1.join());
        System.out.println("res2 返回结果："+res2.get());
        System.out.println("res3 返回结果："+res3.get());
    }


    /**
     * 异常处理
     * handle();		相当于finally,无论有无异常都执行，执行get方法返回异常后的处理结果
     * whenComplete;	相当于finally,无论有无异常都执行，执行get方法会抛出异常,程序终止。
     * exceptionally    相当于catch,只有发生异常的时候才执行
     * @throws Exception
     */
    public static void collectException() throws Exception {
        CompletableFuture<Integer>  f0 = CompletableFuture.supplyAsync(()->7/1);

        CompletableFuture<Integer> res1 = f0.thenApply(r -> r / 0)
                //相当于finally,无论有无异常都执行，执行get方法返回异常后的处理结果
                .handle((result, e) -> {
                    System.out.println("	handle 参数result:"+result + " 参数e:" + e);
                    return 0;
                });


        CompletableFuture<Integer> res2 = f0.thenApply(r -> r / 0)
                //相当于finally,无论有无异常都执行，执行get方法会抛出异常,程序终止。
                .whenComplete((result, e)->{
                    System.out.println("	whenComplete 参数result:"+result + " 参数e:" + e);
                });


        CompletableFuture<Integer> res3 = f0.thenApply(r -> r / 0)
                //相当于catch,只有发生异常的时候才执行
                .exceptionally(e->{
                    System.out.println("exceptionally: "+e);
                    return 0;
                });

        System.out.println("res1 返回结果："+res1.join());
        System.out.println("res3 返回结果："+res3.get());
        System.out.println("res2 没有返回结果会直接抛出异常："+res2.join());

    }


}
