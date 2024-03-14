package ThreadPool;

import java.util.concurrent.TimeUnit;

/**
 * @author: jiaolong
 * @date: 2024/03/13 16:51
 **/
public class ThreadTest {
    public static void main(String[] args) throws InterruptedException {

        //非法参数
       // IllegalThreadStateErrorTest();

        threadPoolShutdown();
    }

    private static void threadPoolShutdown() throws InterruptedException {
        //不继承Thread 线程
        ThreadPoolTaskTestRight t=  new ThreadPoolTaskTestRight();

        t.doService();
        TimeUnit.SECONDS.sleep(10);
        t.close();

        System.out.println("wait......");

        t.doService();
        TimeUnit.SECONDS.sleep(10);

        t.close();
    }


    /**
     * 线程池 无法重复使用
     * 线程池无法退出。
     * @return: void
     **/
    private static void IllegalThreadStateErrorTest() throws InterruptedException {
        ThreadPoolTaskTestError t=  new ThreadPoolTaskTestError();

        t.doService();
        TimeUnit.SECONDS.sleep(10);
        t.close();

        System.out.println("wait......");

        //第二次执行 Exception in thread "main" java.lang.IllegalThreadStateException
        t.doService();
        TimeUnit.SECONDS.sleep(10);

        t.close();
    }

}
