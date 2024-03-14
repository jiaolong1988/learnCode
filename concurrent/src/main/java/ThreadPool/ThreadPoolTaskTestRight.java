package ThreadPool;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author: jiaolong
 * @date: 2024/03/14 10:09
 **/
public class ThreadPoolTaskTestRight {

    private ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

    public void doService(){
        taskExecutor.setCorePoolSize(3);
        taskExecutor.initialize();

        exec();
    }

    public void exec(){
        taskExecutor.execute(new TestTaskExecutor());
        taskExecutor.execute(new TestTaskExecutor());
        taskExecutor.execute(new TestTaskExecutor());
    }

    public void close(){
        taskExecutor.shutdown();
    }

    public static void main(String[] args) {
        ThreadPoolTaskTestRight test = new ThreadPoolTaskTestRight();
        test.doService();
    }

}
