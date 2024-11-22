package nand2tetris.compiler1;

import nand2tetris.CommonUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jiaolong
 * @date 2024/04/21 17:15
 */
public class ReadJackFileUtil {
    public static List<String> readJackFile(File jackFile) {
        //读取文件内容
        List<String> jackInfo = CommonUtils.readFile(jackFile.toPath());

        //内容过滤
        List<String> list = new ArrayList<>();
        for (String jack : jackInfo) {

            String info = jack.trim();
            String command = "";
            if (info.contains("/**")) {
                command = info.substring(0, info.indexOf("/**")).trim();

            } else if (info.contains("//")) {
                command = info.substring(0, info.indexOf("//")).trim();

            } else if (info.startsWith("*") || info.startsWith("*/")) {
                command="";
            }else {
                command = info.trim();
            }

            if (command.length() > 0) {
                list.add(command);
            }
        }
        return list;
    }


    public static void outputFile(Path p, List<String> binaryProgram, String type) {
        String fileName = p.toAbsolutePath().toString();
        String outFileName = fileName.substring(0, fileName.lastIndexOf(".") )+type+".xml";

        Path outPath = Paths.get(outFileName);
        try {
            Files.write(outPath, binaryProgram);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
