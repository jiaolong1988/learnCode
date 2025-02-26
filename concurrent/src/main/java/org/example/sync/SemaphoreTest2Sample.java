package org.example.sync;

import org.apache.log4j.Logger;

import java.util.concurrent.Semaphore;

/**
 * 经典信号量（Semaphore）的实现，它通过控制一定数量的允许（permit）的方式，来达到限制通用资源访问的目的。
 *
 * 目标：模拟如下场景，在车站、机场等出租车时，当很多空出租车就位时，为防止过度拥挤，
 * 调度员指挥排队等待坐车的队伍一次进来 5 个人上车，等这 5 个人坐车出发，再放进去下一批，
 *
 * Semaphore，相当实时计算，也就是一直有5个人可以试图乘车，
 * 如果有1个人出发了，立即就有排队的人获得许可。
 *
 * 相当于实时计算概念，也就是说一直有5个现在同时执行，执行相同的任务。
 * @author jiaolong
 * @date 2025/02/26 14:04
 **/
public class SemaphoreTest2Sample {
    private static Logger logger = Logger.getLogger(SemaphoreTest2Sample.class);

    public static void main(String[] args) throws InterruptedException {
       logger.info("Action...GO!");
        Semaphore semaphore = new Semaphore(5);
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(new SemaphoreWorker(semaphore));
            t.start();
        }
    }

    static class SemaphoreWorker implements Runnable {
        private String name;
        private Semaphore semaphore;

        public SemaphoreWorker(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                //获取许可
                semaphore.acquire();
                log("acquired a permit! "+semaphore.availablePermits());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //释放给定数量的许可
                semaphore.release();
            }
        }

        private void log(String msg) {
            if (name == null) {
                name = Thread.currentThread().getName();
            }
           logger.info(name + " " + msg);
        }
    }
}


