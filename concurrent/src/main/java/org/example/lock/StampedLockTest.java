package org.example.lock;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * 官方案例 读写锁中的性能之王
 * 参考文章: https://www.cnblogs.com/konck/p/9691538.html
     StampedLock 的功能仅仅是 ReadWriteLock 的子集
     StampedLock 不支持重入
 * @author jiaolong
 * @date 2025/02/26 11:59
 **/
public class StampedLockTest {
    public static void main(String[] args) {
        Point point = new Point();

        //写锁
        new Thread(()->{
            while(true)
                point.move(1, 2);
        }).start();

        //乐观读转换为悲观读锁
        new Thread(()->{
            while(true) {
                double res = point.distanceFromOrigin();
                System.out.println("result:"+res);
            }
        }).start();


        point.waitTime(3);

        //悲观读锁 及 乐观读 升级写锁的使用
        new Thread(()->{
            while(true) {
                point.moveIfAtOrigin(1,2);
            }
        }).start();

    }


    static class Point {
        private double x, y;
        //盖章锁
        private final StampedLock sl = new StampedLock();

        /**
         * 写锁的使用
         * @param deltaX
         * @param deltaY
         */
        void move(double deltaX, double deltaY) {
            //an exclusively locked method,写锁是一个独占锁,不可用时一直阻塞
            long stamp = sl.writeLock();	//获取写锁
            System.out.println("move-获得写锁："+printLog());
            try {
                x += deltaX;
                y += deltaY;
                waitTime(60);
            } finally {
                sl.unlockWrite(stamp);  	//释放写锁
            }
        }

        //乐观读的使用
        double distanceFromOrigin() {
            /**
             * 0表示独占锁，非独占锁会返回一个 标记值，用于 validate()方法的验证
             */
            long stamp = sl.tryOptimisticRead();     //获得一个乐观读
            double currentX = x, currentY = y;
            /**
             * 不是独占锁返回true,。否则返回false.
             */
            System.out.println("检查乐观读："+sl.validate(stamp));
            if (!sl.validate(stamp)) {				 //检查 乐观读后 是否有其他写锁发生，有独占锁 则返回false
                stamp = sl.readLock();				 //获取一个悲观读锁
                try {
                    currentX = x;
                    currentY = y;
                    System.out.println("获取读锁currentX:"+currentX+" currentY:"+currentY+printLog());
                    waitTime(10);
                } finally {
                    sl.unlockRead(stamp); 				//释放悲观读锁
                }
            }
            return currentX  + currentY;
        }

        //悲观读锁 及 乐观读 升级写锁的使用
        void moveIfAtOrigin(double newX, double newY) {

            long stamp = sl.readLock();							//悲观读锁
            try {
                while (x == 0.0 && y == 0.0) {
                    /**
                     * 如果锁定状态与给定标记匹配，则执行以下操作之一。
                     * 	1.如果图章表示持有写锁，则返回它。
                     *  2.如果读锁定，如果写锁定可用，则释放读锁定并返回写标记。
                     *  3.如果是乐观读取，则仅在立即可用时才返回写入标记。
                     *    此方法在所有其他情况下返回零。
                     */
                    long ws = sl.tryConvertToWriteLock(stamp);	//读锁转换为写锁
                    if (ws != 0L) {
                        System.out.println("moveIfAtOrigin-获得写锁"+printLog());
                        stamp = ws;							 	//票据更新,否则不会释放新锁

                        x = newX;
                        y = newY;
                        break;
                    } else {
                        System.out.println("没获得写锁-释放读锁"+printLog());
                        sl.unlockRead(stamp);					//转换失败 释放读锁
                        stamp = sl.writeLock();					//强制获取写锁
                    }
                }
            } finally {
                sl.unlock(stamp);								//释放所有锁
            }
        }

        public void waitTime(int s) {
            try {
                TimeUnit.SECONDS.sleep(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public String printLog() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
            return simpleDateFormat.format(System.currentTimeMillis());
        }

    }
}
