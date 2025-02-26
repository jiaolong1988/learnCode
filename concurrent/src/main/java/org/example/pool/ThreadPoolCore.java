package org.example.pool;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 简化的线程池，用来说明工作原理
 *
 * @author jiaolong
 * @date 2023-06-29 01:54:59
 */
public class ThreadPoolCore {
    private static Logger logger = Logger.getLogger(ThreadPoolCore.class);
    public static void main(String[] args) throws InterruptedException {
        // 创建有界阻塞队列
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(2);
        // 创建线程池
        ThreadPoolCore pool = new ThreadPoolCore(3, workQueue);

        // 提交任务
        for (int i = 0; i <20 ; i++) {
            int x = i;
            pool.execute(()->{
                logger.info(Thread.currentThread().getName()+" hello"+x);
            });
        }


    }

    // 利用阻塞队列实现生产者 - 消费者模式
    BlockingQueue<Runnable> workQueue;
    // 保存内部工作线程
    List<WorkerThread> threads = new ArrayList<>();

    // 构造方法
    ThreadPoolCore(int poolSize, BlockingQueue<Runnable> workQueue){
        this.workQueue = workQueue;

        // 创建工作线程
        for(int idx=0; idx<poolSize; idx++){
            WorkerThread work = new WorkerThread();
            work.start();
            threads.add(work);
        }
    }

    // 提交任务
    void execute(Runnable command) throws InterruptedException{
        workQueue.put(command);
    }

    // 工作线程负责消费任务，并执行任务
    class WorkerThread extends Thread{
        public void run() {
            // 循环取任务并执行
            while(true){ //①
                try {
                    Runnable task = workQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    interrupt();
                    e.printStackTrace();
                }
            }
        }
    }




}

