package org.example.pool.create;

/**
 * @author jiaolong
 * @date 2025/08/08 11:03
 **/
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolExecutorTest {
    public static void main(String[] args) {
        // 自定义线程工厂（带命名，方便排查问题）
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger threadNum = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("CustomThreadPool-Thread-" + threadNum.getAndIncrement());
                t.setDaemon(false); // 非守护线程（避免JVM提前退出）
                return t;
            }
        };

        // 自定义拒绝策略（记录日志+重试）
        RejectedExecutionHandler handler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.err.println("任务被拒绝，当前线程池状态：" + executor.toString());
                // 可在此处实现重试逻辑（如延迟后重新提交）
            }
        };


        /**
         * 任务队列（workQueue）
             * 有界队列（如ArrayBlockingQueue）：推荐，防止任务无限堆积导致OOM。
             * 无界队列（如LinkedBlockingQueue）：仅适用于任务量可控、绝对不会溢出的场景（极少见）。
             * 同步队列（SynchronousQueue）：适用于短任务、高并发场景（如newCachedThreadPool），但需严格控制maximumPoolSize。

         * 拒绝策略（handler）
             * AbortPolicy（默认）：抛出RejectedExecutionException，需上层捕获处理。
             * CallerRunsPolicy：由调用者线程执行任务（可降低提交速率）。
             * DiscardPolicy：静默丢弃新任务（适用于允许丢失部分任务的场景）。
             * DiscardOldestPolicy：丢弃队列中最旧的任务（适用于对时效性要求高的场景）。
         * **/
        // 创建线程池（关键参数根据业务场景调整）
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                4,                          // 核心线程数（建议=CPU核心数）
                8,                      // 最大线程数（核心*2，根据任务类型调整）
                30,                        // 非核心线程空闲存活时间
                TimeUnit.SECONDS,                       // 存活时间单位
                new ArrayBlockingQueue<>(100),  // 任务队列（存放待执行任务）[有界队列（防止内存溢出）]
                threadFactory,                          // 自定义线程工厂[线程工厂（自定义线程创建）]
                handler                                 // 拒绝策略（任务无法处理时的策略）
        );


        // 使用线程池提交任务
        for (int i = 0; i < 200; i++) {
            int taskId = i;
            executor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + " 执行任务 " + taskId);
                try {
                    Thread.sleep(100); // 模拟任务耗时
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 恢复中断状态
                }
            });
        }

        // 关闭线程池（重要！避免资源泄漏）
        executor.shutdown();
        try {
            // 等待线程池终止
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                executor.shutdownNow(); // 强制关闭（可能中断正在执行的任务）
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
