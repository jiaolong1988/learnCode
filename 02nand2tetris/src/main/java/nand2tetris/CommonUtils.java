package nand2tetris;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jiaolong
 * @date 2024/01/22 11:33
 * @description: TODO
 */
public class CommonUtils {

    public static List<String> readFile(Path p) {
        List<String> Info = null;
        try {
            Info = Files.readAllLines(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Info;
    }

    /**
     * @author jiaolong
     * @date 2024-1-22 10:55
     * suffix
     */
    public static void outputFile(Path p, List<String> binaryProgram,String suffix) {
        String fileName = p.getFileName().toString();
        String outFileName = fileName.substring(0, fileName.length()-suffix.length())+"."+suffix;
        Path outPath = Paths.get(p.getParent().toString(), outFileName );

        try {
            Files.write(outPath, binaryProgram);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param: info
     * @return: java.lang.String
     * ��ȡ������˵�//
     **/
    public static String getCommand(String info){
        //��ȡ���룬ȥ��ע��
        String command = info.trim();
        if(info.contains("//")){
            command = info.substring(0,info.indexOf("//")).trim();
        }
        return command;
    }


    /**
     * ��ȡĿ¼�µ���Ŀ¼�ļ�
     * @param: sourcePath
     * @return: java.util.List<java.lang.String>
     **/
    public static List<String> getFiles(Path sourcePath)  {
        List<String> files = new ArrayList<>();

        try {
            Files.list(sourcePath).forEach(x-> {
                Path pc = Paths.get(x.toString());
                try {
                    Files.list(pc).forEach(y -> {files.add(y.toString());});
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }
}
