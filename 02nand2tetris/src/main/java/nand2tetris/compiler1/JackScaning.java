package nand2tetris.compiler1;

import nand2tetris.compiler1.pubField.Keyword;

import java.util.Arrays;
import java.util.List;

/**
 * @author: jiaolong
 * @date: 2024/04/08 17:25
 **/
public class JackScaning {

   private static List<String> keywords = Arrays.asList("class", "constructor", "function",
            "method", "field", "static", "var", "int", "char",
            "boolean", "void", "true", "false", "null", "this",
            "let", "do", "if", "else", "while", "return");


   private static List<String> symbols = Arrays.asList("{", "}", "(", ")", "[", "]", ".",",",
            ";", "+", "-", "*", "/", "&", "|", "<", ">","=", "~");

   //将jack语言转换为字元
   public static void getJackChar(String code){
      for(String c: code.split(" ")){
         jackAnalysis(c.trim());
      }
   }

   public static void jackAnalysis(String code){
      //int 将代码转换为数字
      //String 查找 双引号"
      //关键字 符号 去字典中对比
      //其他的都是 标识符

      String firstChar = "";
      if(code.length() > 1){
         firstChar = code.substring(0, 1);
      }else{
         firstChar = code;
      }

      if(symbols.contains(firstChar)){
         System.out.println(symbol(firstChar));

      }else if(firstChar.equals("\"")){
         System.out.println(StringVal(code));

      }else if(getIntType(code)){
         System.out.println(inVal(code));

      }else{
         //关键字处理
         for(String keyword: keywords){
            //以关键字开始
            if(code.startsWith(keyword)){
               System.out.println(keyword(keyword));

               String nCode = code.replace(keyword,"").trim();
               jackAnalysis(nCode);
            }
         }

         //标识符处理
         char[] chars = code.toCharArray();
         for(int i=0; i< chars.length; i++){

            if(symbols.contains(chars[i])){
               code.
               System.out.println(identifier(keyword));
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
      return "<symbol> " + currentCode + " </symbol>";
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
      return "<stringConstant> " + currentCode + " </stringConstant>";
   }

}
