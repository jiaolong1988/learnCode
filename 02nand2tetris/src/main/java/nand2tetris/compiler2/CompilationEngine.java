package nand2tetris.compiler2;

import nand2tetris.compiler1.pubField.TokenType;
import nand2tetris.compiler2.util.ArithmeticOperate;
import nand2tetris.compiler2.util.InitData;
import nand2tetris.compiler2.util.SegmentType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 自顶向下的递归语法分析器
 * @author jiaolong
 * @date 2024/04/06 11:02
 */
public class CompilationEngine {

    private JackTokenizer jackTokenizer;
    private SymbolTable symbolTable;
    private VmWriter vmWriter;

    private File inputFile = null;

    private InitData initData;
    public CompilationEngine(File inputFile, File outFile) {
        jackTokenizer = new JackTokenizer(inputFile);
        symbolTable = new SymbolTable();

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        vmWriter = new VmWriter(fileWriter);

        this.inputFile = inputFile;
    }

    private void advance(){
        jackTokenizer.advance();
    }
    private String getCurrentValue(){
         advance();
         return  jackTokenizer.getTockenValue();
    }

    private String currentClassName;

    public void compileClass () {
        String classKeyword = getCurrentValue();
        if(!classKeyword.equals("class")){
            throw new RuntimeException("class defined error.");
        }

        if(jackTokenizer.getPeekType() == TokenType.IDENTIFIER){
            currentClassName = getCurrentValue();
        }

        if(!getCurrentValue().equals("{"))
            throw new RuntimeException("class not have start end char {");
        compileClassVarDec();
        compileSubroutline();

        if(!getCurrentValue().equals("}"))
            throw new RuntimeException("class not have  end char }");
        vmWriter.close();
    }
    /**
     * 编译静态声名或字段声名
     * @return: void
     * @date 2024/4/6 11:12
            */
    public void compileClassVarDec(){
        List<String> fields= Arrays.asList("static","field");
        String lableName = "classVarDec";

        while(fields.contains(jackTokenizer.getPeekValue())){

        }

    }

    //编译整个方法、函数、构造函数
    public void compileSubroutline() {
        String lableDec = "subroutineDec";
        String lableDecBody = "subroutineBody";

        List<String> methods = Arrays.asList("constructor", "function", "method");

        while (methods.contains(getCurrentValue())) {

            String suborutineReturnType = getCurrentValue(); //type
            String functionName = getCurrentValue(); //functionName
            symbolTable.startSubroutine(suborutineReturnType,functionName);

            //(
            if (getCurrentValue().equals("(")) {
               compileParameterList();

                advance(); //)
            }

            if(!getCurrentValue().equals("{")){
                throw new RuntimeException(" method not have start char {");
            }
            compileVarDec();
            compileStatements();

            if(!getCurrentValue().equals("}"))
                throw new RuntimeException(" method not have end char }");
        }

    }

    public void compileVarDec(){

        while(jackTokenizer.getPeekValue().equals("var")){
            advance();  //var
            String type = getCurrentValue(); //type
            String name = getCurrentValue(); //name
            symbolTable.define(name, type, "var");

            while(jackTokenizer.getPeekValue().equals(",")){
                advance(); //,
                String name2 = getCurrentValue(); //name
                symbolTable.define(name2, type, "var");
            }
            if(jackTokenizer.getPeekValue().equals(";")){
              advance(); //;
            }
        }

    }

    public void compileStatements(){

        List<String> methods= Arrays.asList("let", "if", "while", "do", "return");

        int varCount = symbolTable.varCount("var");
        String vmFunctionName = String.join(".",currentClassName, symbolTable.getSubroutineName());
        vmWriter.writeFunction(vmFunctionName, varCount);

        while(methods.contains(jackTokenizer.getPeekValue())){
            String statementKey = getCurrentValue();

            if(statementKey.equals("do")){
                compileDo();
            }
            if(statementKey.equals("let")){
                compileLet();
            }
            if(statementKey.equals("return")){
                compileReturn();
            }
        }

    }

    public void compileDo(){
        String lableName = "doStatement";
        compileSubroutineCall();

        //保存结果
        vmWriter.writePop(SegmentType.TEMP,0);

        if(jackTokenizer.getPeekValue().equals(";")){
            advance();
        }
    }

    public void compileLet(){
        String lableName = "letStatement";

        String varname = getCurrentValue(); // varName

//        if(currentValue.equals("[")){
//            addCurrentCode(); // [
//            compileExpression();
//            addCurrentCode(); // ]
//        }

        if(jackTokenizer.getPeekValue().equals("=")){
           advance();
           compileExpression();
            //保存结果
            int index = symbolTable.indexOf(varname);
            SegmentType segmentType = symbolTable.kindOf(varname);
            vmWriter.writePop(segmentType,index);
        }

        if(jackTokenizer.getPeekValue().equals(";")){
           advance();
        }


    }
    public void compileReturn(){
        String lableName = "returnStatement";

        if(!jackTokenizer.getPeekValue().equals(";")){
            compileExpression();
        }else{
            advance();

            if(symbolTable.getSubroutineReturnType().equals("void")){
                vmWriter.writePush(SegmentType.CONSTANT,0);
            }

            vmWriter.writeReturn();
        }
    }

    public void compileSubroutineCall(){
       //aa.bb();
        String fname = getCurrentValue();//aa
        String fchar = getCurrentValue();//.
        String sfname = getCurrentValue();//bb
        if(getCurrentValue().equals("(")){ //(
            int paramNum = compileExpressionList();
            vmWriter.writeCall(String.join(fchar,fname,sfname), paramNum);
        }


        if(jackTokenizer.getPeekValue().equals(")")){
            advance();
        }
    }

    /**
     * 编译参数列表不包含 ( )
     * @return: 返回参数数量
     **/
    public int compileParameterList(){
        String lableName = "parameterList";
        String currentValue = jackTokenizer.getPeekValue();

        int parameterNum = 0;
        if(!jackTokenizer.getPeekValue().equals(")")){
            String type = getCurrentValue();//type
            String varname = getCurrentValue();//varName
            symbolTable.define(varname, type, SegmentType.ARG.toString().toLowerCase());
            parameterNum++;

            while(jackTokenizer.getPeekValue().equals(",")){
                advance(); //,

                String type1 = getCurrentValue();//type
                String varname1 = getCurrentValue();//varName
                symbolTable.define(varname1, type1, SegmentType.ARG.toString().toLowerCase());

                parameterNum++;
            }

        }

        return parameterNum;
    }
    public int compileExpressionList(){
        String lableName = "expressionList";
        int paramNum = 0;
        if(!jackTokenizer.getPeekValue().equals(")")){
            compileExpression();
            paramNum++;

            while(jackTokenizer.getPeekValue().equals(",")){
                paramNum++;
                advance();
                compileExpression();
            }
        }

        return paramNum;
    }

    /**
     * "expression"
     * @return: void
     * @date 2024/5/26 13:59
     */
    public void compileExpression(){
        compileTerm();

        List<String> op = Arrays.asList("+", "-", "*", "/", "&", "|", "<", ">", "=");
        while(op.contains(jackTokenizer.getPeekValue())){
            //a+(b*c)
            String operate = getCurrentValue();
            compileTerm();

            //数学计算
            arithmeticOperate(operate);
        }
    }

    private void arithmeticOperate(String operate) {
        if(initData.op.containsKey(operate)){
            vmWriter.writeArithmetic(initData.op.get(operate));
        }
        if("*".equals(operate)){
            vmWriter.writeCall("Math.multiply",2);
        }
    }

    public void compileTerm(){
        List<String> keywordConstant= Arrays.asList("true", "false", "null", "this");
        List<String> unaryOp = Arrays.asList("-", "~");
        List<String> op = Arrays.asList("+", "-", "*", "/", "&", "|", "<", ">", "=");

        List<String> varNameEndChar = Arrays.asList(")", ";",",","&lt;","&gt;","]");
        String lableName = "term";

         if(jackTokenizer.getPeekType() == TokenType.INT_CONST){
             String intVal = getCurrentValue();//数字值
             vmWriter.writePush(SegmentType.CONSTANT, Integer.valueOf(intVal));
        }
         //非
         if(unaryOp.contains(jackTokenizer.getPeekValue()) ){
             advance(); //符号
             String intVal = getCurrentValue();//数字值

             vmWriter.writePush(SegmentType.CONSTANT, Integer.valueOf(intVal));
             vmWriter.writeArithmetic(ArithmeticOperate.NEG);

         }
        //subroutineCall 处理
        if(jackTokenizer.getPeekType() == TokenType.IDENTIFIER &&
                (jackTokenizer.getNextValue().equals(".") || jackTokenizer.getNextValue().equals("("))
        ){
            //subroutineCall 处理
            compileSubroutineCall();
        }
        //varname
        if (jackTokenizer.getPeekType() == TokenType.IDENTIFIER &&
                jackTokenizer.getNextValue().equals(")") ) {

            String varname = getCurrentValue();
            int index = symbolTable.indexOf(varname);
            SegmentType segmentType = symbolTable.kindOf(varname);

            vmWriter.writePush(segmentType, index);
        }

        if(jackTokenizer.getPeekValue().equals("(")){
            //()
            advance();//(
            compileExpression();
            advance();//)
        }

    }
}