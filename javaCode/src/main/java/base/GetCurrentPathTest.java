package base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 获取当前项目的路径
 * @author: jiaolong
 * @date: 2024/06/13 14:23
 **/
public class GetCurrentPathTest {
    public static void main(String[] args) throws IOException {

        // 当前路径\aa\xx.txt
        Path path = Paths.get("aa","xx.txt");
        String currentPath = path.toAbsolutePath().toString();
        System.out.println("Paths 获取当前路径：" + currentPath);

        File f = new File("");
        System.out.println("File 获取当前路径：" +f.getAbsolutePath());

        System.out.println("\n===File获取绝对路径");
        File dir = new File("javaCode/Main.jack");
        System.out.println("File 获取当前路径：" +dir.getAbsolutePath());//获取绝对路径
        System.out.println("File 获取当前路径：" +dir.getPath());  //获取指定的路径，既File类指定的路径

        System.out.println("\n===File 与Path相互转换");
        Path p = f.toPath();
        File file = path.toFile();
        System.out.println(p.toAbsolutePath()+"  ==> "+file.getAbsolutePath());
    }
}
