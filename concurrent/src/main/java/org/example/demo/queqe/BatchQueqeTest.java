package org.example.demo.queqe;


import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 生产者-消费者模式，批量处理方式
 * @author: jiaolong
 * @date: 2024/07/01 10:11
 **/
public class BatchQueqeTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // 创建 FutureTask

        BatchQueqeTest batchQueqe = new BatchQueqeTest();
        batchQueqe.start();

        for (int i = 0; i < 100; i++) {
            batchQueqe.bq.put("data"+i);
        }


    }

    BlockingQueue<String> bq = new LinkedBlockingQueue<>(2000);

    // 启动 5 个消费者线程
    // 执行批量任务
    void start() {
        ExecutorService es = Executors.newFixedThreadPool(2);
        for (int i=0; i<5; i++) {
            es.execute(()->{
                try {
                    while (true) {
                        // 获取批量任务
                        List<String> ts = pollTasks();
                        // 执行批量任务
                        execTasks(ts);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    // 从任务队列中获取批量任务
    List<String> pollTasks()  throws InterruptedException{
        List<String> ts=new LinkedList<>();
        // 阻塞式获取一条任务
        String t = bq.take();
        while (t != null) {
            ts.add(t);
            // 非阻塞式获取一条任务
            t = bq.poll();
        }
        return ts;
    }

    // 批量执行任务
   void execTasks(List<String> ts) {

       StringBuilder sb = new StringBuilder("insert into ('aa') values (");
       for(String t :ts){
           sb.append(t+", ");
       }
       System.out.println(Thread.currentThread().getName()+" --> "+sb.toString());
    }
}
