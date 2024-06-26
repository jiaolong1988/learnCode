package thread;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

/**
 * 每个线程都有一个本地的变量
 * @author: jiaolong
 * @date: 2024/06/24 14:53
 **/
public class ThreadLocalTest2 {
    static final AtomicLong nextId=new AtomicLong(0);
    // 定义 ThreadLocal 变量
    static final ThreadLocal<Long> tl = ThreadLocal.withInitial(
            ()->nextId.getAndIncrement()
    );


    public static void main(String[] args) {
        new MyT(tl).start();
        new MyT(tl).start();
        new MyT(tl).start();

        Predicate p;
    }
}
class MyT extends Thread {
    ThreadLocal<Long> threadLocal;

    public MyT(ThreadLocal<Long> tl) {
        this.threadLocal = tl;
    }

    public void run() {
        // 循环10次
        for (int i = 0; i < 10; i++) {
            try {
                long id = threadLocal.get();
                System.out.println(Thread.currentThread().getName()+" --> id:"+id);

                //返回hashCode值
                //System.out.println(Thread.currentThread().getName()+" --> "+System.identityHashCode(threadLocal.get()));
            } finally {
                //防止内存溢出
                //threadLocal.remove();
            }

        }
    }
}