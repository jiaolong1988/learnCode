package nand2tetris.compiler2;

import nand2tetris.compiler1.ReadJackFileUtil;
import nand2tetris.compiler1.pubField.TokenType;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

/**
 * 字元转换器
 *
 * @author jiaolong
 * @date 2024/04/06 11:01
 */
public class JackTokenizer {

    private static List<String> keywords = Arrays.asList("class", "constructor", "function",
            "method", "field", "static", "var", "int", "char",
            "boolean", "void", "true", "false", "null", "this",
            "let", "do", "if", "else", "while", "return");


    private static List<String> symbols = Arrays.asList(
            "{", "}", "(", ")", "[", "]", ".",
            ",", ";", "+", "-", "*", "/", "&",
            "|", "<", ">","=", "~"
    );


    //当前Jack内容
    private String currentCommand;
    //String类型标记
    private boolean stringTypeFlag = false;

    private ArrayDeque<String> tockens = new ArrayDeque<>();

    public JackTokenizer(File inputFilePath) {
        tockens.clear();

        List<String>  jackCodes = ReadJackFileUtil.readJackFile(inputFilePath);
        for(String jackCode : jackCodes) {
            new SplitCodeUtil().splitCode(jackCode);
        }
    }


    public boolean hasMoreTokens() {
        return !tockens.isEmpty();
    }

    //读取下一条指令，并将其设置为当前命令
    public void advance(){
        boolean flag = hasMoreTokens();
        if(flag){
            currentCommand = tockens.poll();
        }
    }

    public TokenType tokenType() {
        if (symbols.contains(currentCommand)) {
            return TokenType.SYMBOL;
        }

        if (currentCommand.contains("\"")) {
            return TokenType.STRING_CONST;
        }

        if (keywords.contains(currentCommand)) {
            return TokenType.KEYWORD;
        }

        //由数字字母下划线组成，其中，不能以数字开头
        if (currentCommand.matches("^[a-zA-Z_]{1}[a-zA-Z0-9_]*")) {
            return TokenType.IDENTIFIER;
        }

        if (currentCommand.matches("\\d+")) {
            return TokenType.INT_CONST;
        }

        return null;
    }

    public String getTockenValue(){
        TokenType type = tokenType();
        if(type == TokenType.KEYWORD){
            return keyword();
        }
        if(type == TokenType.SYMBOL){
            return symbol();
        }
        if(type == TokenType.IDENTIFIER){
            return identifier();
        }
        if(type == TokenType.STRING_CONST){
            return stringVal();
        }
        if(type == TokenType.INT_CONST){
            return inVal();
        }

        return "";
    }

    //字元的5中类型1
    public String keyword() {
        if(tokenType() == TokenType.KEYWORD){
            return currentCommand;
        }
        return "";
    }

    //符号
    public String symbol() {
        String schar="";
        if(tokenType() == TokenType.SYMBOL){
            if(currentCommand.equals("<")){
                schar = "&lt;";
            }else if(currentCommand.equals(">")){
                schar = "&gt;";
            } else if(currentCommand.equals("&")){
                schar = "&amp;";
            }else{
                schar=currentCommand;
            }
        }

        return schar;
    }

    //标识符：类名 方法名 变量名
    public String identifier() {
        if(tokenType() == TokenType.IDENTIFIER){
            return  currentCommand ;
        }
        return "";
    }

    //整数常量
    public String inVal() {
        if(tokenType() == TokenType.INT_CONST){
            return currentCommand;
        }

        return "";
    }

    //字符常量
    public String stringVal() {
        if(tokenType() == TokenType.STRING_CONST){
            return currentCommand.replace("\"","") ;
        }
        return "";
    }


    /**
     * 读取当前元素数据，不删除该元素
     * @param: null
     * @return:
     **/
    public String getPeekValue(){
        return tockens.peek();
    }
    public String getNextValue(){
        //取出当前值，并删除元素
        String temp = tockens.poll();
        String returnValue = tockens.peek();
        //栈操作，后进入先出
        tockens.push(temp);

        return returnValue;
    }


    public TokenType getPeekType(){
        String temp = currentCommand;
        currentCommand = getPeekValue();
        TokenType type = tokenType();
        currentCommand = temp;
        return type;
    }

    class SplitCodeUtil{
        private void splitCode(String jackCode) {
            StringBuilder info = new StringBuilder();
            for (int i = 0; i < jackCode.length(); i++) {
                char c = jackCode.charAt(i);
                info.append(c);

                //空格
                if (Character.isWhitespace(c) && stringTypeFlag==false) {
                    printInfo(info.toString());
                    info.setLength(0);

                }
                //符号
                else if(symbols.contains(String.valueOf(c))){
                    printSymbols(info.toString());
                    info.setLength(0);

                }
                //字符串
                else if("\"".equals(String.valueOf(c)) || stringTypeFlag==true){
                    if (stringTypeFlag==true &&  String.valueOf(c).equals("\"") ){
                        printInfo(info.toString());
                        info.setLength(0);
                        stringTypeFlag=false;
                    }else{
                        stringTypeFlag=true;
                    }
                }
            }

        }

        private void printInfo(String info) {
            String nstring = info.trim();
            if(nstring.length()>0){
                tockens.offer(nstring);
            }
        }

        private void printSymbols(String info) {
            String nstring = info.substring(0,info.length()-1).trim();
            printInfo(nstring);

            String symbol = info.substring(info.length()-1).trim();
            tockens.offer(symbol);
        }

    }

}
