package ThreadPool.spring;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 没有继承Thread
 * @author: jiaolong
 * @date: 2024/03/14 10:09
 **/
public class ThreadPoolTaskTestRight {

    private ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

    public ThreadPoolTaskTestRight(){
        taskExecutor.setCorePoolSize(3);
        taskExecutor.initialize();

    }
    public void doService(){
        exec();
    }

    public void exec(){
        taskExecutor.execute(new TaskExecutor());
        taskExecutor.execute(new TaskExecutor());
        taskExecutor.execute(new TaskExecutor());
    }

    public void close(){
        //停止线程池
        taskExecutor.shutdown();
    }

    public static void main(String[] args) {
        ThreadPoolTaskTestRight test = new ThreadPoolTaskTestRight();
        test.doService();
    }

}
