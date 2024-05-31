package nand2tetris.compiler2;

import java.io.File;

/**
 * @author: jiaolong
 * @date: 2024/05/22 10:35
 **/
public class JackAnalyzer {
    public static void main(String[] args) {
        String file  = "D:\\test\\11\\Square\\SquareGame.jack";
        File inputFile = new File(file);
        File outFile = getOutputFile(inputFile);

        CompilationEngine compilationEngine = new CompilationEngine(inputFile, outFile);
        compilationEngine.compileClass();
    }

    private static File getOutputFile(File inputFile) {
        String type = "_jl";
        String fileName = inputFile.getAbsolutePath().toString();
        String outFileName = fileName.substring(0, fileName.lastIndexOf(".") )+type+".vm";
        File outFile = new File(outFileName);
        return outFile;
    }
}
