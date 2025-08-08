package org.example.pool.create;

import java.util.concurrent.*;

/**
 * Executors 创建线程池 的快捷方式，但推荐使用,也是使用ThreadPollExecutor方式创建
 * @author jiaolong
 * @date 2025/08/08 11:29
 **/
public class ExecutorServiceCreateThreadPollTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
      /**
       方法							      线程数量   是否支持定时任务	    适用场景
       newFixedThreadPool(n)				固定		❌				短期任务、CPU 密集型
       newSingleThreadExecutor()			1		❌				顺序执行任务
       newCachedThreadPool()				动态		❌				短生命周期任务
       newScheduledThreadPool(n)			固定		✅				定时/周期任务
       newWorkStealingPool()				动态		❌				并行计算任务
       newSingleThreadScheduledExecutor()	1		✅				单线程定时任务
       * **/

        // 创建一个固定大小的线程池（4个线程）
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // 提交多个任务
        for (int i = 0; i < 6; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " is running on thread: " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000); // 模拟耗时操作
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // 关闭线程池
        executor.shutdown();

        // 等待所有任务完成
        boolean allTasksFinished = executor.awaitTermination(5, TimeUnit.SECONDS);
        if (allTasksFinished) {
            System.out.println("All tasks have been completed.");
        } else {
            System.out.println("Some tasks are still running.");
        }
        }
}
