package base;

import java.io.File;
import java.io.IOException;

/**
 * ��ȡ��ǰ��Ŀ��·��
 * @author: jiaolong
 * @date: 2024/06/13 14:23
 **/
public class FetchCurrentPathTest {
    public static void main(String[] args) throws IOException {

        System.out.println("===��ȡ��ǰ��Ŀ·��");
        File f = new File("");
        System.out.println(f.getAbsolutePath());


        System.out.println("\n===�������·�� ��ȡ��׼��ַ�� ./javaCode/Main.jack");
        //��ȡ���·��
        File directory = new File("./javaCode/Main.jack");
        File fFile = directory.getCanonicalFile();
        String fString = directory.getCanonicalPath();
        System.out.println(fFile.getAbsolutePath());



        System.out.println("\n===��ȡ����·��");
        File dir = new File("aa");
        System.out.println(dir.getAbsolutePath());//��ȡ����·��
        System.out.println(dir.getPath());  //��ȡָ����·������File��ָ����·��
    }
}
