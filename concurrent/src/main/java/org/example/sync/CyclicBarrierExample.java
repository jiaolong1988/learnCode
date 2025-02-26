package org.example.sync;

import org.apache.log4j.Logger;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CyclicBarrier(循环屏障)
 *    简单使用方法
 * @author jiaolong
 * @date 2025-2-26 13:57
 */
public class CyclicBarrierExample {
   private static Logger logger = Logger.getLogger(CyclicBarrierExample.class);
   // 创建 CyclicBarrier 实例，计数器的值设置为2
   private static CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

   public static void main(String[] args) {
      ExecutorService executorService = Executors.newFixedThreadPool(2);
      int breakCount = 0;

     	// 将线程提交到线程池
      executorService.submit(() -> {
         try {
            System.out.println(Thread.currentThread() + "第一回合");
            Thread.sleep(1000);
            cyclicBarrier.await();

            System.out.println(Thread.currentThread() + "第二回合");
            Thread.sleep(2000);
            cyclicBarrier.await();

            System.out.println(Thread.currentThread() + "第三回合");
         } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
         } 
      });

      executorService.submit(() -> {
         try {
            System.out.println(Thread.currentThread() + "第一回合");
            Thread.sleep(2000);
            cyclicBarrier.await();

            System.out.println(Thread.currentThread() + "第二回合");
            Thread.sleep(1000);
            cyclicBarrier.await();

            System.out.println(Thread.currentThread() + "第三回合");
         } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
         }
      });

      executorService.shutdown();
   }

}