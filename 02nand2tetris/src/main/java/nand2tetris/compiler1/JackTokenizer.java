package nand2tetris.compiler1;

import nand2tetris.compiler1.pubField.Keyword;
import nand2tetris.compiler1.pubField.TokenType;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
    private boolean stringTypeFlag = false;

    public  List<String> jackCodes;
    public  List<String> jackLineCode = new ArrayList<>();
    private Iterator<String> lineIterator;

    private List<String> tockens = new ArrayList();

    public JackTokenizer(File inputFilePath) {
        jackCodes = ReadJackFileUtil.readJackFile(inputFilePath);
//        for (String jackCode : jackCodes) {
//            splitCode(jackCode);
//        }

      //  iterator = jackCodes.iterator();
    }

    public void splitCode(String jackCode) {
        jackLineCode.clear();
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

        lineIterator = jackLineCode.iterator();
    }

    private void printInfo(String info) {
        String nstring = info.trim();
        if(nstring.length()>0){
           // System.out.println(nstring);
            jackLineCode.add(nstring);
        }
    }

    private void printSymbols(String info) {
        String nstring = info.substring(0,info.length()-1).trim();
        printInfo(nstring);

        String symbol = info.substring(info.length()-1).trim();
       // System.out.println(symbol);
        jackLineCode.add(symbol);
    }


    public boolean hasMoreTokens() {
        return lineIterator.hasNext();
    }

    //读取下一条指令，并将其设置为当前命令
    public boolean advance(){
        boolean flag = hasMoreTokens();
        if(flag){
            currentCommand = lineIterator.next().trim();
        }

        return flag;
    }

    public TokenType tokenType() {
        if(symbols.contains(currentCommand)){
            tockens.add( symbol());
            return  TokenType.SYMBOL;
        }

        if (currentCommand.contains("\"")){
            tockens.add( StringVal());
            return TokenType.STRING_CONST;
        }

        if (keywords.contains(currentCommand)){
            tockens.add(keyword());
            return  TokenType.KEYWORD;
        }

        //由数字字母下划线组成，其中，不能以数字开头
       if( currentCommand.matches("^[a-zA-Z_]{1}[a-zA-Z0-9_]*")){
           tockens.add(identifier());
           return TokenType.IDENTIFIER;
       }

        if( currentCommand.matches("\\d+")){
            tockens.add(inVal());
            return TokenType.INT_CONST;
        }

        System.out.println(currentCommand);
        return null;
    }

    //字元的5中类型1
 //   public Keyword keyword() {
    public String keyword() {
    //<keyword>  </keyword>
        return "<keyword> " + currentCommand + " </keyword>";
      //  return Keyword.BOOLEAN;
    }

    //符号
    public String symbol() {
    //<symbol>  </symbol>
        String schar="";
        if(currentCommand.equals("<")){
            schar = "&lt;";
        }else if(currentCommand.equals(">")){
            schar = "&gt;";
        } else if(currentCommand.equals("&")){
            schar = "&amp;";
        }else{
            schar=currentCommand;
        }

        return "<symbol> " + schar + " </symbol>";
    }

    //标识符：类名 方法名 变量名
    public String identifier() {
    //<identifier>  </identifier>
        return "<identifier> " + currentCommand + " </identifier>";
    }

    //整数常量
    public String inVal() {
    //<integerConstant>  </integerConstant>
        return "<integerConstant> " + currentCommand + " </integerConstant>";
    }

    //字符常量
    public String StringVal() {
        //<stringConstant> </stringConstant>
        return "<stringConstant> " + currentCommand.replace("\"","") + " </stringConstant>";
    }

    public List<String> getTockens() {
        return tockens;
    }
}
