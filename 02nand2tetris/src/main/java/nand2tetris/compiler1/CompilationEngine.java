package nand2tetris.compiler1;

import java.io.File;
import java.util.*;

/**
 * 自顶向下的递归语法分析器
 * @author jiaolong
 * @date 2024/04/06 11:02
 */
public class CompilationEngine {
    //类别定义
    private LinkedHashMap<String, String> typeDef = new LinkedHashMap();
    private HashMap<String, String> keywordLableMap = new HashMap<>();
    private HashMap<String, String> statements = new HashMap<>();

    private HashMap<String, String> expressionMap = new HashMap<>();

   // List statementKeyWords = Arrays.asList("let","","","");
    String currentCode = "";
    File inputFile=null;

    public CompilationEngine(File inputFile, File outFile){
        this.inputFile = inputFile;

        keywordLableMap.put("class","class");
        keywordLableMap.put("static","classVarDec");
        keywordLableMap.put("function","subroutineDec");
        keywordLableMap.put("var","varDec");

        statements.put("let","letStatement");
        statements.put("do","doStatement");
        statements.put("return","returnStatement");


        expressionMap.put("=","expression");
    }

    public void getInfo(){
        List<String> jackCodes = ReadJackFileUtil.readJackFile(inputFile);
        for(String code: jackCodes){
            if(!code.contains("tokens")){
                currentCode = code;
                //System.out.println("--> "+code);
                if(code.contains("keyword")){
                    keywordsHandle();

                }else if(code.contains("symbol")){
                    symbolHandle();

                }else{
                    printCurrentCode();
                }
            }
        }
    }

    //符号处理
    private void symbolHandle() {
        String value = currentCode.split(" ")[1].trim();
        if(value.equals(";")){
            printLable("e");
        }

        else if(value.equals("(")){
            setStartLableList("parameterList");
        }

        else if(value.equals(")")){
            printLableList("e");

            if (getCurrentKey().equals("term")) {
                printOnlyCurrentLable("e");
                printOnlyCurrentLable("e");
            }
        }

        else if(value.equals("{")){
            if(getCurrentKey().equals("subroutineDec")){
               setStartLable("subroutineBody" );
            }else{
                printCurrentCode();
            }
        }
        else if(value.equals("=")){
            printCurrentCode();
            if(!getCurrentKey().equals("expression")){
                setStartOnlyLable("expression");
            }
            setStartOnlyLable("term");

        }
        else{
            printCurrentCode();
        }


    }

    //关键字处理
    private void keywordsHandle() {
        String value = currentCode.split(" ")[1].trim();

        if(keywordLableMap.keySet().contains(value)){
            setStartLable(keywordLableMap.get(value));
        }
        else if(statements.keySet().contains(value)){
            if(!getCurrentKey().equals("statements")){
                setStartOnlyLable("statements");
            }

            setStartLable(statements.get(value));
        }
        else{
            printCurrentCode();
        }
    }


    public void printCurrentCode(){
        System.out.println(getNextSpace()  +currentCode);
    }
    //仅打印标签
    public void printOnlyCurrentLable(String type){
        if(type.equals("s")){
            System.out.println(getCurrentSpace()  +"<"+getCurrentKey()+">");
        }else{
            System.out.println(getCurrentSpace()  +"</"+getCurrentKey()+">");
            typeDef.remove(getCurrentKey());
        }

    }

    //标签信息
    public void printLable(String type){
        if(type.equals("s")){
            //正标题
            System.out.println(getCurrentSpace()  +"<"+getCurrentKey()+">");
            System.out.println(getNextSpace()  +currentCode);
        }else{
            //反标题
            System.out.println(getNextSpace()  +currentCode);
            System.out.println(getCurrentSpace()+"</"+getCurrentKey()+">");

            typeDef.remove(getCurrentKey());
        }
    }

    public void printLableList(String type){
        if(type.equals("s")){
            System.out.println(getCurrentSpace()  +currentCode);
            System.out.println(getCurrentSpace()+"<"+getCurrentKey()+">");

        }else{
            System.out.println(getCurrentSpace()  +"</"+getCurrentKey()+">");
            System.out.println(getCurrentSpace()  +currentCode);

            typeDef.remove(getCurrentKey());
        }
    }



//    private String getLastValue(){
//        String newestKey = "";
//        int length = typeDef.keySet().size();
//        int i=0;
//        for (String key : typeDef.keySet()) {
//
//            if(i==(length-2)){
//                newestKey =typeDef.get(key) ;
//            }
//            i++;
//        }
//        return newestKey;
//    }


    //设置开始标签信息
    private void setStartLable(String lable){
        if(lable.equals("class")){
            typeDef.put(lable,"");
        }else{
            typeDef.put(lable,getNextSpace());
        }
        printLable("s");
    }

    private void setStartLableList(String lable){
        typeDef.put(lable,getNextSpace());
        printLableList("s");
    }

    //设置statements标签
    private void setStartOnlyLable(String lable){
        typeDef.put(lable, getNextSpace());
        printOnlyCurrentLable("s");
    }

    //获取当前类型
    private String getCurrentKey(){
        String newestKey = null;

        for (String key : typeDef.keySet()) {
            newestKey = key;
        }
        return newestKey;
    }
    //获取当前空格
    private String getCurrentSpace(){
        String newestKey = getCurrentKey();
        return typeDef.get(newestKey);
    }
    //获取下一行空格
    public String getNextSpace(){
        return getCurrentSpace()+"  ";
    }



}
