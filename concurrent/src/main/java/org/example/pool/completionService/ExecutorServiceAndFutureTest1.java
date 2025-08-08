package org.example.pool.completionService;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 1.引出问题
 * 需求：
 * 		用三个线程异步执行询价，通过三次调用 Future 的 get() 方法获取询价结果，之后将询价结果保存在数据库中.
 * 注意：
 * 		那就是如果获取电商 S1 报价的耗时很长，那么即便获取电商 S2 报价的耗时很短，也无法让保存 S2 报价的操作先执行，因为这个主线程都阻塞在了 f1.get() 操作上。
 * @author jiaolong
 * @date 2023-07-10 02:40:45
 */
public class ExecutorServiceAndFutureTest1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);
        // 异步向电商 S1 询价
        Future<Integer> f1 = executor.submit(() -> getPriceByS1());
        // 异步向电商 S2 询价
        Future<Integer> f2 = executor.submit(() -> getPriceByS2());
        // 异步向电商 S3 询价
        Future<Integer> f3 = executor.submit(() -> getPriceByS3());

        // 获取电商 S1 报价并保存
        Integer r1 = f1.get();
        executor.execute(() -> save(r1));

        // 获取电商 S2 报价并保存
        Integer r2 = f2.get();
        executor.execute(() -> save(r2));

        // 获取电商 S3 报价并保存
        Integer r3 = f3.get();
        executor.execute(() -> save(r3));
    }

    public static int getPriceByS1() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }
    public static int getPriceByS2() {
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 2;
    }
    public static int getPriceByS3() {
        return 3;
    }

    public static void save(int r) {
        System.out.println("save "+ r);
    }
}