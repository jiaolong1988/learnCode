package thread;

import java.util.concurrent.TimeUnit;

/**
 * 多次调用start 抛出 IllegalThreadStateException。原因是 线程状态是不是 0.
 * @author: jiaolong
 * @date: 2024/06/28 11:50
 **/
public class IllegalThreadStateExceptionTest  {
    public static void main(String[] args) throws Exception {
        Thread t =  new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
        TimeUnit.SECONDS.sleep(1);

        //多次调用start 抛出 IllegalThreadStateException。原因是 线程状态是不是 0.
        t.start();
    }
}
