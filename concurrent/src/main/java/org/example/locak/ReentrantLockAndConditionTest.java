package org.example.locak;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 重入锁案例: 同一线程 可以重复 获取同一把锁。
 * 锁与同步
 * @author jiaolong
 * @date 2023-06-09 03:10:46
 */
public class ReentrantLockAndConditionTest {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLockAndConditionTest bq = new ReentrantLockAndConditionTest();
        Thread t1 =	new Thread(()->{
            bq.enq();
        });
        t1.start();

        Thread.sleep(1000*5);
        System.out.println("\n等待5秒\n");

        new Thread(()->{
            bq.deq();
        }).start();
    }

    //true：队列已满 false:队列已空
    private boolean isCheck = true;
    //可重入锁：既线程 可以重复 获取同一把锁。
    final Lock lock = new ReentrantLock();
    // 条件变量：检查队列是否已 满
    final Condition checkFull = lock.newCondition();
    // 条件变量：检查队列是否为 空
    final Condition checkEmpty = lock.newCondition();

    // 入队
    void enq() {
        String tname = Thread.currentThread().getName()+" ";

        System.out.println(tname+"尝试获得锁");
        lock.lock();
        System.out.println(tname+"已获取锁");

        try {
            //队列已满，无法入队
            while (isCheck) {
                System.out.println(tname+"队列已满，进入条件等待队列。");
                // 等待队列不满
                checkFull.await(); //使当前线程等待，直到它发出信号或 中断。与此 Condition 相关的锁将以原子方式释放，
            }

            System.out.println(tname+"执行入队操作ok");
            // 省略入队操作...

            // 入队后, 通知可出队
            checkEmpty.signal(); //唤醒Condition等待线程，必须重新获取锁，然后才能 从await返回。

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println(tname+"enq unlock");
        }
    }

    // 出队
    void deq() {
        lock.lock();

        String tname = Thread.currentThread().getName()+" ";
        System.out.println(tname+"出队获取锁ok");
        try {
            //队列已空，无法出队
            while (!isCheck) {
                System.out.println(tname+"队列已空，进入条件等待队列。");
                // 等待队列不空
                checkEmpty.await();
            }

            System.out.println(tname+"执行出队操作ok");
            // 省略出队操作...

            // 出队后，通知可入队
            checkFull.signal();
            isCheck = false;
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
            System.out.println(tname+"deq unlock");
        }
    }



}
