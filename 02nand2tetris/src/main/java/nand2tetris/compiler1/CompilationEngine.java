package nand2tetris.compiler1;

import nand2tetris.CommonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自顶向下的递归语法分析器
 * @author jiaolong
 * @date 2024/04/06 11:02
 */
public class CompilationEngine {

    public static final boolean printLog = true;

    private List<String> infos = new ArrayList<>();
    private String currentSpace="";

    List<String> tockens = null;
    private int localCount=0;

    File inputFile = null;
    public CompilationEngine(File inputFile, File outFile) {
        List<String> list = CommonUtils.readFile(inputFile.toPath());
        tockens = list.stream().filter(t -> !t.contains("tokens")).collect(Collectors.toList());
        this.inputFile = inputFile;
    }

    public String getPeekType(){
        String lable = tockens.get(localCount).split(" ")[0];
        return lable.replace("<","").replace(">", "").trim();
    }

    public String getPeekValue(){
        String info = tockens.get(localCount);
        return  info.split(" ")[1].trim();
    }

    public String getNextValue(){
        String info = tockens.get(localCount+1);
        return info.split(" ")[1].trim();
    }


    public void compileClass () {
        String lableName = "class";
        if(!getPeekValue().equals("class")){
            throw new RuntimeException("class defined error.");
        }

        startLableNextElementAddTwoSpace(lableName);
        addCurrentCode();//class
        addCurrentCode();//main

        if(getPeekValue().equals("{")){
            addCurrentCode();//{
            compileClassVarDec();
            compileSubroutline();
        }

        addCurrentCode();//}
        endLableSpaceLenthSubTwo(lableName);

        ReadJackFileUtil.outputFile(inputFile.toPath(), infos,"_JL");
    }


    /**
     * 编译静态声名或字段声名
     * @return: void
     * @date 2024/4/6 11:12
     */
    public void compileClassVarDec(){
        List<String> fields= Arrays.asList("static","field");
        String lableName = "classVarDec";

        while(fields.contains(getPeekValue())){
            startLableNextElementAddTwoSpace(lableName);

            addCurrentCode();  //static
            addCurrentCode();  //type
            addCurrentCode();  //varName
            while(getPeekValue().equals(",")){
               addCurrentCode();  //,
               addCurrentCode();  //varName
            }
            if(getPeekValue().equals(";") ){
                addCurrentCode();
                endLableSpaceLenthSubTwo(lableName);
            }
        }

    }

    //编译整个方法、函数、构造函数
    public void compileSubroutline() {
        String lableDec = "subroutineDec";
        String lableDecBody = "subroutineBody";

        List<String> methods = Arrays.asList("constructor", "function", "method");

        while (methods.contains(getPeekValue())) {
            startLableNextElementAddTwoSpace(lableDec);

            addCurrentCode(); //function
            addCurrentCode(); //type
            addCurrentCode(); //functionName
            if (getPeekValue().equals("(")) {
                addCurrentCode();
                compileParameterList();
            }
            if (getPeekValue().equals("{")) {
                //开始标签
                startLableNextElementAddTwoSpace(lableDecBody);
                addCurrentCode();//{

                compileVarDec();

                compileStatements();
            }

            if (getPeekValue().equals("}")) {
                addCurrentCode();//}

                endLableSpaceLenthSubTwo(lableDecBody);
                endLableSpaceLenthSubTwo(lableDec);
            } else {
                throw new RuntimeException("method difine error.");
            }

        }

    }

    //编译参数列表不包含 ( )
    public void compileParameterList(){
        String lableName = "parameterList";
        startLableNextElementAddTwoSpace(lableName);

        if(!getPeekValue().equals(")")){
            addCurrentCode();//type
            addCurrentCode();//varName

            while(getPeekValue().equals(",")){
                addCurrentCode();//,
                addCurrentCode();//type
                addCurrentCode();//varName
            }

        }

        if(getPeekValue().equals(")")){
            endLableSpaceLenthSubTwo(lableName);
            addCurrentCode();
        }

    }

    public void compileVarDec(){
        String lableName = "varDec";

        while(getPeekValue().equals("var")){
            startLableNextElementAddTwoSpace(lableName);

           addCurrentCode(); //var
           addCurrentCode(); //type
           addCurrentCode(); //name

            while(getPeekValue().equals(",")){
               addCurrentCode(); //,
               addCurrentCode(); //name
            }
            if(getPeekValue().equals(";")){
               addCurrentCode(); //;
                endLableSpaceLenthSubTwo(lableName);
            }
        }

    }

    public void compileStatements(){
        String lableName = "statements";
        startLableNextElementAddTwoSpace(lableName);

        List<String> methods= Arrays.asList("let", "if", "while", "do", "return");
        while(methods.contains(getPeekValue())){
            if(getPeekValue().equals("let")){
                compileLet();
            }
            else if(getPeekValue().equals("if")){
                compileIf();
            }
            else if(getPeekValue().equals("while")){
                compileWhile();
            }
            else if(getPeekValue().equals("do")){
                compileDo();
            }
            else if(getPeekValue().equals("return")){
                compileReturn();
            }
            else{
                throw new RuntimeException("statements Syntax error. keyword: "+getPeekValue());
            }

        }

        endLableSpaceLenthSubTwo(lableName);
    }

    public void compileDo(){
        String lableName = "doStatement";
        startLableNextElementAddTwoSpace(lableName);

        addCurrentCode(); //do
        compileSubroutineCall();
        if(getPeekValue().equals(";")){
            addCurrentCode();
            endLableSpaceLenthSubTwo(lableName);
        }
    }

    public void compileLet(){
        String lableName = "letStatement";
        startLableNextElementAddTwoSpace(lableName);

        addCurrentCode(); // let
        addCurrentCode(); // varName

        if(getPeekValue().equals("[")){
            addCurrentCode(); // [
            compileExpression();
            addCurrentCode(); // ]
        }
        if(getPeekValue().equals("=")){
            addCurrentCode(); // =
            compileExpression();
        }

        if(getPeekValue().equals(";")){
            addCurrentCode();
            endLableSpaceLenthSubTwo(lableName);
        }


    }

    public void compileWhile(){
        String lableName = "whileStatement";
        startLableNextElementAddTwoSpace(lableName);

        addCurrentCode();//while
        if(getPeekValue().equals("(")){
            addCurrentCode();//(
            compileExpression();
            addCurrentCode();//)
        }

        if(getPeekValue().equals("{")){
            addCurrentCode();//{
            compileStatements();
        }


        if(getPeekValue().equals("}")){
            addCurrentCode();//}
            endLableSpaceLenthSubTwo(lableName);
        }else{
            new RuntimeException("if not exit end lable, }");
        }
    }

    public void compileReturn(){
        String lableName = "returnStatement";
        startLableNextElementAddTwoSpace(lableName);

        addCurrentCode(); //return
        if(!getPeekValue().equals(";")){
            compileExpression();
        }

        if(getPeekValue().equals(";")){
            addCurrentCode();
            endLableSpaceLenthSubTwo(lableName);
        }
    }

    public void compileIf(){
        String lableName = "ifStatement";
        startLableNextElementAddTwoSpace(lableName);

        addCurrentCode();//if
        if(getPeekValue().equals("(")){
            addCurrentCode();//(
            compileExpression();
            addCurrentCode();//)
        }

        if(getPeekValue().equals("{")){
            addCurrentCode();//{
            compileStatements();
        }

        if(getPeekValue().equals("}") && getNextValue().equals("else")){
            addCurrentCode();//}
            addCurrentCode();//else
            addCurrentCode();//{

            compileStatements();
        }
        if(getPeekValue().equals("}")){
            addCurrentCode();//}
            endLableSpaceLenthSubTwo(lableName);
        }else{
            new RuntimeException("if not exit end lable, }");
        }

    }

    public void compileExpression(){
        String lableName = "expression";
        startLableNextElementAddTwoSpace(lableName);

        List<String> op = Arrays.asList("+", "-", "*", "/", "&", "|", "<", ">", "=");
        for (int i = 0; i < op.size(); i++) {
            String info = op.get(i);
            if (info.equals("<")) {
                op.set(i, "&lt;");
            } else if (info.equals(">")) {
                op.set(i, "&gt;");
            } else if (info.equals("&")) {
                op.set(i, "&amp;");
            }
        }

        compileTerm();

        while(op.contains(getPeekValue())){
            addCurrentCode(); //op
            compileTerm();
        }
        endLableSpaceLenthSubTwo(lableName);
    }

    public void compileTerm(){
        List<String> keywordConstant= Arrays.asList("true", "false", "null", "this");
        List<String> unaryOp = Arrays.asList("-", "~");
        List<String> op = Arrays.asList("+", "-", "*", "/", "&", "|", "<", ">", "=");

        List<String> varNameEndChar = Arrays.asList(")", ";",",","&lt;","&gt;","]");

        String lableName = "term";
        startLableNextElementAddTwoSpace(lableName);

        String currentType = getPeekType();
        String nextValue = getNextValue();
        if(currentType.equals("identifier") && (nextValue.equals(".") || nextValue.equals("("))){
            //subroutineCall 处理
            compileSubroutineCall();
        }
        else if(currentType.equals("stringConstant")){
            addCurrentCode();
        }
        else if(currentType.equals("integerConstant")){
            addCurrentCode();
        }
        else if(currentType.equals("keyword") && keywordConstant.contains(getPeekValue())){
            addCurrentCode();
        }else if(getPeekValue().equals("(")){
            //()
            addCurrentCode();//(
            compileExpression();
            addCurrentCode();//)
        }else if (currentType.equals("identifier") && nextValue.equals("[")){
            //varName[]
            addCurrentCode();//varname
            addCurrentCode();//[

            compileExpression();

            addCurrentCode();//]
        }else if(currentType.equals("identifier") && (op.contains(nextValue) || varNameEndChar.contains(nextValue) ) ){
            //varName + varName
            //(varName)
            //varName;
            //varName,varName
            addCurrentCode();

        }else if(unaryOp.contains(getPeekValue())){
            addCurrentCode(); // -
            compileTerm();
        }else{
            throw new RuntimeException("Term not found type.");
        }


        endLableSpaceLenthSubTwo(lableName);
    }

    public void compileExpressionList(){
        String lableName = "expressionList";
        startLableNextElementAddTwoSpace(lableName);

        if(!getPeekValue().equals(")")){
            compileExpression();
            while(getPeekValue().equals(",")){
                addCurrentCode();
                compileExpression();
            }
        }

        endLableSpaceLenthSubTwo(lableName);
    }
    public void compileSubroutineCall(){
        String nextValue = getNextValue();
        //aa.bb();
        if(nextValue.equals(".")){
            addCurrentCode(); //aa
            addCurrentCode(); //.
            addCurrentCode(); //bb


        }else if(nextValue.equals("(")){
            //(
            addCurrentCode();
        }

        if(getPeekValue().equals("(")){
            addCurrentCode(); //(
            compileExpressionList();
        }

        if(getPeekValue().equals(")")){
            addCurrentCode(); //)
        }
    }




    public void startLableNextElementAddTwoSpace(String lableName) {
        startLable(lableName);
        //空格长度加2
        currentSpace = currentSpace+"  ";
    }

    public void endLableSpaceLenthSubTwo(String lableName){
        //空格长度减2
        currentSpace = currentSpace.substring(0, currentSpace.length()-2);
        endLable(lableName);
    }

    public void addCurrentCode(){
        String info= currentSpace+tockens.get(localCount);
        localCount++;

        infos.add(info);
        printLog(info);
    }

    public void startLable(String lableName) {
        String info = currentSpace + "<"+lableName+">";
        infos.add(info);

        printLog(info);
    }
    public void endLable(String lableName){
        String info = currentSpace+"</"+lableName+">";
        infos.add(info);

        printLog(info);
    }
    private void printLog(String info) {
        if (printLog) {
            System.out.println(info);
        }
    }

}