package thread.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * 优雅的停止线程案例
 * @author: jiaolong
 * @date: 2024/06/28 17:29
 **/
public class StopRunThreadExample {
    public static void main(String[] args) throws InterruptedException {
        //采集数据工具
        Proxy proxy =  new Proxy();
        //启动线程
        proxy.start();

        //优雅的停止线程
        TimeUnit.SECONDS.sleep(5);
        System.out.println("停止数据采集工作...");
        proxy.stop();
    }
}
class Proxy {
    // 线程终止标志位 必须是volatile保证可见性
    volatile boolean terminated = false;
    boolean started = false;
    // 采集线程
    Thread rptThread;

    // 启动采集功能
    synchronized void start(){
        // 不允许同时启动多个采集线程
        if (started) {
            return;
        }

        started = true;
        terminated = false;

        rptThread = new Thread(()->{
            while (!terminated){
                // 省略采集、回传实现
                System.out.println("数据采集...");

                // 每隔两秒钟采集、回传一次数据
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e){
                    // 重新设置线程中断状态
                    Thread.currentThread().interrupt();
                }
            }
            // 执行到此处说明线程马上终止
            started = false;
        });
        rptThread.start();
    }

    // 终止采集功能
    synchronized void stop(){
        // 设置中断标志位
        terminated = true;
        // 中断线程 rptThread
        rptThread.interrupt();
    }
}