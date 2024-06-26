package org.example.locak;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 非重入锁：自旋锁就
 * 重入锁：线程 可以重复 获取同一把锁。如：synchronized、ReentrantLock
 * @author: jiaolong
 * @date: 2024/06/25 17:11
 **/

class ReentrantLockTest {
    private final Lock rtl = new ReentrantLock();
    int value;


    public  int get() {
        // 获取锁
        rtl.lock();        // ②
        try {
            return value;
        } finally {
            // 保证锁能释放
            rtl.unlock();
        }
    }
    public void addOne() {
        // 获取锁
        rtl.lock();;
        try {
            value = 1 + get();// ①进入get方法后，有一次获取锁
            System.out.println(Thread.currentThread().getName()+"--"+value);

        } finally {
            // 保证锁能释放
            rtl.unlock();
        }
    }

    public static void main(String[] args) throws Exception {
        ReentrantLockTest x = new ReentrantLockTest();
        for (int i = 0; i < 2; i++) {
            new Thread(()->{

                for (int j = 0; j < 10; j++) {

                    x.addOne();
                }

            }).start();
        }
    }
}
