package io.nio.buffer;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

/**
 * @author: jiaolong
 * @date: 2024/08/09 11:24
 **/
public class ByteBufferTest {
    public static void main(String[] args) {
        test();


        ByteBuffer header = ByteBuffer.allocate (10);
        header.putShort((short) 1);
        header.putShort((short) 2);
        header.putShort((short) 3);
        header.putShort((short) 4);
        header.putShort((short) 5);

        //遍历buffer
        header.flip();
        while(header.hasRemaining()){
            System.out.println("position:"+header.position());
            System.out.println(header.getShort());
        }

    }

    private static void test() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
//        buffer.put((byte) 1);
//        buffer.put((byte) 2);
//        buffer.put((byte) 3);
//        buffer.put((byte) 4);
//        buffer.put((byte) 5);

        buffer.putShort((short) 1);
        buffer.putShort((short) 2);
        buffer.putShort((short) 3);
        buffer.putShort((short) 4);
        buffer.putShort((short) 5);

// 读取索引为2的字节
        short b = buffer.getShort(3);
        System.out.println(b); // 输出: 3
    }
}
