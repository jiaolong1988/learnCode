package org.example.lock;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
  * 读写锁原理测试
     1.允许多个线程同时读共享变量；
     2.只允许一个线程写共享变量；
     3.如果一个写线程正在执行写操作，此时禁止读线程读共享变量。

     本案例在读数据的时候，是不允许 执行写操作的，只有当读操作执行完毕后，才能执行写操作。
     从而验证 读锁与写锁是互斥的。
 * @author jiaolong
 * @date 2025/02/26 11:56
 **/
public class ReadWriteLockTest3CoreTest {
    private static Logger logger = Logger.getLogger(ReadWriteLockTest3CoreTest.class);
    public static void main(String[] args) {
        Cache<String,String> cache = new Cache<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");

        for(int i=0;i<3;i++) {
            //线程始终读取缓存数据
            Thread t = new Thread(()->{
                while(true) {
                    String data = cache.get("A");

                    String date = formatter.format(System.currentTimeMillis());
                    String info = "线程"+Thread.currentThread().getName()+"-读取的内容 "+date+" "+data;
                    logger.info(info);
                }

            });
            t.setName(i+"");
            t.start();
        }

        cache.writeSleep(2);

        new Thread(()->{
            int i=1;
            while(true) {
                cache.put("A", "hello"+i);
                cache.writeSleep(5);
                i++;
            }
        }).start();
    }

  static class Cache<K, V> {
        final Map<K, V> m = new HashMap<>();
        final ReadWriteLock rwl = new ReentrantReadWriteLock();
        // 读锁
        final Lock r = rwl.readLock();
        // 写锁
        final Lock w = rwl.writeLock();

        // 读缓存
        V get(K key) {
            r.lock();
            try {
                V v = m.get(key);
                String tname = Thread.currentThread().getName();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");
                String date = formatter.format(System.currentTimeMillis());
                logger.info(tname+"-开始读数据 "+date);

                if(!tname.equals("1")) {
                    logger.info("线程"+tname+" 等待10秒 ");
                    writeSleep(10);
                }else {
                    logger.info("线程1 等待8秒 ");
                    writeSleep(8);
                }

                return v;
            } finally {
                r.unlock();
            }
        }

        // 写缓存
        V put(K key, V v) {
            w.lock();
            try {

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");
                String date = formatter.format(System.currentTimeMillis());
                logger.info("\n"+Thread.currentThread().getName()+"-开始[写]数据 "+date);

                V x = m.put(key, v);
                return x;
            } finally {
                w.unlock();
            }
        }

        public void writeSleep(int val) {
            try {
                Thread.sleep(1000*val);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
