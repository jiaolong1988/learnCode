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

   //��jack����ת��Ϊ��Ԫ
   public static void getJackChar(String code){

     // String code = "let length = Keyboard.readInt(\"HOW MANY NUMBERS? \");";
      //��ȡ�ַ�����Ϣ
      String stringCode = "";
      if(code.contains("\"")){
         int a = code.indexOf("\"");
         int b = code.lastIndexOf("\"");

         stringCode = code.substring(a, b+1);
      }

      String ncode = code;
      if(stringCode.length()>0){
         //�滻�ַ�����Ϣ
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
      //int ������ת��Ϊ����
      //String ���� ˫����"
      //�ؼ��� ���� ȥ�ֵ��жԱ�
      //�����Ķ��� ��ʶ��

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
//�ؼ��ִ���
         for(String keyword: keywords){
            //�Թؼ��ֿ�ʼ
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

//��ʶ������
         if(code.length()>0){

            //1.����Ƿ���������ַ�
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

            //���������ַ�����
            if(symbolFlag>-1 && symbol.length()>0){
               String identifier = code.substring(0,symbolFlag);
               // 0;
               if(getIntType(identifier)){
                  //����
                  infos.add(inVal(identifier));
               }else{
                 // Keyboard.readInt()
                  infos.add(identifier(identifier));
               }

               //����
               infos.add(symbol(symbol));

               String nCode = code.substring(symbolFlag+1, code.length());
               if(nCode.length()>0){
                  jackAnalysis(nCode);
               }

            }else{
               //δ���������ַ�����
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


   //��Ԫ��5������1
   public static String keyword(String currentCode) {
      return "<keyword> " + currentCode + " </keyword>";
   }

   //����
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
      String scode = currentCode.replace("\"", "");
      return "<stringConstant> " + scode + " </stringConstant>";
   }

}
