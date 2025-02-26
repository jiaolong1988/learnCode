package org.example.sync;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

/**
 * Semaphore(信号量）是一种用于对共享资源访问的控制机制。
 *  快速实现一个限流器。
 *
 * @author jiaolong
 * @date 2025/02/26 14:10
 **/
public class SemaphoreTest3Example {
    private static Logger logger = Logger.getLogger(SemaphoreTest3Example.class);

    public static void main(String[] args) throws InterruptedException {
        // 创建对象池
        ObjPool<Long, String> pool = new ObjPool<Long, String>(10, 2L);

        // 通过对象池获取 t，之后执行
        String x = pool.exec(t -> {
           logger.info("对象池 执行业务逻辑"+t);
            return t.toString();
        });

       logger.info("返回结果："+x);
    }

   static class ObjPool<T, R> {
        final List<T> pool;
        // 用信号量实现限流器
        final Semaphore sem;

        // 构造函数
        ObjPool(int size, T t) {
            pool = new Vector<T>() {
            };
            for (int i = 0; i < size; i++) {
                pool.add(t);
            }
            sem = new Semaphore(size);
        }

        // 利用对象池的对象，调用 func
        R exec(Function<T, R> func) throws InterruptedException {
            T t = null;
            sem.acquire();
            try {
                t = pool.remove(0);
                return func.apply(t);
            } finally {
                pool.add(t);
                sem.release();
            }
        }


    }

}
