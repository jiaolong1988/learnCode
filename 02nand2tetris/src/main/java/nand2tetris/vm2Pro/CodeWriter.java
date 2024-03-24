package nand2tetris.vm2Pro;

import nand2tetris.CommonUtils;
import nand2tetris.vm1.CmomandType;

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


        addLog(assemblerInfo, command);
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

        }

        if (cmomandType == CmomandType.C_POP) {
            if (("temp pointer static".contains(segment))){
                assemblerInfo = MemoryAssemblerCode.getPopTempPointerStaticAssemblerInfo(segment,index);
            }else{
                assemblerInfo = MemoryAssemblerCode.getPopLocaArgThisThatAssemblerInfo(segment,index);
            }
        }


        String command = MemoryAssemblerCode.typeCommand.get(cmomandType);
        String debugInfo = "//" + command + " " + segment + " " + index;

        System.out.println("    " + assemblerInfo);
        List<String> assemblerList = Arrays.asList(assemblerInfo.split(" "));

        //第一含代码添加注释
        String firstNewLine = assemblerList.get(0)+"   "+debugInfo;
        vmInfo.add(firstNewLine);

        List<String> oldInfo = assemblerList.subList(1,assemblerList.size());
        vmInfo.addAll(oldInfo);
    }


    //lable
    public void writeLable( String arg1, String functionName) {

        //label LOOP_START --> (LOOP_START)
        String functionLable = functionName+"$"+arg1;
        String lable = "("+functionLable+")" +"  //label "+arg1;
        System.out.println("    " + lable);

        vmInfo.add(lable);
    }

    public void writeGoto( String arg1, String functionName) {
            /*
             *  goto LOOP_START
             *
             *  @LOOP_START
             *  0;JMP
             *  @SP M=M-1
             * @date 2024/3/10 15:13
             */

            String functionLable = functionName+"$"+arg1;
            String info = "@"+functionLable+" 0;JMP";

            String log = "goto "+ functionName;
            addLog(info, log);
    }
    //程序流程控制
    public void writeIf(String arg1,String functionName) {
            /*
             *  if-goto LOOP_START
             *
             *  @SP
             *  AM=M-1
             *  D=M
             *  @LOOP_START
             *  D;JNE
             *  @SP M=M-1
             * @date 2024/3/10 15:13
             */

            String functionLable = functionName+"$"+arg1;
            String info = "@SP AM=M-1 D=M @"+functionLable+" D;JNE";

            String log = "if-goto "+ arg1;
            addLog(info, log);

    }


    public void wirteReturn(){
        StringBuilder sb = new StringBuilder();
        sb.append("@LCL D=M @R15 M=D ")                   //LCL的值存入 15地址
            .append("@5 D=A @R15 A=M-D D=M @R14 M=D ")         //REF：返回地址 存入14地址
            .append("@SP AM=M-1 D=M @ARG A=M M=D ")       //重置 调用者的 返回值地址,（在ARG地址中，将当前栈顶的值 放入其中）。 *ARG = pop()
            .append("@ARG D=M+1 @SP M=D ")               //恢复调用者的 SP        SP = ARG+1
            .append("@R15 A=M-1 D=M @THAT M=D ")          //恢复调用者的 THAT
            .append("@2 D=A @R15 A=M-D D=M @THIS M=D ")   //恢复调用者的 THIS
            .append("@3 D=A @R15 A=M-D D=M @ARG M=D ")    //恢复调用者的 ARG
            .append("@4 D=A @R15 A=M-D D=M @LCL M=D ")    //恢复调用者的 LCL
            .append("@R14 A=M 0;JMP")                     //跳转到返回地址 goto REF
        ;
        addLog(sb.toString(), "return");
    }

    public void wirteFunction(String functionName, int numLocals){
        /*
         *function SimpleFunction.test 2
         *
         * @date 2024/3/17 16:09
         */

        StringBuilder sb = new StringBuilder();
        sb.append("("+functionName+") ");
        for (int i = 0; i < numLocals; i++) {
           sb.append("@SP A=M M=0 @SP M=M+1 ") ;
        }

        String log = "function "+ functionName +" "+ numLocals;
        addLog(sb.toString().trim(), log);
    }

    /**
     * 将1个参数压入堆栈，然后调用 Main.fibonacci函数
     * call Main.fibonacci 1
     *
     **/
    public void wirteCall(String functionName, int numLocals ){

        String returnAddress = "End$"+functionName+"$"+ ReturnAddressValueUtil.getOnlyValue();

        StringBuilder sb = new StringBuilder();
        sb.append("@"+returnAddress+" D=A @SP A=M M=D @SP M=M+1 ") //返回地址加入栈
            .append("@LCL D=M @SP A=M M=D @SP M=M+1 ")//报错调用函数的 LCL段指针
            .append("@ARG D=M @SP A=M M=D @SP M=M+1 ")//报错调用函数的 ARG段指针
            .append("@THIS D=M @SP A=M M=D @SP M=M+1 ")//报错调用函数的 THIS段指针
            .append("@THAT D=M @SP A=M M=D @SP M=M+1 ")//报错调用函数的 THAT段指针

            .append("@"+numLocals+" D=A @5 D=D+A @SP D=M-D @ARG M=D " )//重置ARG(n=参数数量) ARG=SP-n-5

            .append("@SP D=M @LCL M=D ")//重置LCL  LCL=SP
            .append("@"+functionName+" 0;JMP ")//跳转控制
            .append("("+returnAddress+")") //为返回地址什么一个标签
        ;

        String log = "call "+ functionName +" "+ numLocals;
        addLog(sb.toString(), log);

    }

    private void addLog(String info, String log) {
        List<String> assemblerList = Arrays.asList(info.split(" "));

        System.out.println("    " + info);

        //第一含代码添加注释 function SimpleFunction.test 2
        String firstNewLine = assemblerList.get(0)+"   // "+ log ;
        vmInfo.add(firstNewLine);

        List<String> oldInfo = assemblerList.subList(1,assemblerList.size());
        vmInfo.addAll(oldInfo);
    }


    public void close() {
     /*
      *
      * @date 2024/2/15 17:44
      */
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

    /* eq lt gt案例

            @SP
            AM=M-1
            D=M     获取两个值做减法运算
            A=A-1
            D=M-D

                    不相等设置为：0
            M=0
            @eq_0         -->指定标签变量
            D;JNE         -->[eq->JNE lt->JGE gt->JLE]

                    相等设置为: -1
            @SP
            A=M-1
            M=-1

            (eq_0)        -->定义标签
    */

            //标签与 汇编符号(eq_0)
            String lable = command + "_" + ReturnAddressValueUtil.getOnlyValue();

            StringBuilder sb = new StringBuilder();
            //获取两个值 且指针减1
            sb.append("@SP ");
            sb.append("AM=M-1 ");
            sb.append("D=M ");  //最近的一个值
            sb.append("A=A-1 ");
            sb.append("D=M-D "); //差值

            //不相等的结果设置0
            sb.append("M=0 ");
            sb.append("@"+lable+" ");  //@eq_0

            //sb.append("D;JNE ");
            sb.append("D;%1$s ");

            //相等的结果设置为-1
            sb.append("@SP A=M-1 M=-1 ");

            //设置标签
            //(eq_0)
            sb.append("("+lable+") ");


            String arithmeticChar = compare.get(command);
            String returnAsserbler = String.format(sb.toString(),arithmeticChar);

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

        //temp
        public static final int SEGMENT_TEMP = 5;
        //pointer
        public static final int SEGMENT_POINTER = 3;

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
                    @17 D=A @SP A=M M=D @SP M=M+1
            */

            StringBuilder sb = new StringBuilder();
            sb.append("@" + index + " D=A @SP A=M M=D @SP M=M+1");

            return sb.toString();
        }

        private static String getPushTempPointerStaticAssemblerInfo(String segment, String index){

            /*
                   实现 push 指令-入栈
                                push temp 2
                                push pointer 0
                                push static 0
            */

            StringBuilder sb = new StringBuilder();

            if(segment.equals("temp")){
                sb.append("@"+index+" D=A ").append("@"+SEGMENT_TEMP).append(" A=A+D D=M ");

            }
            if(segment.equals("pointer")){
                sb.append("@"+index+" D=A ").append("@"+SEGMENT_POINTER).append(" A=A+D D=M ");
            }
            if(segment.equals("static")){
                String readAddress = getFileName()+"."+index;
                sb.append("@"+readAddress+" D=M ");
            }

            sb.append("@SP A=M M=D @SP M=M+1");

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
            */
            StringBuilder sb = new StringBuilder();
            sb.append("@"+index)
                    .append(" D=A ")
                    .append("@"+segmentAddress)
                    .append(" A=M+D ")
                    .append("D=M ");

            sb.append("@SP A=M M=D @SP M=M+1");

            return sb.toString();
        }

        private static String getPopLocaArgThisThatAssemblerInfo(String segment, String index){

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
            */

            StringBuilder sb = new StringBuilder();
            sb.append("@"+index+" D=A ")
                    .append("@"+segmentAddress+" D=M+D ")
                    .append("@R15 M=D ")

                    .append("@SP AM=M-1 D=M @R15 A=MM =D")
                    ;

            return sb.toString();
        }

        private static String getPopTempPointerStaticAssemblerInfo(String segment, String index){

             /*
                   实现 pop 指令-出栈
                              pop temp 0
                              pop pointer 0
                              pop static 0
            */

            StringBuilder sb = new StringBuilder();
            if(segment.equals("temp")){
                sb.append("@"+index+" D=A " ).append("@"+SEGMENT_TEMP+" D=A+D @R15 M=D ");
            }
            if(segment.equals("pointer")){
                sb.append("@"+index+" D=A " ).append("@"+SEGMENT_POINTER+" D=A+D @R15 M=D ");
            }
            if(segment.equals("static")){
              String symbol = getFileName()+"."+index;
              sb.append("@"+symbol+" D=A @R15 M=D ");
            }

            sb.append("@SP AM=M-1 D=M @R15 A=M M=D");

            return sb.toString();
        }

        private static String getFileName() {
            String fileName = new File(fileAddress).getName();
            return fileName.split("\\.")[0];
        }

    }

    public List<String> getVmInfo() {
        return vmInfo;
    }
}
