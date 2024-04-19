package nand2tetris.compiler1;

import nand2tetris.CommonUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jiaolong
 * @date 2024/04/06 11:19
 */
public class ServerMainCompiler1 {
    public static void main(String[] args) {

        String dirPath  = "D:\\test\\10\\Square";
        File dir = new File(dirPath);
        for(File file : dir.listFiles()){
            if(file.getName().contains(".jack")){
                alalysisFileToXml(file.getAbsolutePath());
            }
        }

        //String file = "D:\\test\\10\\ArrayTest\\Main.jack";
        //String file  = "D:\\test\\10\\Square\\Square.jack";
        // String file  = "D:\\test\\10\\Square\\SquareGame.jack";
        // alalysisFileToXml(file);
    }

    private static void alalysisFileToXml(String file) {
        JackScaning.infos.clear();
        File f = new File(file);

        JackScaning.infos.add("<tokens>");
        List<String> jackCodes = readJackFile(f);
        for(String code: jackCodes){
            //System.out.println("--> "+code);
            JackScaning.getJackChar(code);
        }

        JackScaning.infos.add("</tokens>");
        outputFile(f.toPath(),JackScaning.infos,"_T");
    }


    private static List<String> readJackFile(File jackFile) {
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
