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
 * �Զ����µĵݹ��﷨������
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
    //ture:��ʾ ��Ҫ�� ���� ��ֵ�� ����
    //fase:��ʾ��ȡ����
    private boolean arrayLeftVarWriteFlag = false;
    //true:��ʾ��ȡ�����ֵ
    private boolean arrayRightVarReadFlag = false;
    //true:����������
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
     * ���뾲̬�������ֶ�����
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

    //�����������������������캯��
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
                    //�����ĵ�һ���ֶ�
                    symbolTable.define("object-reference", "method-first-element", "arg");
                }
                compileParameterList();

                advance(); //)
            }

            if(!getCurrentValue().equals("{")){
                throw new RuntimeException(" method not have start char {");
            }
            compileVarDec();

            //������ʶ
            int varCount = symbolTable.varCount("var");
            String vmFunctionName = String.join(".",currentClassName, symbolTable.getSubroutineName());
            vmWriter.writeFunction(vmFunctionName, varCount);
            if(subroutineType.equals("method")){
//                push argument 0
//                pop pointer 0

                //�����ĵ�һ������ �Ƕ���ĵ�ַ��this
                vmWriter.writePush(SegmentType.ARG,0);
                vmWriter.writePop(SegmentType.POINTER,0);
            }

            if(subroutineType.equals("constructor")){
//                push constant 2
//                call Memory.alloc 1
//                pop pointer 0     //this
                int index = symbolTable.varCount("field");

                //���캯�����������Ķ������ؼ�
                vmWriter.writePush(SegmentType.CONSTANT,index);
                vmWriter.writeCall("Memory.alloc",1);
                vmWriter.writePop(SegmentType.POINTER, 0); //�������� �ڴ�ռ��ַ ��ֵ��this
            }

            compileStatements();

            if(!getCurrentValue().equals("}"))
                throw new RuntimeException(" method not have end char } ��ǰֵΪ��"+getCurrentValue());
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

        //������
        vmWriter.writePop(SegmentType.TEMP,0);

        if(jackTokenizer.getPeekValue().equals(";")){
            advance();
        }
    }

    public void compileLet(){
        String varname = null; // varName
        String nextValue = jackTokenizer.getNextValue(); // ��������һ��ֵ

        if(nextValue.equals("[")){
            //���� a[i]
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

           //���� ] ����
            if(jackTokenizer.getPeekValue().equals("]")){
                getCurrentValue();
            }

           //����ĸ�ֵ����
           if(arrayLeftVarWriteFlag){
               //������ʽ�Ľ����Ҳ���ǵȺ� �ұߵĽ��.      pop temp 0
               vmWriter.writePop(SegmentType.TEMP, 0);
               //��������Ԫ�صĵ�ַ �� that.                pop pointer 1
               vmWriter.writePop(SegmentType.POINTER, 1);
               //���Ⱥ� �ұߵĽ�����뵽 ջ                  push temp 0
               vmWriter.writePush(SegmentType.TEMP, 0);
               //��ֵ ��ֵ������                           pop that 0
               vmWriter.writePop(SegmentType.THAT, 0);
               arrayLeftVarWriteFlag = false;
           }else{
               //������
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
            //if }��β
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

            //�ж����� ���� ����������Ǳ��� ��ѯ��λ��
            String varType = symbolTable.typeOf(fname);
            if( varType !=null){
                flag = true;

                //��ȡλ��
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
            throw new RuntimeException("do method not have ), ��ǰֵ��"+jackTokenizer.getPeekValue());
        }
    }

    /**
     * ��������б����� ( )
     * @return: ���ز�������
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
     * �����б�
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

            //��ѧ����
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

        //����
         if(peekType == TokenType.INT_CONST){
             String intVal = getCurrentValue();//����ֵ
             vmWriter.writePush(SegmentType.CONSTANT, Integer.valueOf(intVal));
        }
        else if(peekType == TokenType.STRING_CONST){
            //�ַ�����ֵ
             String stringVal = getCurrentValue();
            int x =  stringVal.toCharArray().length;
             vmWriter.writePush(SegmentType.CONSTANT, stringVal.length());
             vmWriter.writeCall("String.new", 1);

             for (char c : stringVal.toCharArray()) {
                 vmWriter.writePush(SegmentType.CONSTANT,Integer.valueOf(c) );
                 vmWriter.writeCall("String.appendChar", 2);
             }
        }
         //��
        else if(unaryOp.contains(peekValue) ){
             String unaryOpChar = getCurrentValue(); //����
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
        //subroutineCall ����
       else if(peekType == TokenType.IDENTIFIER &&
                (nextValue.equals(".") || nextValue.equals("(")) ){
            //subroutineCall ����
            compileSubroutineCall();
        }
       //���鴦��
         else if (peekType == TokenType.IDENTIFIER && nextValue.equals("[")){
             //varName[]
             String varname = getCurrentValue(); //varname
             int index = symbolTable.indexOf(varname);
             SegmentType segmentType = symbolTable.kindOf(varname);

             getCurrentValue();//[
             compileExpression();

             vmWriter.writePush(segmentType, index);
             vmWriter.writeArithmetic(ArithmeticOperate.ADD);

             //�ж��������� ture:��ά���飬 false:�Ƕ�ά����
             boolean array2DFlag = jackTokenizer.getNextValue().equals("]");

             //�Ⱥ��ұߵ� ���� ��ά����,�� �Ⱥ���ߵĶ�ά���� ����Ҫ
             if(array2DFlag || arrayRightVarReadFlag || arrayParmaFlag){
                 //pop pointer 1    �������ַд�� that
                 vmWriter.writePop(SegmentType.POINTER,1);
                 // push that 0     ��ȡ�����ֵ
                 vmWriter.writePush(SegmentType.THAT, 0);

             }

//             //�Ⱥ��ұ� ȥ����ά����� ]
//             if(arrayRightVarReadFlag && (
//                     op.contains((jackTokenizer.getNextValue())) || jackTokenizer.getNextValue().equals(";")  //��ά����
//             )){
//                 advance(); //]
//             }
//
//             if(array2DFlag ){
//                 advance(); //]
//             }


             //�Ⱥ��ұ� ȥ������ ]
             if(arrayRightVarReadFlag || arrayParmaFlag){
                  if( jackTokenizer.getPeekValue().equals("]")) {
                      advance(); //]
                  }

             }else{
                 // �Ⱥ���ߵ����鴦�� Ҫ����һ�� ]
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
        //�ؼ��ֳ���
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
        //���ʽ (
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