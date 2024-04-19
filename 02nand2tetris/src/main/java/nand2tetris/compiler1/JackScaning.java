package nand2tetris.compiler1;

import nand2tetris.compiler1.pubField.Keyword;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: jiaolong
 * @date: 2024/04/08 17:25
 **/
public class JackScaning {
   public static List<String> infos = new ArrayList<>();
   private static List<String> keywords = Arrays.asList("class", "constructor", "function",
            "method", "field", "static", "var", "int", "char",
            "boolean", "void", "true", "false", "null", "this",
            "let", "do", "if", "else", "while", "return");


   private static List<String> symbols = Arrays.asList(
           "{", "}", "(", ")", "[", "]", ".",
           ",", ";", "+", "-", "*", "/", "&",
           "|", "<", ">","=", "~"
   );

   //将jack语言转换为字元
   public static void getJackChar(String code){

     // String code = "let length = Keyboard.readInt(\"HOW MANY NUMBERS? \");";
      //截取字符串信息
      String stringCode = "";
      if(code.contains("\"")){
         int a = code.indexOf("\"");
         int b = code.lastIndexOf("\"");

         stringCode = code.substring(a, b+1);
      }

      String ncode = code;
      if(stringCode.length()>0){
         //替换字符串信息
          ncode = code.replace(stringCode," \" ");
      }


      for(String c: ncode.split(" ")){

         if(c.equals("\"")){
            jackAnalysis(stringCode);
         }else{
            jackAnalysis(c.trim());
         }
      }
   }

   public static void jackAnalysis(String code){
      //int 将代码转换为数字
      //String 查找 双引号"
      //关键字 符号 去字典中对比
      //其他的都是 标识符

      String firstChar = "";
      String nnCode ="";
      if(code.length() > 1){
         firstChar = code.substring(0, 1);
         nnCode = code.substring(1, code.length());
      }else{
         firstChar = code;
      }

      if(symbols.contains(firstChar)){

         infos.add(symbol(firstChar));

        if(nnCode.length()>0){
           jackAnalysis(nnCode);
        }

      }else if(firstChar.equals("\"")){
         infos.add(StringVal(code));

      }else{

         String kw ="";
//关键字处理
         for(String keyword: keywords){
            //以关键字开始
            if(code.startsWith(keyword)){
               kw = keyword;
               break;
            }
         }

         if(kw.length()>0){
            infos.add(keyword(kw));

            String nCode = code.replace(kw,"").trim();
            if(nCode.length()>0){
               jackAnalysis(nCode);
               code="";
            }else{
               return;
            }
         }

//标识符处理
         if(code.length()>0){

            //1.检查是否包含特殊字符
            int symbolFlag = -1;
            String  symbol= "";
            char[] chars = code.toCharArray();
            for(int i=0; i< chars.length; i++){
               char temp = chars[i];

               if(symbols.contains(String.valueOf(temp))){
                  symbol= String.valueOf(chars[i]);
                  symbolFlag = i;
                  break;
               }
            }

            //包含特殊字符处理
            if(symbolFlag>-1 && symbol.length()>0){
               String identifier = code.substring(0,symbolFlag);
               // 0;
               if(getIntType(identifier)){
                  //数字
                  infos.add(inVal(identifier));
               }else{
                 // Keyboard.readInt()
                  infos.add(identifier(identifier));
               }

               //符号
               infos.add(symbol(symbol));

               String nCode = code.substring(symbolFlag+1, code.length());
               if(nCode.length()>0){
                  jackAnalysis(nCode);
               }

            }else{
               //未包含特殊字符处理
               infos.add(identifier(code));
            }
         }



      }

   }

   private static boolean getIntType(String code){
      try {
         Integer.valueOf(code);
         return true;
      } catch (NumberFormatException e) {
      }

      return false;
   }


   //字元的5中类型1
   public static String keyword(String currentCode) {
      return "<keyword> " + currentCode + " </keyword>";
   }

   //符号
   public static String symbol(String currentCode) {
      String schar="";
      if(currentCode.equals("<")){
         schar = "&lt;";
      }else if(currentCode.equals(">")){
         schar = "&gt;";
      } else if(currentCode.equals("&")){
         schar = "&amp;";
      }else{
         schar=currentCode;
      }

      return "<symbol> " + schar + " </symbol>";
   }

   //标识符：类名 方法名 变量名
   public static String identifier(String currentCode) {
      return "<identifier> " + currentCode + " </identifier>";
   }

   //整数常量
   public static String inVal(String currentCode) {
      return "<integerConstant> " + currentCode + " </integerConstant>";
   }

   //字符常量
   public static String StringVal(String currentCode) {
      String scode = currentCode.replace("\"", "");
      return "<stringConstant> " + scode + " </stringConstant>";
   }

}
