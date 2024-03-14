package ThreadPool;

import java.util.TimerTask;

/**
 * @author: jiaolong
 * @date: 2024/03/13 17:05
 **/
public class TestTaskExecutor extends TimerTask {

    @Override
    public void run() {
        System.out.println("111");

    }

}
