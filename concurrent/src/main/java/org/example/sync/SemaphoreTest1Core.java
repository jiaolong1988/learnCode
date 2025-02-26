package org.example.sync;

import org.apache.log4j.Logger;

import java.util.concurrent.Semaphore;

/**
 * 它通过控制一定数量的允许（permit）的方式，来达到限制通用资源访问的目的。
 *
 * 目标：模拟如下场景，在车站、机场等出租车时，当很多空出租车就位时，为防止过度拥挤，
 * 调度员指挥排队等待坐车的队伍一次进来 5 个人上车，等这 5 个人坐车出发，再放进去下一批，
 * 这和 Semaphore 的工作原理有些类似。
 *
 * 符合要求，此种用法为不推荐用法，存在弊端，只用于演示
 * @author jiaolong
 * @date 2025/02/26 14:16
 **/
public class SemaphoreTest1Core {
    private static Logger logger = Logger.getLogger(SemaphoreTest1Core.class);
    public static void main(String[] args) throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        for (int i = 0; i < 10; i++) {
            Thread t = new Thread(new MyWorker(semaphore));
            t.start();
        }
        logger.info("Action...GO!");
        logger.info("1 semaphore.availablePermits(): "+semaphore.availablePermits());
        //释放给定数量的许可
        semaphore.release(5);
        logger.info("2 semaphore.availablePermits(): "+semaphore.availablePermits());

        //符合要求，此种用法为不推荐用法，存在弊端，只用于演示
        while (semaphore.availablePermits() != 0) {
            Thread.sleep(100L);
        }

        logger.info("\nAction...GO again!");
        logger.info("3-1 semaphore.availablePermits(): "+semaphore.availablePermits());
        //释放给定数量的许可
        semaphore.release(5);
        logger.info("3-2 semaphore.availablePermits(): "+semaphore.availablePermits());
    }

    static class MyWorker implements Runnable {
        private String name;
        private Semaphore semaphore;

        public MyWorker(Semaphore semaphore) {
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            try {
                //获取许可
                semaphore.acquire();
                log("acquired a permit!");
            } catch (InterruptedException e) {
                e.printStackTrace();
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



