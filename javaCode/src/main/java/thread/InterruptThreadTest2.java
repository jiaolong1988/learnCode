package thread;

/**
 * 增加标志位，用于控制线程的退出。
 * @author: jiaolong
 * @date: 2024/06/28 16:43
 **/
public class InterruptThreadTest2 {
    //线程停止标志位， 必须是volatile，以便保证可见性
    public volatile boolean terminated = false;

    public static void main(String[] args) {
        InterruptThreadTest2 it = new InterruptThreadTest2();

        Thread t = it.getInstance();
        t.start();

        //模拟中断效果
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("---> 执行线程中断操作。。。");
        it.terminated = true;
        t.interrupt();
    }

    public InterruptThread getInstance(){
        return new InterruptThreadTest2.InterruptThread();
    }

    class InterruptThread extends Thread {

        @Override
        public void run() {
            while (!terminated) {
                //模拟调用第三方库，不处理中断标志
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    System.out.println("Error 调用第三方库，没有处理中断标志位。。。。。。");
                    //isInterrupted()  方法始终返回false，导致线程无法中断。
                }

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    //当客户段调用interrupt()方法后，服务端只要有一个 sleep()抛出了InterruptedException，后续其他sleep都不会在处理中断异常了

                    //重新设置中断标志位
                    Thread.currentThread().interrupt();
                }

                System.out.println("正在执行任务。");
            }

            System.out.println("---> [线程执行完毕...]");
        }

    }
}

