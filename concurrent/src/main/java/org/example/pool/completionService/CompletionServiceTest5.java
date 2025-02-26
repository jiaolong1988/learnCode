package org.example.pool.completionService;

import java.util.concurrent.CompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 计算出最低报价并返回
 * @author jiaolong
 * @date 2023-07-10 02:36:57
 */
public class CompletionServiceTest5 {

    public static void main(String[] args) throws Exception {
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 创建 CompletionService
        CompletionService<Integer> cs = new ExecutorCompletionService<>(executor);
        // 异步向电商 S1 询价
        cs.submit(() -> getPriceByS1());
        // 异步向电商 S2 询价
        cs.submit(() -> getPriceByS2());
        // 异步向电商 S3 询价
        cs.submit(() -> getPriceByS3());

        // 将询价结果异步保存到数据库
        // 并计算最低报价
        AtomicReference<Integer> m = new AtomicReference<>(Integer.MAX_VALUE);
        CountDownLatch latch = new CountDownLatch(3);

        for (int i = 0; i < 3; i++) {
            executor.execute(() -> {
                Integer r = null;
                try {
                    r = cs.take().get();
                } catch (Exception e) {
                }
                save(r);
                m.set(Integer.min(m.get(), r));
                //倒计时门闩
                latch.countDown();
            });
        }
        //等待执行完毕
        latch.await();

        // 返回结果
        System.out.println("返回结果：" + m);
        executor.shutdown();

    }

    public static int getPriceByS1() {
        sleep(10);
        return 1;
    }

    public static int getPriceByS2() {
        sleep(15);
        return 2;
    }

    public static int getPriceByS3() {
        sleep(5);
        return 3;
    }

    public static void save(int r) {
        System.out.println("save " + r);
    }

    public static void sleep(int time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            // 此处中断异常忽略
        }
    }
}
