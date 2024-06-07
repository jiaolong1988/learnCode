package nand2tetris.compiler2;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: jiaolong
 * @date: 2024/05/22 10:35
 **/
public class JackAnalyzer {
    public static void main(String[] args) {

        //Ŀ¼
        String dir = "D:\\test\\11\\ComplexArrays";
        File dirFile = new File(dir);

        //��ȡĿ¼�µ� jack�ļ�
        List<File> jackList = new ArrayList();
        for(File fp : dirFile.listFiles()){
            if(fp.getName().endsWith(".jack")){
                jackList.add(fp);
            }
        }

        //��ȡ������Ϣ
        for(File jackFile: jackList){
           // String file  = "D:\\test\\11\\Square\\Square.jack";
           // File inputFile = new File(file);

            compileClass(jackFile);
        }
    }

    private static void compileClass(File inputFile) {
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
