package nand2tetris.compiler1;

import java.io.File;
import java.util.List;

/**
 * @author jiaolong
 * @date 2024/04/06 11:19
 * https://github.com/AllenWrong/nand2tetris
 */
public class ServerMainCompiler1 {
    public static void main(String[] args) {
       // testTockens();
        testCompilation();
    }

    public static void testTockens(){
        String dirPath  = "D:\\test\\10\\ExpressionLessSquare\\Square.jack";
        File dir = new File(dirPath);

        JackTokenizer jackTokenizer = new JackTokenizer(dir);
        jackTokenizer.getTockens().add("<tokens>");
        for(String jackCode : jackTokenizer.jackCodes){
            jackTokenizer.splitCode(jackCode);

            while(jackTokenizer.advance()){
                jackTokenizer.tokenType();
            }
        }
        jackTokenizer.getTockens().add("</tokens>");
        jackTokenizer.getTockens().forEach(System.out::println);

        ReadJackFileUtil.outputFile(dir.toPath(), jackTokenizer.getTockens(),"_TT");

    }
    public static void testCompilation(){
        String file  = "D:\\test\\10\\ExpressionLessSquare\\SquareGameT.xml";
        File inputFile = new File(file);

        CompilationEngine ce = new CompilationEngine(inputFile, null);
        ce.compileClass();
    }

//    public static void tets1(){
//        String dirPath  = "D:\\test\\10\\Square";
//        File dir = new File(dirPath);
//        for(File file : dir.listFiles()){
//            if(file.getName().contains(".jack")){
//                alalysisFileToXml(file.getAbsolutePath());
//            }
//        }
//
//        //String file = "D:\\test\\10\\ArrayTest\\Main.jack";
//        //String file  = "D:\\test\\10\\Square\\Square.jack";
//        // String file  = "D:\\test\\10\\Square\\SquareGame.jack";
//        // alalysisFileToXml(file);
//    }
//
//    private static void alalysisFileToXml(String file) {
//        JackScaning.infos.clear();
//        File f = new File(file);
//
//        JackScaning.infos.add("<tokens>");
//        List<String> jackCodes = ReadJackFileUtil.readJackFile(f);
//        for(String code: jackCodes){
//            //System.out.println("--> "+code);
//            JackScaning.getJackChar(code);
//        }
//
//        JackScaning.infos.add("</tokens>");
//        ReadJackFileUtil.outputFile(f.toPath(),JackScaning.infos,"_T");
//    }


}
