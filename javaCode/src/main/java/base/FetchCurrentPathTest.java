package base;

import java.io.File;
import java.io.IOException;

/**
 * 获取当前项目的路径
 * @author: jiaolong
 * @date: 2024/06/13 14:23
 **/
public class FetchCurrentPathTest {
    public static void main(String[] args) throws IOException {

        System.out.println("===获取当前项目路径");
        File f = new File("");
        System.out.println(f.getAbsolutePath());


        System.out.println("\n===根据相对路径 获取标准地址： ./javaCode/Main.jack");
        //获取相对路径
        File directory = new File("./javaCode/Main.jack");
        File fFile = directory.getCanonicalFile();
        String fString = directory.getCanonicalPath();
        System.out.println(fFile.getAbsolutePath());



        System.out.println("\n===获取绝对路径");
        File dir = new File("aa");
        System.out.println(dir.getAbsolutePath());//获取绝对路径
        System.out.println(dir.getPath());  //获取指定的路径，既File类指定的路径
    }
}
