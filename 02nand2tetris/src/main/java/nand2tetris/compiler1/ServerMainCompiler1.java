package nand2tetris.compiler1;

import nand2tetris.CommonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jiaolong
 * @date 2024/04/06 11:19
 */
public class ServerMainCompiler1 {
    public static void main(String[] args) {
        String file = "D:\\test\\10\\ArrayTest\\Main.jack";
        File f = new File(file);

        List<String> jackCodes = readJackFile(f);
        for(String code: jackCodes){
            JackScaning.getJackChar(code);
        }



//        JackTokenizer jackTokenizer = new JackTokenizer(f);
//        while (true){
//            boolean flag = jackTokenizer.advance();
//            if(!flag){
//                break;
//            }
//        }

    }


   private static List<String> readJackFile(File jackFile) {
       //读取文件内容
       List<String> jackInfo = CommonUtils.readFile(jackFile.toPath());

       //内容过滤
       List<String> list = new ArrayList<>();
       for (String info : jackInfo) {

           String command = "";
           if (info.contains("/**")) {
               command = info.substring(0, info.indexOf("/**")).trim();

           } else if (info.contains("//")) {
               command = info.substring(0, info.indexOf("//")).trim();
           } else {
               command = info.trim();
           }

           if (command.length() > 0) {
               list.add(command);
           }
       }
        return list;
   }


}
