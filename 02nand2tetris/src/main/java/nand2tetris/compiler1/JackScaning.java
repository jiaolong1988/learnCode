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

   //��jack����ת��Ϊ��Ԫ
   public static void getJackChar(String code){
      for(String c: code.split(" ")){
         jackAnalysis(c.trim());
      }
   }

   public static void jackAnalysis(String code){
      //int ������ת��Ϊ����
      //String ���� ˫����"
      //�ؼ��� ���� ȥ�ֵ��жԱ�
      //�����Ķ��� ��ʶ��

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
         //�ؼ��ִ���
         for(String keyword: keywords){
            //�Թؼ��ֿ�ʼ
            if(code.startsWith(keyword)){
               System.out.println(keyword(keyword));

               String nCode = code.replace(keyword,"").trim();
               jackAnalysis(nCode);
            }
         }

         //��ʶ������
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


   //��Ԫ��5������1
   public static String keyword(String currentCode) {
      return "<keyword> " + currentCode + " </keyword>";
   }

   //����
   public static String symbol(String currentCode) {
      return "<symbol> " + currentCode + " </symbol>";
   }

   //��ʶ�������� ������ ������
   public static String identifier(String currentCode) {
      return "<identifier> " + currentCode + " </identifier>";
   }

   //��������
   public static String inVal(String currentCode) {
      return "<integerConstant> " + currentCode + " </integerConstant>";
   }

   //�ַ�����
   public static String StringVal(String currentCode) {
      return "<stringConstant> " + currentCode + " </stringConstant>";
   }

}
