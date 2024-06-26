package thread;

/**
 * 自定义线程的异常信息
 * @author jiaolong
 * @date 2024-6-26 10:55
 */
public class ThreadDefinException {
    public static void main(String[] args) {
        // 设置主线程的异常处理器
        Thread.currentThread().setUncaughtExceptionHandler(new MyExHandler());
        int a = 5 / 0;     // ①
        System.out.println("程序正常结束！");
    }
}

// 定义自己的异常处理器
class MyExHandler implements Thread.UncaughtExceptionHandler {
    // 实现uncaughtException方法，该方法将处理线程的未处理异常
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println(t + " 线程出现了异常：" + e);
    }
}