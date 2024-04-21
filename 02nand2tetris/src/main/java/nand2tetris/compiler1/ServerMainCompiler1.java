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

        String file  = "D:\\test\\10\\Square\\MainT.xml";
        File inputFile = new File(file);

        CompilationEngine ce = new CompilationEngine(inputFile, null);
        ce.getInfo();

    }

    public static void tets1(){
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
        List<String> jackCodes = ReadJackFileUtil.readJackFile(f);
        for(String code: jackCodes){
            //System.out.println("--> "+code);
            JackScaning.getJackChar(code);
        }

        JackScaning.infos.add("</tokens>");
        ReadJackFileUtil.outputFile(f.toPath(),JackScaning.infos,"_T");
    }




}
