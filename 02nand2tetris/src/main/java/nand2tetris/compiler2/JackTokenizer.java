package nand2tetris.compiler2;

import nand2tetris.compiler1.ReadJackFileUtil;
import nand2tetris.compiler1.pubField.TokenType;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

/**
 * ��Ԫת����
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


    //��ǰJack����
    private String currentCommand;
    //String���ͱ��
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

    //��ȡ��һ��ָ�����������Ϊ��ǰ����
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

        //��������ĸ�»�����ɣ����У����������ֿ�ͷ
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

    //��Ԫ��5������1
    public String keyword() {
        if(tokenType() == TokenType.KEYWORD){
            return currentCommand;
        }
        return "";
    }

    //����
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

    //��ʶ�������� ������ ������
    public String identifier() {
        if(tokenType() == TokenType.IDENTIFIER){
            return  currentCommand ;
        }
        return "";
    }

    //��������
    public String inVal() {
        if(tokenType() == TokenType.INT_CONST){
            return currentCommand;
        }

        return "";
    }

    //�ַ�����
    public String stringVal() {
        if(tokenType() == TokenType.STRING_CONST){
            return currentCommand.replace("\"","") ;
        }
        return "";
    }


    /**
     * ��ȡ��ǰԪ�����ݣ���ɾ����Ԫ��
     * @param: null
     * @return:
     **/
    public String getPeekValue(){
        return tockens.peek();
    }
    public String getNextValue(){
        //ȡ����ǰֵ����ɾ��Ԫ��
        String temp = tockens.poll();
        String returnValue = tockens.peek();
        //ջ������������ȳ�
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

                //�ո�
                if (Character.isWhitespace(c) && stringTypeFlag==false) {
                    printInfo(info.toString());
                    info.setLength(0);

                }
                //����
                else if(symbols.contains(String.valueOf(c))){
                    printSymbols(info.toString());
                    info.setLength(0);

                }
                //�ַ���
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
