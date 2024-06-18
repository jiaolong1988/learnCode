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

    private int whileCount = 0;
    private int ifCount = 0;
    //ture:表示 需要将 变量 赋值给 数组
    //fase:表示读取变量
    private boolean arrayLeftVarWriteFlag = false;
    //true:表示读取数组的值
    private boolean arrayRightVarReadFlag = false;
    //true:参数是数组
    private boolean arrayParmaFlag = false;


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
        compileSubroutine();

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

        while(fields.contains(jackTokenizer.getPeekValue())){
            String defineType = getCurrentValue();  // static/field
            String type = getCurrentValue(); //type
            String name = getCurrentValue(); //name
            symbolTable.define(name, type, defineType);

            while(jackTokenizer.getPeekValue().equals(",")){
                advance(); //,
                String name2 = getCurrentValue(); //name
                symbolTable.define(name2, type, defineType);
            }

            if(!getCurrentValue().equals(";")){
                throw new RuntimeException("class var not have ;");
            }
        }

    }

    //编译整个方法、函数、构造函数
    public void compileSubroutine() {
        List<String> methods = Arrays.asList("constructor", "function", "method");

        while (methods.contains(jackTokenizer.getPeekValue())) {
            String subroutineType = getCurrentValue();
            this.ifCount=0;
            this.whileCount=0;

            String suborutineReturnType = getCurrentValue(); //type
            String functionName = getCurrentValue(); //functionName
            symbolTable.startSubroutine(suborutineReturnType, functionName);

            //(
            if (getCurrentValue().equals("(")) {
                if(subroutineType.equals("method")){
                    //方法的第一个字段
                    symbolTable.define("object-reference", "method-first-element", "arg");
                }
                compileParameterList();

                advance(); //)
            }

            if(!getCurrentValue().equals("{")){
                throw new RuntimeException(" method not have start char {");
            }
            compileVarDec();

            //方法标识
            int varCount = symbolTable.varCount("var");
            String vmFunctionName = String.join(".",currentClassName, symbolTable.getSubroutineName());
            vmWriter.writeFunction(vmFunctionName, varCount);
            if(subroutineType.equals("method")){
//                push argument 0
//                pop pointer 0

                //方法的第一个参数 是对象的地址及this
                vmWriter.writePush(SegmentType.ARG,0);
                vmWriter.writePop(SegmentType.POINTER,0);
            }

            if(subroutineType.equals("constructor")){
//                push constant 2
//                call Memory.alloc 1
//                pop pointer 0     //this
                int index = symbolTable.varCount("field");

                //构造函数，给创建的对象分配控件
                vmWriter.writePush(SegmentType.CONSTANT,index);
                vmWriter.writeCall("Memory.alloc",1);
                vmWriter.writePop(SegmentType.POINTER, 0); //将创建的 内存空间地址 赋值给this
            }

            compileStatements();

            if(!getCurrentValue().equals("}"))
                throw new RuntimeException(" method not have end char } 当前值为："+getCurrentValue());
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

        while(methods.contains(jackTokenizer.getPeekValue())){
            String statementKey = getCurrentValue();

            if(statementKey.equals("do")){
                compileDo();
            }
            if(statementKey.equals("if")){
                compileIf();
            }

            if(statementKey.equals("while")){
                compileWhile();
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
        compileSubroutineCall();

        //保存结果
        vmWriter.writePop(SegmentType.TEMP,0);

        if(jackTokenizer.getPeekValue().equals(";")){
            advance();
        }
    }

    public void compileLet(){
        String varname = null; // varName
        String nextValue = jackTokenizer.getNextValue(); // 变量的下一个值

        if(nextValue.equals("[")){
            //数组 a[i]
            varname = jackTokenizer.getPeekValue();
            arrayLeftVarWriteFlag = true;

            compileExpression();

        }else{
            varname = getCurrentValue();
        }

        if(jackTokenizer.getPeekValue().equals("]")){
            getCurrentValue();
        }

        if(jackTokenizer.getPeekValue().equals("=")){
            arrayRightVarReadFlag = true;
            advance();
            compileExpression();
            arrayRightVarReadFlag = false;

           //数组 ] 处理
            if(jackTokenizer.getPeekValue().equals("]")){
                getCurrentValue();
            }

           //数组的赋值处理
           if(arrayLeftVarWriteFlag){
               //保存表达式的结果，也就是等号 右边的结果.      pop temp 0
               vmWriter.writePop(SegmentType.TEMP, 0);
               //保存数组元素的地址 到 that.                pop pointer 1
               vmWriter.writePop(SegmentType.POINTER, 1);
               //将等号 右边的结果放入到 栈                  push temp 0
               vmWriter.writePush(SegmentType.TEMP, 0);
               //将值 赋值给数组                           pop that 0
               vmWriter.writePop(SegmentType.THAT, 0);
               arrayLeftVarWriteFlag = false;
           }else{
               //保存结果
               int index = symbolTable.indexOf(varname);
               SegmentType segmentType = symbolTable.kindOf(varname);
               if(segmentType==null){
                   throw new RuntimeException("SegmentType is null. "+segmentType+"-----> varname:"+varname);
               }
               vmWriter.writePop(segmentType, index);
           }


        }

        if(jackTokenizer.getPeekValue().equals(";")){
           advance();
        }


    }

    public void compileIf(){

        String ifTrue = "IF_TRUE"+ifCount;
        String ifFalse = "IF_FALSE"+ifCount;
        String ifEnd = "IF_END"+ifCount;

        ifCount++;
        if(!getCurrentValue().equals("(")){
            throw new RuntimeException("if statuments not have (");
        }
        compileExpression();

        advance();//)
        /**
         * if-goto IF_TRUE0
         * goto IF_FALSE0
         * label IF_TRUE0
         * **/
        vmWriter.writeIf(ifTrue);
        vmWriter.writeGoto(ifFalse);
        vmWriter.writeLabel(ifTrue);

        //{
        printErrorInfo(getCurrentValue(),"{","if not exit end lable, {");
        compileStatements();
        //}
        printErrorInfo(getCurrentValue(),"}","if not exit end lable, }");

        if(jackTokenizer.getPeekValue().equals("else")){
/*            goto IF_END0
            label IF_FALSE0
            label IF_END0*/

            vmWriter.writeGoto(ifEnd);
            vmWriter.writeLabel(ifFalse);


            advance();//else
            advance();//{

            compileStatements();
            advance();//}

            vmWriter.writeLabel(ifEnd);

        }else{
            //if }结尾
            vmWriter.writeLabel(ifFalse);
        }


    }

    public void compileWhile(){
        String whileStart = "WHILE_EXP"+whileCount;
        String whileEnd = "WHILE_END"+whileCount;
        whileCount++;

        vmWriter.writeLabel(whileStart);

        if(!getCurrentValue().equals("(")){
            new RuntimeException("while not exit end lable, (");
        }
        compileExpression();

        if(!getCurrentValue().equals(")")){
            new RuntimeException("while not exit end lable, )");
        }
        vmWriter.writeArithmetic(ArithmeticOperate.NOT);
        vmWriter.writeIf(whileEnd);

        if(!getCurrentValue().equals("{")){
            new RuntimeException("while not exit end lable, }");
        }
        compileStatements();

        if(getCurrentValue().equals("}")){
            //goto WHILE_EXP0
            //label WHILE_END0
            vmWriter.writeGoto(whileStart);
            vmWriter.writeLabel(whileEnd);

        }else{
            new RuntimeException("while not exit end lable, }");
        }
    }


    public void compileReturn(){

        if(!jackTokenizer.getPeekValue().equals(";")){
            compileExpression();
        }

        advance();
        if(symbolTable.getSubroutineReturnType().equals("void")){
            vmWriter.writePush(SegmentType.CONSTANT,0);
        }
//        if(symbolTable.getSubroutineName().equals("new")){
//            //push pointer 0
//            vmWriter.writePush(SegmentType.POINTER,0);
//        }
        vmWriter.writeReturn();

    }

    public void compileSubroutineCall(){
        String functionName = "";

        boolean flag = false;
       //aa.bb();
        String fname = getCurrentValue();//aa
        if(jackTokenizer.getPeekValue().equals(".")){

            //判断是类 还是 变量，如果是变量 查询其位置
            String varType = symbolTable.typeOf(fname);
            if( varType !=null){
                flag = true;

                //获取位置
                SegmentType type = symbolTable.kindOf(fname);
                int index = symbolTable.indexOf(fname);
                vmWriter.writePush(type,index);

                fname = varType;
            }

            String fchar = getCurrentValue();//.
            String sfname = getCurrentValue();//bb

            functionName = String.join(fchar,fname,sfname);
        }else{
            flag = true;

            //push pointer 0
            vmWriter.writePush(SegmentType.POINTER, 0);
            functionName = String.join(".",currentClassName,fname);;
        }

        if(getCurrentValue().equals("(")){ //(
            int paramNum = compileExpressionList();
            if(flag){
                paramNum++;
            }
            vmWriter.writeCall(functionName, paramNum);
        }


        if(jackTokenizer.getPeekValue().equals(")")){
            advance();
        }else{
            throw new RuntimeException("do method not have ), 当前值："+jackTokenizer.getPeekValue());
        }
    }

    /**
     * 编译参数列表不包含 ( )
     * @return: 返回参数数量
     **/
    public void compileParameterList(){

        int parameterNum = 0;
        if(!jackTokenizer.getPeekValue().equals(")")){
            String type = getCurrentValue();//type
            String varname = getCurrentValue();//varName
            symbolTable.define(varname, type, "arg");
            parameterNum++;

            while(jackTokenizer.getPeekValue().equals(",")){
                advance(); //,

                String type1 = getCurrentValue();//type
                String varname1 = getCurrentValue();//varName
                symbolTable.define(varname1, type1, "arg");

                parameterNum++;
            }

        }

       // return parameterNum;
    }

    /**
     * 参数列表
     * @return: int
     * @date 2024/6/8 19:26
     */
    public int compileExpressionList(){

        arrayParmaFlag = true;
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
        arrayParmaFlag = false;
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
        if(InitData.op.containsKey(operate)){
            vmWriter.writeArithmetic(InitData.op.get(operate));
        }
        if("*".equals(operate)){
            vmWriter.writeCall("Math.multiply",2);
        }
        if("/".equals(operate)){
            vmWriter.writeCall("Math.divide",2);
        }
    }

    public void compileTerm(){
        List<String> keywordConstant= Arrays.asList("true", "false", "null", "this");
        List<String> unaryOp = Arrays.asList("-", "~");
        List<String> op = Arrays.asList("+", "-", "*", "/", "&", "|", "<", ">", "=");
        List<String> varNameEndChar = Arrays.asList(")", ";",",","<;",">","]");


        String peekValue = jackTokenizer.getPeekValue();
        String nextValue = jackTokenizer.getNextValue();
        TokenType peekType = jackTokenizer.getPeekType();

        //数字
         if(peekType == TokenType.INT_CONST){
             String intVal = getCurrentValue();//数字值
             vmWriter.writePush(SegmentType.CONSTANT, Integer.valueOf(intVal));
        }
        else if(peekType == TokenType.STRING_CONST){
            //字符串的值
             String stringVal = getCurrentValue();
            int x =  stringVal.toCharArray().length;
             vmWriter.writePush(SegmentType.CONSTANT, stringVal.length());
             vmWriter.writeCall("String.new", 1);

             for (char c : stringVal.toCharArray()) {
                 vmWriter.writePush(SegmentType.CONSTANT,Integer.valueOf(c) );
                 vmWriter.writeCall("String.appendChar", 2);
             }
        }
         //非
        else if(unaryOp.contains(peekValue) ){
             String unaryOpChar = getCurrentValue(); //符号
             compileTerm();

             if(unaryOpChar.equals("-")){
                 vmWriter.writeArithmetic(ArithmeticOperate.NEG);
             }
             else if(unaryOpChar.equals("~")){
                 vmWriter.writeArithmetic(ArithmeticOperate.NOT);
             }else{
                 throw new RuntimeException("unaryOp not have operate instructions");
             }

         }
        //subroutineCall 处理
       else if(peekType == TokenType.IDENTIFIER &&
                (nextValue.equals(".") || nextValue.equals("(")) ){
            //subroutineCall 处理
            compileSubroutineCall();
        }
       //数组处理
         else if (peekType == TokenType.IDENTIFIER && nextValue.equals("[")){
             //varName[]
             String varname = getCurrentValue(); //varname
             int index = symbolTable.indexOf(varname);
             SegmentType segmentType = symbolTable.kindOf(varname);

             getCurrentValue();//[
             compileExpression();

             vmWriter.writePush(segmentType, index);
             vmWriter.writeArithmetic(ArithmeticOperate.ADD);

             //判断数组类型 ture:二维数组， false:非二维数组
             boolean array2DFlag = jackTokenizer.getNextValue().equals("]");

             //等号右边的 数组 二维数组,与 等号左边的二维数组 都需要
             if(array2DFlag || arrayRightVarReadFlag || arrayParmaFlag){
                 //pop pointer 1    将数组地址写入 that
                 vmWriter.writePop(SegmentType.POINTER,1);
                 // push that 0     读取数组的值
                 vmWriter.writePush(SegmentType.THAT, 0);

             }

//             //等号右边 去除二维数组的 ]
//             if(arrayRightVarReadFlag && (
//                     op.contains((jackTokenizer.getNextValue())) || jackTokenizer.getNextValue().equals(";")  //二维数组
//             )){
//                 advance(); //]
//             }
//
//             if(array2DFlag ){
//                 advance(); //]
//             }


             //等号右边 去除所有 ]
             if(arrayRightVarReadFlag || arrayParmaFlag){
                  if( jackTokenizer.getPeekValue().equals("]")) {
                      advance(); //]
                  }

             }else{
                 // 等号左边的数组处理 要保留一个 ]
                 if( jackTokenizer.getNextValue().equals("]")) {
                     advance(); //]
                 }
             }

         }
            //varname
       else if (peekType == TokenType.IDENTIFIER &&
                (op.contains( nextValue) || varNameEndChar.contains(nextValue) )  ) {

            String varname = getCurrentValue();
            int index = symbolTable.indexOf(varname);
            SegmentType segmentType = symbolTable.kindOf(varname);

            vmWriter.writePush(segmentType, index);
        }
        //关键字常量
       else if(keywordConstant.contains(peekValue)){
            String value = getCurrentValue();
            if(value.equals("null") || value.equals("false") ){
                vmWriter.writePush(SegmentType.CONSTANT, 0);
            }
            if(value.equals("true") ){
                vmWriter.writePush(SegmentType.CONSTANT, 0);
                vmWriter.writeArithmetic(ArithmeticOperate.NOT);
            }
             if(value.equals("this") ){
                 //push pointer 0
                 vmWriter.writePush(SegmentType.POINTER, 0);
             }
        }
        //表达式 (
       else if(jackTokenizer.getPeekValue().equals("(")){
            //()
            advance();//(
            compileExpression();
            advance();//)
        }
       else{
           throw new RuntimeException(" Term analysis fail.");
         }

    }

    private void printErrorInfo(String newInfo, String oldInfo, String errorInfo) {
        if(!newInfo.equals(oldInfo)){
            new RuntimeException(errorInfo);
        }
    }
}