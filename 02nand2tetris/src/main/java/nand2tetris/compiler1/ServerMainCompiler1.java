package nand2tetris.compiler1;

import java.io.File;

/**
 * @author jiaolong
 * @date 2024/04/06 11:19
 */
public class ServerMainCompiler1 {
    public static void main(String[] args) {
        String file = "D:\\test\\10\\ArrayTest\\Main.jack";
        File f = new File(file);

        JackTokenizer jackTokenizer = new JackTokenizer(f);
        while (true){
            boolean flag = jackTokenizer.advance();


            if(!flag){
                break;
            }
        }

    }
}
