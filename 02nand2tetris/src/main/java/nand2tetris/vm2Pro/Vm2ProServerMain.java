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

//        String file ="D:\\test\\07\\MemoryAccess\\PointerTest\\PointerTest.vm";
//        vmParserMerge(file).close();

        mergeCompileTest();
    }

    public static void mergeCompileTest() {
        //ָ��Ŀ¼
        String dir = "D:\\test\\08\\FunctionCalls\\FibonacciElement";
        //ָ����
        List<String> infoList = new ArrayList();

        //��ʼ��
        /**
         *
         * ��ʼ������
         *  1.��ջ��ʼֵΪ��256
         *  2.��һ��ִ�г������Ϊ��Sys.init
         **/
        StringBuilder sb = new StringBuilder();
        sb.append("@256 D=A @SP M=D ").append("@Sys.init 0;JMP");
        List info = Arrays.asList(sb.toString().split(" "));
        infoList.addAll(info);


        mergeCompile(dir, infoList);

        //������
        File dirFile = new File(dir);
        String ourFileAddress = String.join(File.separator, dir, dirFile.getName() + ".vm");
        Path filePath = Paths.get(ourFileAddress);
        CommonUtils.outputFile(filePath, infoList, "asm");
    }

    public static void mergeCompile(String dir, List<String> infoList) {

        //Ŀ¼
        File dirFile = new File(dir);
        //��ȡĿ¼�µ� vm�ļ�
        List<String> vmList = new ArrayList();
        for (File fp : dirFile.listFiles()) {
            if (fp.getName().endsWith(".vm")) {
                vmList.add(fp.getAbsolutePath());
            }
        }

        //��ȡ������Ϣ
        for (String vmFile : vmList) {
            CodeWriter cw = vmParserMerge(vmFile);
            List<String> info = cw.getVmInfo();
            infoList.addAll(info);
        }
    }


    /**
     * �ļ��� vm�ļ��ϲ�����
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
                //vm ����Ϊ ���
                String arg1 = parser.arg1().trim();
                String arg2 = parser.arg2();

                //push �� pop
                if (currentCommantType == CmomandType.C_PUSH || currentCommantType == CmomandType.C_POP) {
                    codeWriter.writePushPop(parser.commandType(), arg1, arg2);
                }
                //��������ָ��
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
