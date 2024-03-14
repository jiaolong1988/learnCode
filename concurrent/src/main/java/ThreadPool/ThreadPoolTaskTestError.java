package ThreadPool;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author: jiaolong
 * @date: 2024/03/13 16:48
 **/
public class ThreadPoolTaskTestError extends Thread{

    private ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

    public void doService(){
        taskExecutor.setCorePoolSize(3);
        taskExecutor.initialize();

        this.start();
    }

    @Override
    public void run() {
        taskExecutor.execute(new TestTaskExecutor());
        System.out.println("=========");
        taskExecutor.execute(new TestTaskExecutor());
        System.out.println("=========");
        taskExecutor.execute(new TestTaskExecutor());

    }

    public void close(){
        taskExecutor.shutdown();
    }
}
