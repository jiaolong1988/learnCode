package thread;


import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 1. 初始(NEW)：新创建了一个线程对象，但还没有调用start()方法。
 * 2. 运行(RUNNABLE)：Java线程中将就绪（ready）和运行中（running）两种状态笼统的称为“运行”。
 * 线程对象创建后，其他线程(比如main线程）调用了该对象的start()方法。该状态的线程位于可运行线程池中，等待被线程调度选中，获取CPU的使用权，此时处于就绪状态（ready）。就绪状态的线程在获得CPU时间片后变为运行中状态（running）。
 * 3. 阻塞(BLOCKED)：表示线程阻塞于锁。
 * 4. 等待(WAITING)：进入该状态的线程需要等待其他线程做出一些特定动作（通知或中断）。
 * 5. 超时等待(TIMED_WAITING)：该状态不同于WAITING，它可以在指定的时间后自行返回。
 * 6. 终止(TERMINATED)：表示该线程已经执行完毕。
 * 原文链接：https://blog.csdn.net/pange1991/article/details/53860651
 *
 * @author: jiaolong
 * @date: 2024/06/26 11:43
 **/
public class ThreadStateTest {
    public static void main(String[] args) throws InterruptedException {
        //testNew();        // 0
        //testRunnable();   //2
        //testBlocked();    //5
        //testWaiting();      //401
        //testTimedWaiting();
        //testTerminated();
    }

    public static void testNew() {
        //NEW
        Thread thread = new Thread(() -> {
        });
        System.out.println(thread.getState());
    }

    public static void testRunnable() {
        //1.线程RUNNABLE
        Thread thread = new Thread(() -> {
        });
        thread.start();
        System.out.println(thread.getState());
    }

    public static void testBlocked() throws InterruptedException {
        //2.BLOCKED
        Thread t1 = new Thread(new DemoThreadB());
        Thread t2 = new Thread(new DemoThreadB());
        t1.start();
        t2.start();

        Thread.sleep(1000);
        System.out.println((t2.getState()));
        System.exit(0);
    }

    /**
     * WAITING-无时间参数等待
     *
     * @return: void
     **/
    public static void testWaiting() throws InterruptedException {
        //3.WAITING-无时间参数等待
        Thread main = Thread.currentThread();

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("mainThread Status: " + main.getState());
        });

        thread2.start();
        //等待线程2任务完成，此时的main主线程为WAITING
        thread2.join();
    }


    public static void testTimedWaiting() throws InterruptedException {
        //4.TIMED_WAITING
        Thread thread3 = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                //	Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });
        thread3.start();

        Thread.sleep(1000);
        System.out.println(thread3.getState());
    }

    public static void testTerminated() throws InterruptedException {
        //1.线程TERMINATED
        Thread thread = new Thread(() -> {
        });
        thread.start();
        Thread.sleep(1000);//TERMINATED
        System.out.println(thread.getState());
    }
}


class DemoThreadB implements Runnable {
    @Override
    public void run() {
        commonResource();
    }

    public static synchronized void commonResource() {
        while (true) {

        }
    }
}
