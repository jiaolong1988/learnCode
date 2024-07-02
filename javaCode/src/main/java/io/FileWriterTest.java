package io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author: jiaolong
 * @date: 2024/07/01 11:19
 **/
public class FileWriterTest {
    public static void main(String[] args) throws IOException {
        final File file = new File("aa.log");
        FileWriter writer=  new FileWriter(file);

        //数据写入缓冲区
        writer.write("hello world1");
        //刷新缓冲区，确保数据写入到文件
        writer.flush();

        writer.write("hello world3");
        writer.close();
    }
}
