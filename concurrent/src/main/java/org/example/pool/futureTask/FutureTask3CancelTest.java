package org.example.pool.futureTask;

import java.util.concurrent.*;

/**
 * Future 接口中的 cancel方法 与 get方法必须在一起。
 *      cancel方法 必须在 get方法 之前执行。
 *
 * @author: jiaolong
 * @date: 2024/07/02 14:31
 **/
public class FutureTask3CancelTest {

    //内部类-状态控制
    static class ControlStop{
        private Future<String> future;
        private volatile boolean isStop ;

        ControlStop(Future<String> future, boolean isStop) {
            this.future = future;
            this.isStop = isStop;
        }
    }

    //内部类-控制线程的取消
    static class TaskCancel implements Runnable{
        private ControlStop cs;
        private TaskCancel(ControlStop cs){
            this.cs = cs;
        }

        @Override
        public void run() {
            Future<String> future = cs.future;

            //必须先执行 cancel方法
            while(!future.isDone()) {
                System.out.println("子线程任务还没有结束...");
                if(cs.isStop){
                    future.cancel(true);
                }
            }

            //在执行get方法
            if (!future.isCancelled()){
                System.out.println("子线程任务已完成");
                try {
                    String result = future.get();
                    System.out.println("子线程任务已完成-结果："+result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("子线程任务被取消");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        //1.向线程池 提交任务
        Callable<String> callable = () -> {
            System.out.println("进入 Callable 的 call 方法");

            Thread.sleep(500000);
            return "Hello from Callable";
        };

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        //方式1
        //FutureTask<String> future = way1(callable, executorService);
        //方式2
        Future<String> future = executorService.submit(callable);

        //2.启动一个行的线程 控制任务结束
        TaskCancel tc = new TaskCancel(new ControlStop(future, false));
        new Thread(tc).start();

        //3.五秒钟后，传入参数 停止任务
        TimeUnit.SECONDS.sleep(5);
        tc.cs.isStop=true;

        executorService.shutdown();
    }

    public static FutureTask<String> way1(Callable<String> callable, ExecutorService executorService) {
        FutureTask<String> futureTask = new FutureTask<>(callable);
        executorService.submit(futureTask);

        return futureTask;
    }


}

