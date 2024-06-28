package thread;

/**
 * 当调用第三方代码 没有处理中断异常时，Thread.isInterrupted(),是无法判断线程是否被总段。
 * @author: jiaolong
 * @date: 2024/06/28 16:26
 **/
public class InterruptThreadTest1 {
    public static void main(String[] args) {
        Thread t = new InterruptThreadTest1().getInstance();
        t.start();

        //模拟中断效果
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("---> 执行线程中断操作。。。");
        t.interrupt();
    }

    public InterruptThread getInstance(){
        return new InterruptThreadTest1.InterruptThread();
    }

   private class InterruptThread extends Thread {

        @Override
        public void run() {
            while (!isInterrupted()) {
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
        }

    }
}
