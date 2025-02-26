package org.example.lock;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *   读写锁原理测试
 * 	    1.允许多个线程同时读共享变量；
 * 	    2.只允许一个线程写共享变量；
 * 	    3.如果一个写线程正在执行写操作，此时禁止读线程读共享变量。
 * @author jiaolong
 * @date 2025/02/26 11:33
 **/
public class ReadWriteLockTest1Core {
    private static Logger logger = Logger.getLogger(ReadWriteLockTest1Core.class);
    public static void main(String[] args) {
        Cache<String,String> cache = new Cache<>();

        List<String> list = new ArrayList<>();
        for(int i=0;i<2;i++) {
            //线程始终读取缓存数据
            new Thread(()->{
                while(true) {
                    String info = Thread.currentThread().getName()+"-"+cache.get("A")+ Calendar.getInstance().getTime();
                    list.add(info);
                    try {
                        Files.write(Paths.get("writeInfo.txt"), list, Charset.forName("gbk"));
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    logger.info(info);
                }

            }).start();
        }

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
                return m.get(key);
            } finally {
                r.unlock();
            }
        }

        // 写缓存
        V put(K key, V v) {
            w.lock();
            try {
                V x = m.put(key, v);
                //写锁暂停5秒，读锁会进行阻塞，等待写锁执行完毕后，读锁才可读取数据。
                writeSleep(5);

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

