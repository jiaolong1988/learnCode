package nand2tetris.vm2;


import nand2tetris.CommonUtils;
import nand2tetris.vm1.CmomandType;
import nand2tetris.vm1.StackSPUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jiaolong
 * @date 2024/03/10 13:31
 */
public class Vm2ServerMain {
    public static void main(String[] args) {
//        String file ="D:\\test\\08\\FunctionCalls\\SimpleFunction\\SimpleFunction.vm";
//        String file ="D:\\test\\08\\FunctionCalls\\FibonacciElement\\Main.vm";
//        file ="D:\\test\\08\\FunctionCalls\\FibonacciElement\\Sys.vm";
//
   //     ParserSingle(file);

        mergeTest();

    }

    public static void mergeTest(){
        //指令结合
        List<String> infoList = new ArrayList();

        //初始化操作
        init(infoList);

        //目录
        String dir = "D:\\test\\08\\FunctionCalls\\FibonacciElement";
        File dirFile = new File(dir);

        //获取目录下的 vm文件
        List<String> vmList = new ArrayList();
        for(File fp : dirFile.listFiles()){
            if(fp.getName().endsWith(".vm")){
                vmList.add(fp.getAbsolutePath());
            }
        }

        //获取解析信息
        for(String vmFile: vmList){
            CodeWriter cw = vmParserMerge(vmFile);
            List<String> info = cw.getVmInfo();
            infoList.addAll(info);
        }

        String ourFileAddress = String.join(File.separator,dir, dirFile.getName()+".vm");
        Path filePath = Paths.get(ourFileAddress);
        CommonUtils.outputFile(filePath, infoList, "asm");
    }

    /**
     * 初始化操作
     *  1.堆栈初始值为：256
     *  2.第一个执行程序必须为：Sys.init
     * @return: void
     **/
    public static void init(List <String> infoList){
        StringBuilder sb = new StringBuilder();
        sb.append("@256 D=A @SP M=D ")
        //    .append("@256 D=A @LCL M=D ")
            .append("@Sys.init 0;JMP")
        ;

        List info = Arrays.asList(sb.toString().split(" "));
        infoList.addAll(info);
    }

    /**
     * 文件夹 vm文件合并解析
     * @param: file
     * @return: void
     **/
    public static CodeWriter vmParserMerge(String file){
        //重置堆栈指针
        StackSPUtils.reset();

       Parser parser = new Parser(file);
       CodeWriter codeWriter = new CodeWriter(file);

       String functionName="";
        int lineNum=0;
        while(true){
            boolean flag = parser.advance();
            if(!flag ){
                break;
            }

            CmomandType currentCommantType = parser.commandType();
            if(currentCommantType == CmomandType.C_RETURNN){
                codeWriter.wirteReturn();

            }else{
                //vm 翻译为 汇编
                String arg1 = parser.arg1().trim();
                String arg2 = parser.arg2();

                //push 和 pop
                if(currentCommantType == CmomandType.C_PUSH || currentCommantType == CmomandType.C_POP){
                    codeWriter.writePushPop(parser.commandType(), arg1, arg2);
                }
                //算术类型指令
                if(currentCommantType == CmomandType.C_ARITHMETIC){
                    codeWriter.writerArithmetic(arg1);
                }

                //lable
                if(currentCommantType == CmomandType.C_LABLE){
                    codeWriter.writeLable(arg1, functionName);
                }

                //goto
                if(currentCommantType == CmomandType.C_GOTO){
                    codeWriter.writeGoto(arg1, functionName);
                }

                //if
                if(currentCommantType == CmomandType.C_IF ){
                    codeWriter.writeIf(arg1,functionName);
                }

                if(currentCommantType == CmomandType.C_FUNCTION){
                    codeWriter.wirteFunction(arg1, Integer.valueOf(arg2));
                    functionName = arg1;
                }

                if(currentCommantType == CmomandType.C_CALL){
                    codeWriter.wirteCall(arg1, Integer.valueOf(arg2));
                }
            }

            lineNum++;
            System.out.println("--> "+lineNum);
        }

        return codeWriter;
    }

    public static void ParserSingle(String file){
        //重置堆栈指针
        StackSPUtils.reset();

        Parser parser = new Parser(file);
        CodeWriter codeWriter = new CodeWriter(file);

        String functionName="";
        int lineNum=0;
        while(true){
            boolean flag = parser.advance();
            if(!flag ){
                break;
            }

            CmomandType currentCommantType = parser.commandType();
            if(currentCommantType == CmomandType.C_RETURNN){
                codeWriter.wirteReturn();
                functionName="";

            }else{
                //vm 翻译为 汇编
                String arg1 = parser.arg1().trim();
                String arg2 = parser.arg2();

                //push 和 pop
                if(currentCommantType == CmomandType.C_PUSH || currentCommantType == CmomandType.C_POP){
                    codeWriter.writePushPop(parser.commandType(), arg1, arg2);
                }
                //算术类型指令
                if(currentCommantType == CmomandType.C_ARITHMETIC){
                    codeWriter.writerArithmetic(arg1);
                }

                //lable
                if(currentCommantType == CmomandType.C_LABLE){
                    codeWriter.writeLable(arg1, functionName);
                }

                //goto
                if(currentCommantType == CmomandType.C_GOTO){
                    codeWriter.writeGoto(arg1, functionName);
                }

                //if
                if(currentCommantType == CmomandType.C_IF ){
                    codeWriter.writeIf(arg1,functionName);
                }

                if(currentCommantType == CmomandType.C_FUNCTION){
                    codeWriter.wirteFunction(arg1, Integer.valueOf(arg2));
                    functionName = arg1;
                }

                if(currentCommantType == CmomandType.C_CALL){
                    codeWriter.wirteCall(arg1, Integer.valueOf(arg2));
                }
            }

            lineNum++;
            System.out.println("--> "+lineNum);
        }

        codeWriter.close();
    }

}
