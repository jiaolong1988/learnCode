package nand2tetris.compiler1;

import nand2tetris.CommonUtils;
import nand2tetris.compiler1.pubField.Keyword;
import nand2tetris.compiler1.pubField.TokenType;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 字元转换器
 *
 * @author jiaolong
 * @date 2024/04/06 11:01
 */
public class JackTokenizer {

    private Iterator<String> iterator = null;

    public JackTokenizer(File inputFilePath) {
        //读取文件内容
        List<String> jackInfo = CommonUtils.readFile(inputFilePath.toPath());

        //内容过滤
        List<String> list = new ArrayList<>();
        for(String info :jackInfo){

            String command = "";
            if(info.contains("/**")){
                command = info.substring(0,info.indexOf("/**")).trim();

            }else if(info.contains("//")){
                command = info.substring(0,info.indexOf("//")).trim();
            }else{
                command = info.trim();
            }

            if(command.length()>0){
                list.add(command);
            }
        }

        for(String word: list){
            word.trim().split(" ");
        }

        iterator = list.iterator();
    }

    public boolean hasMoreTokens() {
        return iterator.hasNext();
    }

    //读取下一条指令，并将其设置为当前命令
    public boolean advance(){
        boolean flag = hasMoreTokens();
        if(flag){
            String currentCommand = iterator.next().trim();
            System.out.println("jack info："+currentCommand);
        }

        return flag;
    }

    public TokenType tokenType() {

        return TokenType.IDENTIFIER;
    }

    public Keyword keyword() {

        return Keyword.BOOLEAN;
    }

    public String symbol() {

        return null;
    }

    public String identifier() {

        return null;
    }

    public int inVal() {

        return 0;
    }

    public String StringVal() {

        return null;
    }

}
