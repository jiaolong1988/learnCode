package io.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;


/**
 * @author: jiaolong
 * @date: 2024/08/28 17:00
 **/
public class FileChannelTest {
    public static void main(String[] args) throws Exception {


        String sourcePath = "D:\\a.txt"; // 源文件路径
        String destPath = "destination.txt"; // 目标文件路径

        try (FileInputStream fis = new FileInputStream(sourcePath);
             FileOutputStream fos = new FileOutputStream(destPath);
             FileChannel sourceChannel = fis.getChannel();
             FileChannel destChannel = fos.getChannel()) {

            // 传输数据
            long position = 0;
            long size = sourceChannel.size();
            long transferred = 0;


            while (transferred < size) {
                transferred += sourceChannel.transferTo(position + transferred, size - transferred, destChannel);
            }

            System.out.println("文件复制完成！");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
