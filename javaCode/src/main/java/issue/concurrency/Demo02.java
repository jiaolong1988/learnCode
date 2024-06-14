package issue.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 网站访客统计Demo
 */
public class Demo02 {
    //网站总访问量
    volatile static int count = 0;				//加了volatile保证count变量对于所有线程来说是可见的
    public static  synchronized void request() throws InterruptedException {
        //耗时5毫秒
        TimeUnit.MILLISECONDS.sleep(5);
        count++;            //访问量++,这里count++并不是原子操作
    }
    public static void main(String[] args) throws InterruptedException {
        //开始时间
        long startTime = System.currentTimeMillis();
        //最大线程数，模拟100个线程同时访问
        int threadSize = 100;

        //设置初值为100，表示100个线程执行完后，countDownLatch.await()处的线程才可以继续执行
        CountDownLatch countDownLatch = new CountDownLatch(threadSize);

        for(int i = 0; i < threadSize; i++){
            new Thread(() -> {
                    try {
                        for(int j = 0; j < 10; j++){
                            request();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();             //countDownLatch--
                    }
            }).start();
        }
        countDownLatch.await();
        // 100个线程询问时间
        long endTime = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + ", 耗时:" + (endTime - startTime) + ", count:" + count);
    }
}
