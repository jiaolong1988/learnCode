package nand2tetris.compiler1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 自顶向下的递归语法分析器
 * @author jiaolong
 * @date 2024/04/06 11:02
 */
public class CompilationEngine {
    //语句类型
    List<String> languageType = new ArrayList<>();
    List<String> level = new ArrayList<>();

    File inputFile=null;
    public CompilationEngine(File inputFile, File outFile ){
        level.add("");
        this.inputFile = inputFile;
    }

    public void getInfo(){
        List<String> jackCodes = ReadJackFileUtil.readJackFile(inputFile);
        for(String code: jackCodes){
            if(!code.contains("tokens")){
                // System.out.println("--> "+code);
                compileClass(code);
            }
        }
    }

    /**
     * 编译整个类
     * @return: void
     * @date 2024/4/6 11:11
     */
    public void compileClass(String code){
        boolean flag =true;
        String value = code.split(" ")[1].trim();
        if(code.contains("keyword")){

            if(value.equals("class")){
                System.out.println("<class>");
                level.add("  ");
                languageType.add("class");
            }

            if(value.equals("static")){
                compileClassVarDec("start");
            }
            if(value.equals("function")){
                compileSubroutline("start");
            }

            if(value.equals("var")){
                compileVarDec("start");
            }
            if(value.equals("let")){


            }
            if(value.equals("do")){


            }
            if(value.equals("return")){


            }
            if(value.equals("if")){


            }

            if(value.equals("else")){


            }
        }

        if(code.contains("symbol")){

            if(value.equals("(")){
                flag= false;
                System.out.println(getCurrentLevel()  +code);
                compileParameterList("start");
            }
            //表达式结束
            if(value.equals(")")){
                compileParameterList("end");
                languageType.remove(languageType.size()-1);
            }
            if(value.equals("{")){
                if(getCurrentLanguageType().equals("subroutineDec")){
                    languageType.add("subroutineBody");
                    System.out.println(getCurrentLevel() +"<"+getCurrentLanguageType()+">");
                    level.add("      ");
                }
            }
            //方法结束
            if(value.equals("}")){
                if(getCurrentLanguageType().equals("subroutineDec")){
                    flag= false;
                    System.out.println(getCurrentLevel()  +code);
                    System.out.println(getCurrentLevel() +"</"+getCurrentLanguageType()+">");
                    languageType.remove(languageType.size()-1);
                }
            }

        }

        if(flag){
            System.out.println(getCurrentLevel()  +code);
        }


        if(code.contains("symbol")){

            if(value.equals(";")){
                if(getCurrentLanguageType().equals("classVarDec")){

                    level.remove(level.size()-1);
                    compileClassVarDec("end");
                    languageType.remove(languageType.size()-1);
                }
                if(getCurrentLanguageType().equals("varDec")){
                    level.remove(level.size()-1);
                    compileVarDec("end");
                    languageType.remove(languageType.size()-1);
                }

            }
        }
    }

    public  String getCurrentLevel(){
       return level.get(level.size()-1);
    }
    public  String getCurrentLanguageType(){
        return languageType.get(languageType.size()-1);
    }

    /**
     * 编译静态声名或字段声名
     * @return: void
     * @date 2024/4/6 11:12
     */
    public void compileClassVarDec(String type){
        if(type.equals("start")){
            languageType.add("classVarDec");
            //System.out.println(getCurrentLevel() +"<classVarDec>");
            System.out.println(getCurrentLevel() +"<"+getCurrentLanguageType()+">");
            level.add("    ");

        }else{
             System.out.println(getCurrentLevel() +"</"+getCurrentLanguageType()+">");
        }


    }

    //编译整个方法、函数、构造函数
    public void compileSubroutline(String type){
        if(type.equals("start")){
            languageType.add("subroutineDec");
            //System.out.println(getCurrentLevel() +"<subroutineDec>");
            System.out.println(getCurrentLevel() +"<"+getCurrentLanguageType()+">");
            level.add("    ");

        }else{
            System.out.println(getCurrentLevel() +"</"+getCurrentLanguageType()+">");
        }
    }

    //编译参数列表不包含 ( )
    public void compileParameterList(String type){
        if(type.equals("start")){
            languageType.add("parameterList");
            System.out.println(getCurrentLevel() +"<"+getCurrentLanguageType()+">");
        }else{
            System.out.println(getCurrentLevel() +"</"+getCurrentLanguageType()+">");
        }


    }

    public void compileVarDec(String type){
        if(type.equals("start")){
            languageType.add("varDec");
            System.out.println(getCurrentLevel() +"<"+getCurrentLanguageType()+">");
            level.add("        ");
        }else{
            System.out.println(getCurrentLevel() +"</"+getCurrentLanguageType()+">");
        }
    }

    public void compileStatements(String type){

    }

    public void compileDo(String type){

    }

    public void compileLet(String type){

    }

    public void compileWhile(String type){

    }

    public void compileReturn(String type){

    }

    public void compileIf(String type){

    }

    public void compileExpression(){

    }

    public void compileTerm(){

    }

    public void compileExpressionList(){

    }


}
