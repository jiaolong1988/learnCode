package io.nio.channel;

import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.Pipe;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Random;

/**
 * pipe sink    写数据
 *      source  读数据
 * 管道有点像是 队列。
 *
 * 管道就是一个用来 在两个实体之间单向传输数据的 导管。创建的管道只能在进程内（在 Java 虚拟机进程内部）使用。
 * 唯一可保证的是写到SinkChannel 中的字节都能按照同样的顺序在 SourceChannel 上重现。
 * 管道的好处在于封装性。当效率不是很高。
 *
 * Pipe 实例是通过调用不带参数的 Pipe.open( )工厂方法来创建的。Pipe 类定义了两个嵌套的通道类来实现管路。
 * 这两个类是 Pipe.SourceChannel（管道负责读的一端）和 Pipe.SinkChannel（管道负责写的一端）。
 * 这两个通道实例是在 Pipe 对象创建的同时被创建的，可以通过在 Pipe 对象上分别调用 source( )和 sink( )方法来取回
 *
 * @author: jiaolong
 * @date: 2024/08/30 11:40
 **/
public class PipeTest {
    public static void main(String[] argv) throws Exception {
// Wrap a channel around stdout
        WritableByteChannel out = Channels.newChannel(System.out);
// Start worker and get read end of channel
        ReadableByteChannel workerChannel = startWorker(10);
        ByteBuffer buffer = ByteBuffer.allocate(100);
        while (workerChannel.read(buffer) >= 0) {
            buffer.flip();
            out.write(buffer);
            buffer.clear();
        }
    }

    // This method could return a SocketChannel or
// FileChannel instance just as easily
    private static ReadableByteChannel startWorker(int reps)
            throws Exception {
        Pipe pipe = Pipe.open();
        Worker worker = new Worker(pipe.sink(), reps);
        worker.start();
        return (pipe.source());
    }
// -----------------------------------------------------------------

    /**
     * A worker thread object which writes data down a channel.
     * Note: this object knows nothing about Pipe, uses only a
     * generic WritableByteChannel.
     *
     * 一个 worker thread 对象，它将数据写入通道。注意：这个对象对 Pipe 一无所知，
     * 只使用一个通用的 WritableByteChannel。
     */
    private static class Worker extends Thread {
        WritableByteChannel channel;
        private int reps;

        Worker(WritableByteChannel channel, int reps) {
            this.channel = channel;
            this.reps = reps;
        }

        // Thread execution begins here
        public void run() {
            ByteBuffer buffer = ByteBuffer.allocate(100);
            try {
                for (int i = 0; i < this.reps; i++) {
                    doSomeWork(buffer);
// channel may not take it all at once
                    while (channel.write(buffer) > 0) {
// empty
                    }
                    this.sleep(1000); // simulate some work
                }
                this.channel.close();
            } catch (Exception e) {
// easy way out; this is demo code
                e.printStackTrace();
            }
        }

        private String[] products = {
                "No good deed goes unpunished",
                "To be, or what?",
                "No matter where you go, there you are",
                "Just say \"Yo\"",
                "My karma ran over my dogma"
        };
        private Random rand = new Random();

        private void doSomeWork(ByteBuffer buffer) {
            int product = rand.nextInt(products.length);
            buffer.clear();
            buffer.put(products[product].getBytes());
            buffer.put("\r\n".getBytes());
            buffer.flip();
        }
    }
}