package io.nio.channel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.io.FileOutputStream;
import java.util.Random;
import java.util.List;
import java.util.LinkedList;

/**
 * 多 ByteBuffer 写入channel
 * @author jiaolong
 * @date 2024-8-9 16:45
 */
public class GatheringByteChannelTest {
    private static final String DEMOGRAPHIC = "blahblah.txt";

    public static void main(String[] argv) throws Exception {

        FileOutputStream fos = new FileOutputStream(DEMOGRAPHIC);
        GatheringByteChannel gatherChannel = fos.getChannel();
        FileChannel x;

        int reps = 10;
        ByteBuffer[] bs = utterBS(reps);

        try {
            //判断写入的数据是否 大于 0
            while (gatherChannel.write(bs) > 0) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Mindshare paradigms synergized to "+ DEMOGRAPHIC);
        fos.close();
    }


    private static String[] col1 = {
            "Aggregate", "Enable", "Leverage",
            "Facilitate", "Synergize", "Repurpose",
            "Strategize", "Reinvent", "Harness"
    };
    private static String[] col2 = {
            "cross-platform", "best-of-breed", "frictionless",
            "ubiquitous", "extensible", "compelling",
            "mission-critical", "collaborative", "integrated"
    };
    private static String[] col3 = {
            "methodologies", "infomediaries", "platforms",
            "schemas", "mindshare", "paradigms",
            "functionalities", "web services", "infrastructures"
    };
    private static String newline = System.getProperty("line.separator");

    // 创建buffer数组
    private static ByteBuffer[] utterBS(int howMany) throws Exception {
        List list = new LinkedList();
        for (int i = 0; i < howMany; i++) {
            list.add(pickRandom(col1, " "));
            list.add(pickRandom(col2, " "));
            list.add(pickRandom(col3, newline));
        }
        ByteBuffer[] bufs = new ByteBuffer[list.size()];
        list.toArray(bufs);
        return (bufs);
    }

    //生成随机大小数据的 buffer
    private static Random rand = new Random();
    private static ByteBuffer pickRandom(String[] strings, String suffix)
            throws Exception {
        String string = strings[rand.nextInt(strings.length)];
        int total = string.length() + suffix.length();
        ByteBuffer buf = ByteBuffer.allocate(total);
        buf.put(string.getBytes("US-ASCII"));
        buf.put(suffix.getBytes("US-ASCII"));
        buf.flip();
        return (buf);
    }
}
