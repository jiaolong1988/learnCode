package ThreadPool.spring;

import java.util.concurrent.TimeUnit;

/**
 * @author: jiaolong
 * @date: 2024/03/13 16:51
 **/
public class TestSpringThreadPool {
    public static void main(String[] args) throws InterruptedException {

        //非法参数
        IllegalThreadStateErrorTest();

    //    threadPoolShutdown();
    }

    private static void threadPoolShutdown() throws InterruptedException {
        //不继承Thread 线程
        ThreadPoolTaskTestRight t=  new ThreadPoolTaskTestRight();

        t.doService();
        TimeUnit.SECONDS.sleep(10);

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

        //这是一个线程类，多次start 会出错
        ThreadPoolTaskTestError t=  new ThreadPoolTaskTestError();

        t.doService();
        TimeUnit.SECONDS.sleep(10);
        t.close();

        System.out.println("---> 第一次执行 doService 完毕。 线程状态："+ t.getState());

        //第二次执行 Exception in thread "main" java.lang.IllegalThreadStateException
        t.doService();
        TimeUnit.SECONDS.sleep(10);

        t.close();
    }

}
