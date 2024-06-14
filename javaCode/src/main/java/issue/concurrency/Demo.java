package issue.concurrency;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Demo {
    // 网站总访问量：volatile保证线程可见性,便于在下面逻辑中 -> 保证多线程之间每次获取到的count是最新值
    volatile static int count = 0;

    // 模拟访问的方法
    public static void request() throws InterruptedException {
        // 模拟耗时5毫秒
        TimeUnit.MILLISECONDS.sleep(5);

        //count ++;

        int expectCount; // 表示期望值
        // 比较并交换
        while (!compareAndSwap((expectCount = getCount()), expectCount + 1)) {
        	 System.out.println(Thread.currentThread().getName()+" wait");
 
        }
    }

    /**
     * 比较并交换
     *
     * @param expectCount 期望值count
     * @param newCount    需要给count赋值的新值
     * @return 成功返回 true 失败返回false
     */
    public static synchronized boolean compareAndSwap(int expectCount, int newCount) {
        // 判断count当前值是否和期望值expectCount一致，如果一致 将newCount赋值给count
        if (getCount() == expectCount) {
            count = newCount;
            return true;
        }
        return false;
    }

    public static int getCount() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        // 开始时间
        long startTime = System.currentTimeMillis();
        int threadSize = 100;
        CountDownLatch countDownLatch = new CountDownLatch(threadSize);


        for (int i = 0; i < threadSize; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    // 模拟用户行为，每个用户访问10次网站
                    try {
                        for (int j = 0; j < 10; j++) {
                            request();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
           

            thread.start();
        }
        // 保证100个线程 结束之后，再执行后面代码
        countDownLatch.await();
        long endTime = System.currentTimeMillis();

        System.out.println(Thread.currentThread().getName() + ",耗时：" + (endTime - startTime) + ", count = " + count);
    }
}
