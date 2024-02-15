package nand2tetris.vm1;

import nand2tetris.CommonUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author jiaolong
 * @date 2024/01/22 10:24
 * @description: 将vm命令翻译成汇编语言
 */
public class CodeWriter {
    private static String fileAddress;
    private List<String> vmInfo = new ArrayList<>();

    public CodeWriter(String fileAddress) {
        this.fileAddress = fileAddress;
    }

    /**
     * 将算术操作的vm翻译成汇编进行输出
     * 正确返回-1,错误返回0
     * 指针减 1
     *
     * @param: null
     * @return:
     **/
    public void writerArithmetic(String command) {

        String assemblerInfo = "";

        if ("eq lt gt".contains(command)) {
            assemblerInfo = ArithmeticAssemblerCode.getArithmeticAssemInfo(command);
        } else if ("and or add sub".contains(command)) {
            assemblerInfo = ArithmeticAssemblerCode.getAndOrAddSubAssemInfo(command);
        } else {
            assemblerInfo = ArithmeticAssemblerCode.getNegNotAssemInfo(command);
        }

        //指针操作
        if (!"not neg".contains(command)) {
            //指针减1
            StackSPUtils.decrementOne();
        }

        List<String> assemblerList = Arrays.asList(assemblerInfo.split(" "));
        vmInfo.add("//" + command);
        vmInfo.addAll(assemblerList);
    }

    //将push pop vm命令翻译未汇编
    public void writePushPop(CmomandType cmomandType, String segment, String index) {
        /*
         *
         * 指令操作
         * 1.定义变量（A指令），就是将数据写入寄存器
         * 2.如何向内存写入信息（C指令）
         *  将数据写入寄存器，然后使用C指令 将 寄存器数据 写入内存。
         */

        String assemblerInfo = "";
        if (cmomandType == CmomandType.C_PUSH) {
            if (segment.equals("constant")) {
                assemblerInfo = MemoryAssemblerCode.getPushConstantAssemblerInfo(index);
            }else if ("temp pointer static".contains(segment)) {
                assemblerInfo = MemoryAssemblerCode.getPushTempPointerStaticAssemblerInfo(segment,index);
            }else{
                assemblerInfo = MemoryAssemblerCode.getPushLocaArgThisThatAssemblerInfo(segment,index);
            }
            StackSPUtils.addOne();
        }

        if (cmomandType == CmomandType.C_POP) {
            if (("temp pointer static".contains(segment))){
                assemblerInfo = MemoryAssemblerCode.getPopTempPointerStaticAssemblerInfo(segment,index);
            }else{
                assemblerInfo = MemoryAssemblerCode.getPopLocaArgThisThatAssemblerInfo(segment,index);
            }
            StackSPUtils.decrementOne();
        }


        String command = MemoryAssemblerCode.typeCommand.get(cmomandType);
        vmInfo.add("//" + command + " " + segment + " " + index);

        System.out.println("    " + assemblerInfo);
        List<String> assemblerList = Arrays.asList(assemblerInfo.split(" "));
        vmInfo.addAll(assemblerList);

    }

    public void close() {
        Path filePath = Paths.get(fileAddress);
        CommonUtils.outputFile(filePath, vmInfo, "asm");
    }

    /**
     * vm 翻译为 汇编的工具- 算术和逻辑堆栈命令
     * 私有 不可继承 静态内部类
     *
     * @author jiaolong
     * @date 2024-1-24 16:57
     */
    private final static class ArithmeticAssemblerCode {
        //eq lt gt
        private static Map<String, String> compare = new HashMap<>();
        //and or add sub
        private static Map<String, String> computeTypes = new HashMap<>();
        //neg not
        private static Map<String, String> negNotTypes = new HashMap<>();

        static {
            compare.put("eq", "JNE");
            compare.put("lt", "JGE");
            compare.put("gt", "JLE");

            computeTypes.put("add", "+");
            computeTypes.put("sub", "-");
            computeTypes.put("and", "&");
            computeTypes.put("or", "|");

            negNotTypes.put("neg", "-");
            negNotTypes.put("not", "!");
        }

        /**
         * eq lt gt
         *
         * @param: command
         * @return: java.lang.String
         **/
        private static String getArithmeticAssemInfo(String command) {
            int lableIndex = StackSPUtils.LableIndex.getAssemblerLableIndex();

          /* eq 案例
                   获取两个值 且指针减1
            @SP
            AM=M-1
            D=M
            A=A-1
            D=M-D

                  不相等的结果设置0
            M=0
            @eq_0
            D;JNE

                  相等的结果设置为-1
            @SP
            A=M-1
            M=-1

            (eq_0)
    */
            StringBuilder sb = new StringBuilder();
            //获取两个值 且指针减1
            sb.append("@SP ");
            sb.append("AM=M-1 ");
            sb.append("D=M ");
            sb.append("A=A-1 ");
            sb.append("D=M-D ");

            //不相等的结果设置0
            sb.append("M=0 ");
            //sb.append("@eq_0 ");
            sb.append("@%1$s ");
            //sb.append("D;JNE ");
            sb.append("D;%2$s ");

            //相等的结果设置为-1
            sb.append("@SP ");
            sb.append("A=M-1 ");
            sb.append("M=-1 ");

            //设置标签
            //sb.append("(eq_0) ");
            sb.append("(%3$s) ");

            //标签与 汇编符号
            String lable = command + "_" + lableIndex;
            String arithmeticChar = compare.get(command);

            String returnAsserbler = String.format(sb.toString(), lable, arithmeticChar, lable);
            System.out.println("    " + returnAsserbler);

            return returnAsserbler;
        }

        /**
         * and or add sub
         *
         * @param: command
         * @return: java.lang.String
         **/
        private static String getAndOrAddSubAssemInfo(String command) {
           /*
                实现的指令：add sub and or
            @SP
            AM=M-1
            D=M                 y=D
            A=A-1
            D=M+D               x=M

                 设置返回结果
            @SP
            A=M-1
            M=D
           */

            StringBuilder sb = new StringBuilder();
            sb.append("@SP ");
            sb.append("AM=M-1 ");
            sb.append("D=M ");
            sb.append("A=A-1 ");

            //sb.append("D=M+D ");
            sb.append("D=M" + "%1$s" + "D ");

            sb.append("@SP ");
            sb.append("A=M-1 ");
            sb.append("M=D ");

            //替换相应计算字符
            String returnAsserbler = String.format(sb.toString(), computeTypes.get(command));
            System.out.println("    " + returnAsserbler);

            return returnAsserbler;

        }

        /**
         * not neg 指令汇编翻译
         *
         * @param: command
         * @return: java.lang.String
         **/
        private static String getNegNotAssemInfo(String command) {
           /*
                实现的指令：neg not
            @SP
            AM=M-1
            D=M                 y=D

            M=!D                对数据进行 not neg操作
           */

            StringBuilder sb = new StringBuilder();
            sb.append("@SP ");
            sb.append("A=M-1 ");
            sb.append("D=M ");
            //sb.append("M=!D ");
            sb.append("M=" + "%1$s" + "D ");

            String returnAsserbler = String.format(sb.toString(), negNotTypes.get(command));
            System.out.println("    " + returnAsserbler);

            return returnAsserbler;
        }
    }


    /**
     * vm 翻译为 汇编的工具- 内存访问命令
     *
     * @author jiaolong
     * @date 2024-1-25 15:41
     */
    private final static class MemoryAssemblerCode {
        private static Map<CmomandType, String> typeCommand = new HashMap<>();
        private static Map<String, String> segments = new HashMap<>();

        static {
            typeCommand.put(CmomandType.C_PUSH, "push");
            typeCommand.put(CmomandType.C_POP, "pop");

            segments.put("local","LCL");
            segments.put("argument","ARG");
            segments.put("this","THIS");
            segments.put("that","THAT");
        }


        private static String getPushConstantAssemblerInfo(String index) {
            /*
                实现 push constant 指令-入栈
                        @17
                        D=A         将17放入到D存储器

                        @16         指针
                        M=D		  将数据写入指定地址

                        D=A		  获取指针
                        @SP       指针加1
                        M=D+1
            */

            //获取当前指针
            int sp = StackSPUtils.get();

            StringBuilder sb = new StringBuilder();
            sb.append("@" + index + " ")
                    .append("D=A ")
                    .append("@" + sp + " ")
                    .append("M=D ")
                    .append("D=A ")
                    .append("@SP ")
                    .append("M=D+1 ");

            return sb.toString();
        }


        private static String getPushTempPointerStaticAssemblerInfo(String segment, String index){

            /*
                   实现 push 指令-入栈
                                push temp 2

                    @5+index  计算真实地址
                    D=M

                    @SP
                    A=M		获取栈顶元素地址
                    M=D     将数据放入栈

                    @SP		栈指针加1
                    M=M+1
            */

            //获取真是地址
            String readAddress = "";
            if(segment.equals("temp")){
                 readAddress = PubConst.SEGMENT_TEMP+Integer.valueOf(index)+"";
            }
            if(segment.equals("pointer")){
                 readAddress = PubConst.SEGMENT_POINTER+Integer.valueOf(index)+"";
            }
            if(segment.equals("static")){
                readAddress = getFileName()+"."+index;
            }

            StringBuilder sb = new StringBuilder();
                    sb.append(getNewInfo("@"+readAddress))
                    .append(getNewInfo("D=M"))

                    .append(getNewInfo("@SP A=M M=D"))

                    .append(getNewInfo("@SP M=M+1"))
            ;

            return sb.toString();

        }

        private static String getPushLocaArgThisThatAssemblerInfo(String segment, String index){

            String segmentAddress =Optional.ofNullable(segments.get(segment)).orElse("");
            if(segmentAddress.length() == 0){
                new RuntimeException("segment Name is null. segment:"+segment);
            }

            /*
                   实现 push 指令-入栈
                                push local 0
                                push argument 2
                                push this 2
                                push that 2
                                push temp 2

                    @segmentAddress
                    D=M     获取段地址

                    @index  计算真实地址
                    A=D+A
                    D=M

                    @SP
                    A=M		获取栈顶元素地址
                    M=D     将数据放入栈

                    @SP		栈指针加1
                    M=M+1
            */
            StringBuilder sb = new StringBuilder();
            sb.append(getNewInfo("@"+segmentAddress))
                    .append(getNewInfo("D=M"))

                    .append(getNewInfo("@"+index))
                    .append(getNewInfo("A=D+A"))
                    .append(getNewInfo("D=M"))

                    .append(getNewInfo("@SP"))
                    .append(getNewInfo("A=M"))
                    .append(getNewInfo("M=D"))

                    .append(getNewInfo("@SP"))
                    .append(getNewInfo("M=M+1")
                    );

            return sb.toString();

        }


        private static String getPopLocaArgThisThatAssemblerInfo(String segment, String index){

            //临时字段地址
            int temp0Address = PubConst.SEGMENT_TEMP;
            int temp1Address = temp0Address+1;

            String segmentAddress =Optional.ofNullable(segments.get(segment)).orElse("");
            if(segmentAddress.length() == 0){
                new RuntimeException("segment Name is null. segment:"+segment);
            }

            /*
                   实现 pop 指令-出栈
                              pop local 0
                              pop argument 2
                              pop this 2
                              pop that 2
        获取数据
                    @SP
                    A=M-1
                    D=M     获取栈顶数据
              1
                    @temp0Address
                    M=D     将栈顶数据存放temp5中


                    @segmentAddress
                    D=M     获取段地址
                    @index
                    D=D+A   计算真实地址
              2
                    @temp1Address
                    M=D     将段的真实地址temp6中

        操作数据
                    @temp0Address
                    D=M

                    @temp1Address
                    A=M
                    M=D    将栈数据 放入 指定位置

                    @SP		栈指针减1
                    M=M-1
            */
            StringBuilder sb = new StringBuilder();
            sb.append(getNewInfo("@SP A=M-1 D=M"))
                    .append(getNewInfo("@"+temp0Address+" M=D"))

                    .append(getNewInfo("@"+segmentAddress+" D=M"))
                    .append(getNewInfo("@"+index+" D=D+A"))
                    .append(getNewInfo("@"+temp1Address+" M=D"))

                    .append(getNewInfo("@"+temp0Address+" D=M"))
                    .append(getNewInfo("@"+temp1Address+" A=M M=D"))
                    .append(getNewInfo("@SP M=M-1"))
                    ;

            return sb.toString();
        }

        private static String getPopTempPointerStaticAssemblerInfo(String segment, String index){

             /*
                   实现 pop 指令-出栈
                              pop temp 0
                    @SP
                    A=M-1
                    D=M     获取栈顶数据

                    @temp+index
                    M=D     将栈顶数据存放temp5中
         */

            //临时字段地址
            String readAddress = "";
            if(segment.equals("temp")){
                readAddress = PubConst.SEGMENT_TEMP+Integer.valueOf(index)+"";
            }
            if(segment.equals("pointer")){
                readAddress = PubConst.SEGMENT_POINTER+Integer.valueOf(index)+"";
            }
            if(segment.equals("static")){
                readAddress = getFileName()+"."+index;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(getNewInfo("@SP A=M-1 D=M"))
              .append(getNewInfo("@"+readAddress+" M=D"))
              .append(getNewInfo("@SP M=M-1"))
            ;

            return sb.toString();
        }


        private static String getNewInfo(String info){
            return info+" ";
        }
        private static String getFileName() {
            String fileName = new File(fileAddress).getName();
            return fileName.split("\\.")[0];
        }

    }

}
