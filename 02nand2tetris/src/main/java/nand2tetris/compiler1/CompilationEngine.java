package nand2tetris.compiler1;

import java.io.File;
import java.util.*;

/**
 * 自顶向下的递归语法分析器
 * @author jiaolong
 * @date 2024/04/06 11:02
 */
public class CompilationEngine {

    private HashMap<String, String> keywordLableMap = new HashMap<>();
    private HashMap<String, String> statements = new HashMap<>();
    private HashMap<String, String> expressionMap = new HashMap<>();
    private List<String> keywordConstant = Arrays.asList("true","false","null","this");

    File inputFile=null;

    //指针
    private static List<String> type = new ArrayList<>();
    //当前类型的空格距离
    private static String currentSpace = "";
    //当前代码
    private static String currentCode = "";
    //上一行代码
    private static String previousCodeVlaue="";

    public CompilationEngine(File inputFile, File outFile){
        this.inputFile = inputFile;

        keywordLableMap.put("class","class");
        keywordLableMap.put("static","classVarDec");
        keywordLableMap.put("function","subroutineDec");
        keywordLableMap.put("var","varDec");

        statements.put("let","letStatement");
        statements.put("do","doStatement");
        statements.put("if","ifStatement");

        expressionMap.put("=","expression");
    }

    public void getInfo(){
        List<String> jackCodes = ReadJackFileUtil.readJackFile(inputFile);
        for(String code: jackCodes){
            if(!code.contains("tokens")){
                previousCodeVlaue = currentCode;
                currentCode = code;

                String value = LableUtil.getTockenLableValue();
                if("[({=".contains(value)){
                    symbolTypeStart();
                }else if(")}];".contains(value)){
                    symbolTypeEnd();
                }else{
                    otherHandle(value);
                }

            }
        }
    }


    private void otherHandle(String value) {
        String currentType = LableUtil.getCurrentType();
        List  termList= Arrays.asList("stringConstant","integerConstant");

        List<String> termEnd = Arrays.asList( "*", "/","-","|" );

        if(keywordLableMap.keySet().contains(value)){
            printStartLableCode(keywordLableMap.get(value), PrintType.AFTER_PRINT);

        }else if(statements.keySet().contains(value)){
            if(!currentType.equals("statements")){
                printStartLableCode("statements", PrintType.NO_PRINT);
            }
            printStartLableCode(statements.get(value), PrintType.AFTER_PRINT);
        }
        else if(value.equals("return")){
            while(true){
                if(LableUtil.getCurrentType().equals("statements")){
                    break;
                }
                printEndLable(PrintType.NO_PRINT, null);
            }
            printStartLableCode("returnStatement", PrintType.AFTER_PRINT);
        }
        else if(currentType.equals("expression") &&
                LableUtil.getTockenLable().equals("identifier")){
                 //<term> new
            printStartLableCode("term", PrintType.AFTER_PRINT);
        }
        else if(termEnd.contains(LableUtil.getTockenLableValue())){
            //变量
            if(currentType.equals("term") ){
                //打印结束标签
                printEndLable(PrintType.NO_PRINT,null);
            }
            if(currentType.equals("expression") ){
                //打印term开始标签
                printStartLableCode("term",null);
            }

            printStartLableCode("term", PrintType.BEFORE_PRINT);
        }
        else if(keywordConstant.contains(value) ||
                termList.contains(LableUtil.getTockenLable())){
            if(currentType.equals("term")){
                printCurrentCode();
            }else{
                //关键字处理
                printStartLableCode("term", PrintType.AFTER_PRINT);
                printEndLable(PrintType.NO_PRINT,null);
            }

        }
        else{
            printCurrentCode();
        }
    }

    public void symbolTypeStart(){
        String value = LableUtil.getTockenLableValue();
        String currentType = LableUtil.getCurrentType();


        if ("(".equals(value) && currentType.equals("subroutineDec")) {
            printStartLableCode("parameterList", PrintType.BEFORE_PRINT);
        }
        else if( "=".equals(value)){
            printStartLableCode( expressionMap.get(value), PrintType.BEFORE_PRINT);
        }
        else if("(".equals(value) && (LableUtil.getTockenLableValuePrevious().equals("new")
                || currentType.equals("doStatement")) ){
            printStartLableCode("expressionList", PrintType.BEFORE_PRINT);
        }
        else if ("(".equals(value) && (currentType.equals("ifStatement") || currentType.equals("term"))) {
            printStartLableCode("expression", PrintType.BEFORE_PRINT);
        }
        else if ("{".equals(value) && currentType.equals("subroutineDec") ) {
            printStartLableCode("subroutineBody", PrintType.AFTER_PRINT);
        }
        else if ("[".equals(value) ) {
            printStartLableCode("expression", PrintType.BEFORE_PRINT);
        }
        else {
            printCurrentCode();
        }

    }

    public void symbolTypeEnd(){
        String value = LableUtil.getTockenLableValue();
        String currentType = LableUtil.getCurrentType();
        List<String> typeList = Arrays.asList( "expression", "returnStatement","subroutineBody" );

        if(")".equals(value) &&currentType.equals("parameterList")){
                printEndLable(PrintType.AFTER_PRINT,SpaceType.CURRENT_SPACE);
        }
        else if(")".equals(value) && currentType.equals("expressionList")){
            //先答应标签 在打印代码  在打印标签
            printEndLable(PrintType.AFTER_PRINT,SpaceType.CURRENT_SPACE);

            if(LableUtil.getCurrentType().equals("term")){
                printEndLable(PrintType.NO_PRINT,SpaceType.CURRENT_SPACE); //打印 </term>
            }
        }
        else  if(")".equals(value) && currentType.equals("expression")){
            //if()
            printEndLable(PrintType.AFTER_PRINT, SpaceType.CURRENT_SPACE);
        }
        else  if("]".equals(value)){
            //数组
            printEndLable(PrintType.AFTER_PRINT, SpaceType.CURRENT_SPACE);
        }
        else if(")".equals(value) && currentType.equals("term") ){
            while(true){
                if(LableUtil.getCurrentType().equals("expression")){
                    printEndLable(PrintType.NO_PRINT, null);
                    break;
                }
                printEndLable(PrintType.NO_PRINT, null);
            }
            printEndLable(PrintType.BEFORE_PRINT,SpaceType.NEXT_SPACE);
        }
        else if(";".equals(value) && currentType.equals("term") ){
            while(true){
                if(LableUtil.getCurrentType().equals("expression")){
                    printEndLable(PrintType.NO_PRINT, null);
                    break;
                }
                printEndLable(PrintType.NO_PRINT, null);
            }
//            printEndLable(PrintType.NO_PRINT, null);
//            printEndLable(PrintType.NO_PRINT, null);
            printEndLable(PrintType.BEFORE_PRINT,SpaceType.NEXT_SPACE);

        }
        else if("}".equals(value) &&  currentType.equals("statements") &&
                LableUtil.getCurrentPreviousType().equals("ifStatement") ){
            //if statements
            printEndLable(PrintType.AFTER_PRINT,SpaceType.CURRENT_SPACE);
        }
        else if( ";}".contains(value) && typeList.contains(currentType)){
            if(currentType.equals("expression")){
                // ; expression
                printEndLable(PrintType.AFTER_PRINT,SpaceType.CURRENT_SPACE);
            }else{
                //;return 或 }subroutineBody
                printEndLable(PrintType.BEFORE_PRINT,SpaceType.NEXT_SPACE);
            }
            printEndLable(PrintType.NO_PRINT,SpaceType.CURRENT_SPACE);
        }
//        else if(";".equals(value) && currentType.equals("expression")){
//            printEndLable(PrintType.AFTER_PRINT,SpaceType.CURRENT_SPACE);
//            printEndLable(PrintType.NO_PRINT,SpaceType.CURRENT_SPACE);
//        }
//        else if(";".equals(value) && currentType.equals("returnStatement")  ){
//            printEndLable(PrintType.BEFORE_PRINT,SpaceType.NEXT_SPACE);
//            printEndLable(PrintType.NO_PRINT,SpaceType.CURRENT_SPACE);
//        }
//        else if("}".equals(value) && currentType.equals("subroutineBody")){
//            printEndLable(PrintType.BEFORE_PRINT,SpaceType.NEXT_SPACE);
//            printEndLable(PrintType.NO_PRINT,SpaceType.CURRENT_SPACE);
//        }
        else{
            //    <symbol> ; </symbol>
            //  </classVarDec>
            printEndLable(PrintType.BEFORE_PRINT, SpaceType.NEXT_SPACE);
        }


    }



    public void printCurrentCode(){
        System.out.println(LableUtil.getNextSpace()  +currentCode);
    }

    public void printEndLable(PrintType pt, SpaceType st) {
        String spaceType ="";
        if(st == SpaceType.CURRENT_SPACE){
            spaceType = currentSpace;
        }else{
            spaceType = LableUtil.getNextSpace();
        }

        if(PrintType.BEFORE_PRINT == pt){
            System.out.println(spaceType  +currentCode);
        }

        System.out.println(currentSpace + "</" + LableUtil.getCurrentType() + ">");
        type.remove(type.size() - 1);
        currentSpace = LableUtil.getPreviousSpace();

        if(PrintType.AFTER_PRINT == pt){
            //打印代码
            System.out.println(spaceType +currentCode);
        }
    }

    public void printStartLableCode(String newType, PrintType pt){
        LableUtil.addNewType(newType);

        if(PrintType.BEFORE_PRINT == pt){
            System.out.println(currentSpace +currentCode);
        }

        //打印标签
        System.out.println(currentSpace  +"<"+ LableUtil.getCurrentType()+">");

        if(PrintType.AFTER_PRINT == pt){
            //打印代码
            System.out.println(LableUtil.getNextSpace()  +currentCode);
        }
    }




   private static class LableUtil {
        public static void addNewType(String newType){
            type.add(newType);
            if(!newType.equals("class")){
                currentSpace = LableUtil.getNextSpace();
            }
        }

        //获取tockens标签
        private static  String getTockenLable() {
            String lable = currentCode.split(" ")[0];
            return lable.replace("<","").replace(">", "").trim();
        }
        //获取tockens标签中的值
        private static String getTockenLableValue() {
            return currentCode.split(" ")[1].trim();
        }
       private static String getTockenLableValuePrevious() {
           return previousCodeVlaue.split(" ")[1].trim();
       }

        //获取当前类型
        private static String getCurrentType(){
            if(type.size()>0){
                return type.get(type.size()-1);
            }
           return "";
        }
       private static String getCurrentPreviousType(){
           if(type.size()>0){
               return type.get(type.size()-2);
           }
           return "";
       }
        //获取下一行空格
        public static String getNextSpace(){
          return currentSpace+"  ";
        }
        //获取上一个空格
        public static String getPreviousSpace(){
            if(currentSpace.length()>0){
                return currentSpace.substring(0, currentSpace.length()-2);
            }
            return "";
        }

    }

   private enum PrintType{
        BEFORE_PRINT,
        AFTER_PRINT,
        NO_PRINT;
    }

   private enum SpaceType{
        CURRENT_SPACE,
        NEXT_SPACE;
    }

}
