package org.example.demo.queqe;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.*;

/**
 * 生产者消费者模式-类似实时处理
 * 利用生产者 - 消费者模式还支持分阶段提交的应用场景。我们知道写文件如果同步刷盘性能会很慢，
 *            所以对于不是很重要的数据，我们往往采用异步刷盘的方式。
 * 日志组件采用的异步刷盘方式，刷盘的时机是：
 *  1.ERROR 级别的日志需要立即刷盘；
 *  2.数据积累到 500 条需要立即刷盘；
 *  3.存在未刷盘数据，且 5 秒钟内未曾刷盘，需要立即刷盘。
 *
 * @author: jiaolong
 * @date: 2024/07/01 14:15
 **/
public class ProducerConsumerModel {
    public static void main(String[] args) throws IOException {
        Logger logger = new Logger();
        logger.start();

        logger.error("222");
        logger.info("111");
    }
}

class Logger {
    // 任务队列
    final BlockingQueue<LogMsg> bq  = new LinkedBlockingQueue<>();
    //flush 批量
    static final int batchSize=500;
    // 只需要一个线程写日志
    ExecutorService es = Executors.newFixedThreadPool(1);

    // 启动写日志线程
    void start() throws IOException {
        File file= new File("javaTest.log");
        final FileWriter writer= new FileWriter(file);
        this.es.execute(()->{
            try {
                // 未刷盘日志数量
                int curIdx = 0;
                long preFT=System.currentTimeMillis();
                while (true) {
                    LogMsg log = bq.poll(5, TimeUnit.SECONDS);
                    // 写日志
                    if (log != null) {
                        writer.write(log.toString());
                        ++curIdx;
                    }
                    // 如果不存在未刷盘数据，则无需刷盘
                    if (curIdx <= 0) {
                        continue;
                    }
                    // 根据规则刷盘
                    if (log!=null && log.level==LEVEL.ERROR ||
                            curIdx == batchSize ||
                            System.currentTimeMillis()-preFT>5000 ){
                        writer.flush();
                        curIdx = 0;
                        preFT=System.currentTimeMillis();
                        System.out.println("数据写入磁盘。。。"+ Calendar.getInstance().getTime());
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            } finally {
                try {
                    writer.flush();
                    writer.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        });
    }

    // 写 INFO 级别日志
    void info(String msg)  {
        try {
            bq.put(new LogMsg(LEVEL.INFO, msg));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // 写 ERROR 级别日志
    void error(String msg) {
        try {
            bq.put(new LogMsg( LEVEL.ERROR, msg));
        } catch (InterruptedException e) {
           Thread.currentThread().interrupt();
        }
    }
}

// 日志级别
enum LEVEL {
    INFO, ERROR
}

class LogMsg {
    LEVEL level;
    String msg;

    public LogMsg(LEVEL level, String msg) {
        this.level = level;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "LogMsg{" +
                "level=" + level +
                ", msg='" + msg + '\'' +
                '}';
    }
}