package nand2tetris.vm2Pro;


import nand2tetris.CommonUtils;
import nand2tetris.vm1.CmomandType;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: jiaolong
 * @date: 2024/03/22 11:36
 **/
public class Vm2ProServerMain {
    public static void main(String[] args) {

        //指定目录
        String fibonacciElementDir = "D:\\test\\08\\FunctionCalls\\FibonacciElement";
        String staticTestDir = "D:\\test\\08\\FunctionCalls\\StaticsTest";
        mergeCompileTest(staticTestDir);
    }

    public static void mergeCompileTest(String dir) {
        //指令结合
        List<String> infoList = new ArrayList();

        //初始化
        StringBuilder sb = init();
        //添加初始化指令
        List info = Arrays.asList(sb.toString().split(" "));
        infoList.addAll(info);

        mergeCompile(dir, infoList);

        //汇编输出
        File dirFile = new File(dir);
        String ourFileAddress = String.join(File.separator, dir, dirFile.getName() + ".vm");
        Path filePath = Paths.get(ourFileAddress);
        CommonUtils.outputFile(filePath, infoList, "asm");
    }

    /**
     *
     * 初始化操作
     *  1.堆栈初始值为：256
     *  2.第一个执行程序必须为：Sys.init
     **/
    private static StringBuilder init() {
        //init-start
        StringBuilder sb = new StringBuilder();
        sb.append("@256 D=A @SP M=D ")
//        .append("@1 D=A @LCL M=D ")
//        .append("@2 D=A @ARG M=D ")
//        .append("@3 D=A @THIS M=D ")
//        .append("@4 D=A @THAT M=D ")
        ;

        //push retAddr
        String push_D="@SP A=M M=D @SP M=M+1 ";
        sb.append("@bootstrap D=A "+push_D);

        for(String x :Arrays.asList("LCL","ARG","THIS","THAT")){
            sb.append("@"+x+" D=M "+push_D);
        }

        // ARG=SP-n-5
        sb.append("@5 D=A @SP D=M-D @ARG M=D ");
        //LCL=SP
        sb.append("@SP D=M @LCL M=D ");
        sb.append("@Sys.init 0;JMP (bootstrap)");
        return sb;
    }

    public static void mergeCompile(String dir, List<String> infoList) {

        //目录
        File dirFile = new File(dir);
        //获取目录下的 vm文件
        List<String> vmList = new ArrayList();
        for (File fp : dirFile.listFiles()) {
            if (fp.getName().endsWith(".vm")) {
                vmList.add(fp.getAbsolutePath());
            }
        }

        //获取解析信息
        for (String vmFile : vmList) {
            CodeWriter cw = vmParserMerge(vmFile);
            List<String> info = cw.getVmInfo();
            infoList.addAll(info);
        }
    }


    /**
     * 文件夹 vm文件合并解析
     *
     * @param: file
     * @return: void
     **/
    public static CodeWriter vmParserMerge(String file) {

        Parser parser = new Parser(file);
        CodeWriter codeWriter = new CodeWriter(file);

        String functionName = "";
        int lineNum = 0;
        while (true) {
            boolean flag = parser.advance();
            if (!flag) {
                break;
            }

            CmomandType currentCommantType = parser.commandType();
            if (currentCommantType == CmomandType.C_RETURNN) {
                codeWriter.wirteReturn();

            } else {
                //vm 翻译为 汇编
                String arg1 = parser.arg1().trim();
                String arg2 = parser.arg2();

                //push 和 pop
                if (currentCommantType == CmomandType.C_PUSH || currentCommantType == CmomandType.C_POP) {
                    codeWriter.writePushPop(parser.commandType(), arg1, arg2);
                }
                //算术类型指令
                if (currentCommantType == CmomandType.C_ARITHMETIC) {
                    codeWriter.writerArithmetic(arg1);
                }

                //lable
                if (currentCommantType == CmomandType.C_LABLE) {
                    codeWriter.writeLable(arg1, functionName);
                }

                //goto
                if (currentCommantType == CmomandType.C_GOTO) {
                    codeWriter.writeGoto(arg1, functionName);
                }

                //if
                if (currentCommantType == CmomandType.C_IF) {
                    codeWriter.writeIf(arg1, functionName);
                }

                if (currentCommantType == CmomandType.C_FUNCTION) {
                    codeWriter.wirteFunction(arg1, Integer.valueOf(arg2));
                    functionName = arg1;
                }

                if (currentCommantType == CmomandType.C_CALL) {
                    codeWriter.wirteCall(arg1, Integer.valueOf(arg2));
                }
            }

            lineNum++;
            System.out.println("--> " + lineNum);
        }

        return codeWriter;
    }


}
