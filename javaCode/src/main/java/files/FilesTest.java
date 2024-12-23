package files;

import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: jiaolong
 * @date: 2024/11/12 16:38
 **/
public class FilesTest {
    public static void main(String[] args) throws Exception {
        Path sourcePath = Paths.get("src/FilesTest.java").toAbsolutePath();
        Path target = Paths.get("src/FilesTestB.java").toAbsolutePath();
        System.out.println("源文件路径："+sourcePath);

        // 复制文件
        Files.copy(sourcePath, new FileOutputStream("a.txt"));
        Files.move(sourcePath, target);

        // 判断FilesTest.java文件是否为隐藏文件
        System.out.println("\nFilesTest.java是否为隐藏文件：" + Files.isHidden(sourcePath));

        // 一次性读取FilesTest.java文件的所有行
        List<String> lines = Files.readAllLines(sourcePath, Charset.forName("gbk"));
        System.out.println("读取文件所有信息存储为List:"+lines);

        // 判断指定文件的大小
        System.out.println("\nFilesTest.java的大小为：" + Files.size(sourcePath));

        List<String> poem = new ArrayList<>();
        poem.add("水晶潭底银鱼跃");
        poem.add("清徐风中碧竿横");
        // 直接将多个字符串内容写入指定文件中
        Path writeFlag = Files.write(Paths.get("pome.txt"), poem, Charset.forName("gbk"));
        System.out.println("文件写入成功,写入路径："+writeFlag.toAbsolutePath());

        System.out.println("\n读取当前目录下所有文件和子目录");
        // 使用Java 8新增的Stream API列出当前目录下所有文件和子目录
        Files.list(Paths.get(".")).forEach(path -> System.out.println(path));


        // 使用Java 8新增的Stream API读取文件内容
        Files.lines(sourcePath, Charset.forName("gbk")).forEach(line -> System.out.println(line));

        FileStore cStore = Files.getFileStore(Paths.get("C:"));
        // 判断C盘的总空间，可用空间
        System.out.println("C:共有空间：" + cStore.getTotalSpace());
        System.out.println("C:可用空间：" + cStore.getUsableSpace());
    }
}