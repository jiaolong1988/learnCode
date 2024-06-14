package io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author: jiaolong
 * @date: 2024/06/13 16:16
 **/
public class FileReaderTest {
    public static void main(String[] args) throws IOException {
        File paramFile = new File("./javaCode/Main.jack");
        FileReader fr =  new FileReader(paramFile.getCanonicalFile());
        //读取一个字符
        System.out.println((char)fr.read());
    }
}
