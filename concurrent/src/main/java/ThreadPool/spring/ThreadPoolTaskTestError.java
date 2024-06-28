package ThreadPool.spring;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *  继承了Thread
 * @author: jiaolong
 * @date: 2024/03/13 16:48
 **/
public class ThreadPoolTaskTestError extends Thread{

    private ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

    public ThreadPoolTaskTestError(){
        taskExecutor.setCorePoolSize(3);
        taskExecutor.initialize();
    }
    public void doService(){
        //启动线程
        //第二次执行 Exception in thread "main" java.lang.IllegalThreadStateException
        this.start();
    }

    @Override
    public void run() {
        taskExecutor.execute(new TaskExecutor());
        taskExecutor.execute(new TaskExecutor());
        taskExecutor.execute(new TaskExecutor());

    }

    public void close(){
        taskExecutor.shutdown();
    }
}
