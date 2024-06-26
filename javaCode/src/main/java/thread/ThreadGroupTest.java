package thread;

import java.util.concurrent.TimeUnit;

/**
 * 线程组主要对线程进行分类管理，例如同一组的线程同时中断、同时为守护线程
 * @author jiaolong
 * @date 2024-6-26 11:10
 */
public class ThreadGroupTest {
    public static void main(String[] args) throws Exception {
        // 获取主线程所在的线程组，这是所有线程默认的线程组
        ThreadGroup mainGroup = Thread.currentThread().getThreadGroup();
        System.out.println("-->主线程组的名字：" + mainGroup.getName());
        System.out.println("-->主线程组是否是后台线程组："  + mainGroup.isDaemon());
        new MyThread("主线程组的线程").start();


        //线程组设置
        ThreadGroup tg = new ThreadGroup("新线程组");
        tg.setDaemon(true);
        System.out.println("-->tg线程组是否是后台线程组："  + tg.isDaemon());

        //线程测试
        new MyThread(tg, "tg组的线程甲").start();
        new MyThread(tg, "tg组的线程乙").start();

        TimeUnit.SECONDS.sleep(3);
        System.out.println("-----> 停止线程组");

        //中断tg线程组中的所有线程
        tg.interrupt();

        TimeUnit.SECONDS.sleep(1);
        System.exit(0);
    }
}

class MyThread extends Thread {
    // 提供指定线程名的构造器
    public MyThread(String name) {
        super(name);
    }

    // 提供指定线程名、线程组的构造器
    public MyThread(ThreadGroup group, String name) {
        super(group, name);
    }

    public void run() {
        int i=0;
        while(true){
            if (isInterrupted()) {
                System.out.println(getName() + "===》线程中断，程序退出。");
                break;
            }
            i++;
        }
    }
}