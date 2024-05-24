package nand2tetris.compiler2;

import nand2tetris.compiler1.pubField.TokenType;
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

    private String currentFunctionReturnType;

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

        compileSubroutline();

        if(!getCurrentValue().equals("}"))
            throw new RuntimeException("class not have  end char }");
        vmWriter.close();
    }

    //编译整个方法、函数、构造函数
    public void compileSubroutline() {
        String lableDec = "subroutineDec";
        String lableDecBody = "subroutineBody";

        List<String> methods = Arrays.asList("constructor", "function", "method");
        symbolTable.startSubroutine();

        while (methods.contains(getCurrentValue())) {
            currentFunctionReturnType = getCurrentValue(); //type
            String functionName = getCurrentValue(); //functionName

            //(
            if (getCurrentValue().equals("(")) {
               int paramNum = compileParameterList();

                String vmFunctionName = String.join(".",currentClassName,functionName);
                vmWriter.writeFunction(vmFunctionName,paramNum);

                advance(); //)
            }

            if(!getCurrentValue().equals("{")){
                throw new RuntimeException(" method not have start char {");
            }
            compileStatements();

            if(!getCurrentValue().equals("}"))
                throw new RuntimeException(" method not have end char }");
        }

    }

    public void compileStatements(){

        List<String> methods= Arrays.asList("let", "if", "while", "do", "return");

        while(methods.contains(jackTokenizer.getPeekValue())){
            String statementKey = getCurrentValue();

            if(statementKey.equals("do")){
                compileDo();
            }

            if(statementKey.equals("return")){
                compileReturn();
            }
        }

    }

    public void compileDo(){
        String lableName = "doStatement";
        compileSubroutineCall();

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

            //保存结果
            vmWriter.writePop(SegmentType.TEMP,0);

            if(currentFunctionReturnType.equals("void")){
                vmWriter.writePush(SegmentType.CONST,0);
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
//        if(!jackTokenizer.getPeekValue().equals(")")){
//            addCurrentCode();//type
//            addCurrentCode();//varName
//
//            while(getPeekValue().equals(",")){
//                addCurrentCode();//,
//                addCurrentCode();//type
//                addCurrentCode();//varName
//            }
//
//        }

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

    public void compileExpression(){
        String lableName = "expression";
        List<String> op = Arrays.asList("+", "-", "*", "/", "&", "|", "<", ">", "=");
        compileTerm();

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
             vmWriter.writePush(SegmentType.CONST,Integer.valueOf(intVal));
        }
        if(jackTokenizer.getPeekValue().equals("(")){
            //()
            advance();//(
            compileExpression();
            advance();//)
        }

    }
}