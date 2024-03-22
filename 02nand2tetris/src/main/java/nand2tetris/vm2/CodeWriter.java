package nand2tetris.vm2;

import nand2tetris.CommonUtils;
import nand2tetris.vm1.CmomandType;
import nand2tetris.vm1.PubConst;
import nand2tetris.vm1.StackSPUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author jiaolong
 * @date 2024/01/22 10:24
 * @description: ��vm�����ɻ������
 */
public class CodeWriter {
    private static String fileAddress;
    private List<String> vmInfo = new ArrayList<>();

    public CodeWriter(String fileAddress) {
        this.fileAddress = fileAddress;
    }

    /**
     * ������������vm����ɻ��������
     * ��ȷ����-1,���󷵻�0
     * ָ��� 1
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

        //ָ�����
        if (!"not neg".contains(command)) {
            //ָ���1
            StackSPUtils.decrementOne();
        }

        addLog(assemblerInfo, command);
    }

    //��push pop vm�����δ���
    public void writePushPop(CmomandType cmomandType, String segment, String index) {
        /*
         *
         * ָ�����
         * 1.���������Aָ������ǽ�����д��Ĵ���
         * 2.������ڴ�д����Ϣ��Cָ�
         *  ������д��Ĵ�����Ȼ��ʹ��Cָ�� �� �Ĵ������� д���ڴ档
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
        String debugInfo = "//" + command + " " + segment + " " + index;

        System.out.println("    " + assemblerInfo);
        List<String> assemblerList = Arrays.asList(assemblerInfo.split(" "));

        //��һ���������ע��
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
    //�������̿���
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
        sb.append("@LCL D=M @14 M=D ")                   //LCL��ֵ���� 14��ַ
            .append("@5 D=A @14 D=M-D @15 M=D ")         //REF�����ص�ַ ����15��ַ
            .append("@SP A=M-1 D=M @ARG A=M M=D ")       //���� �����ߵ� ����ֵ��ַ,����ARG��ַ�У�����ǰջ����ֵ �������У��� *ARG = pop()
            .append("@ARG D=M @SP M=D+1 ")               //�ָ������ߵ� SP        SP = ARG+1
            .append("@14 A=M-1 D=M @THAT M=D ")          //�ָ������ߵ� THAT
            .append("@2 D=A @14 A=M-D D=M @THIS M=D ")   //�ָ������ߵ� THIS
            .append("@3 D=A @14 A=M-D D=M @ARG M=D ")    //�ָ������ߵ� ARG
            .append("@4 D=A @14 A=M-D D=M @LCL M=D ")    //�ָ������ߵ� LCL
            .append("@15 A=M 0;JMP")                     //��ת�����ص�ַ goto REF
        ;
        addLog(sb.toString(), "return");
    }

    public void wirteFunction(String functionName, int numLocals){
        /*
         *function SimpleFunction.test 2
         *
         * @LCL         //LCLԪ�س�ʼ��Ϊ0
         * A=M
         * M=0
         *
         * A=A+1
         * M=0
         *
         * D=A+1          //SP=LCL+Ԫ�ظ���
         * @SP
         * M=D
         * @date 2024/3/17 16:09
         */

        StringBuilder sb = new StringBuilder();
        sb.append("("+functionName+") ");
        for (int i = 0; i < numLocals; i++) {
           sb.append("@SP A=M M=0 @SP M=M+1 ") ;
        }

//
//        //��ջ��ѹ�����ݣ�����ʼ��Ϊ0
//        for (int i = 0; i < numLocals; i++) {
//            if(i==0){
//                sb.append(" @LCL ");
//                sb.append("A=M ");
//            }
//            sb.append("M=0");
//            if (numLocals > 1 && i != numLocals - 1) {
//                sb.append(" A=A+1 ");
//            }
//        }
//
//        //SP=LCL+Ԫ�ظ���
//        if(numLocals > 0){
//            sb.append(" D=A+1 @SP M=D");
//        }


        String log = "function "+ functionName +" "+ numLocals;
        addLog(sb.toString().trim(), log);
    }

    /**
     * ��1������ѹ���ջ��Ȼ����� Main.fibonacci����
     * call Main.fibonacci 1
     *
     **/
    public void wirteCall(String functionName, int numLocals ){

        String returnAddress = "End$"+functionName+"$"+ReturnAddressValueUtil.getOnlyValue();

        StringBuilder sb = new StringBuilder();
        sb.append("@"+returnAddress+" D=A @SP A=M M=D @SP M=M+1 ") //���ص�ַ����ջ
            .append("@LCL D=M @SP A=M M=D @SP M=M+1 ")//������ú����� LCL��ָ��
            .append("@ARG D=M @SP A=M M=D @SP M=M+1 ")//������ú����� ARG��ָ��
            .append("@THIS D=M @SP A=M M=D @SP M=M+1 ")//������ú����� THIS��ָ��
            .append("@THAT D=M @SP A=M M=D @SP M=M+1 ")//������ú����� THAT��ָ��

            .append("@"+numLocals+" D=A @5 D=D+A @SP D=M-D @ARG M=D " )//����ARG(n=��������) ARG=SP-n-5

            .append("@SP D=M @LCL M=D ")//����LCL  LCL=SP
            .append("@"+functionName+" 0;JMP ")//��ת����
            .append("("+returnAddress+")") //Ϊ���ص�ַʲôһ����ǩ
        ;

        String log = "call "+ functionName +" "+ numLocals;
        addLog(sb.toString(), log);

    }

    private void addLog(String info, String log) {
        List<String> assemblerList = Arrays.asList(info.split(" "));

        System.out.println("    " + info);

        //��һ���������ע�� function SimpleFunction.test 2
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
     * vm ����Ϊ ���Ĺ���- �������߼���ջ����
     * ˽�� ���ɼ̳� ��̬�ڲ���
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

    /* eq lt gt����

            @SP
            AM=M-1
            D=M     ��ȡ����ֵ����������
            A=A-1
            D=M-D

                    ���������Ϊ��0
            M=0
            @eq_0         -->ָ����ǩ����
            D;JNE         -->[eq->JNE lt->JGE gt->JLE]

                    �������Ϊ: -1
            @SP
            A=M-1
            M=-1

            (eq_0)        -->�����ǩ
    */

            //��ǩ�� ������(eq_0)
            int lableIndex = StackSPUtils.LableIndex.getAssemblerLableIndex();
            String lable = command + "_" + lableIndex;

            StringBuilder sb = new StringBuilder();
            //��ȡ����ֵ ��ָ���1
            sb.append("@SP ");
            sb.append("AM=M-1 ");
            sb.append("D=M ");  //�����һ��ֵ
            sb.append("A=A-1 ");
            sb.append("D=M-D "); //��ֵ

            //����ȵĽ������0
            sb.append("M=0 ");
            sb.append("@"+lable+" ");  //@eq_0

            //sb.append("D;JNE ");
            sb.append("D;%1$s ");

            //��ȵĽ������Ϊ-1
            sb.append("@SP ");
            sb.append("A=M-1 ");
            sb.append("M=-1 ");

            //���ñ�ǩ
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
                ʵ�ֵ�ָ�add sub and or
            @SP
            AM=M-1
            D=M                 y=D
            A=A-1
            D=M+D               x=M

                 ���÷��ؽ��
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

            //�滻��Ӧ�����ַ�
            String returnAsserbler = String.format(sb.toString(), computeTypes.get(command));
            return returnAsserbler;
        }

        /**
         * not neg ָ���෭��
         *
         * @param: command
         * @return: java.lang.String
         **/
        private static String getNegNotAssemInfo(String command) {
           /*
                ʵ�ֵ�ָ�neg not
            @SP
            AM=M-1
            D=M                 y=D

            M=!D                �����ݽ��� not neg����
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
     * vm ����Ϊ ���Ĺ���- �ڴ��������
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
                ʵ�� push constant ָ��-��ջ
                    @17 D=A @SP A=M M=D @SP M=M+1
            */

            StringBuilder sb = new StringBuilder();
            sb.append("@" + index + " D=A @SP A=M M=D @SP M=M+1");

            return sb.toString();
        }

        private static String getPushTempPointerStaticAssemblerInfo(String segment, String index){

            /*
                   ʵ�� push ָ��-��ջ
                                push temp 2
                                push pointer 0
                                push static 0

                    @5+index  ������ʵ��ַ
                    D=M

                    @SP
                    A=M		��ȡջ��Ԫ�ص�ַ
                    M=D     �����ݷ���ջ

                    @SP		ջָ���1
                    M=M+1
            */

            //��ȡ���ǵ�ַ
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
                   ʵ�� push ָ��-��ջ
                                push local 0
                                push argument 2
                                push this 2
                                push that 2
                                push temp 2

                    @segmentAddress
                    D=M     ��ȡ�ε�ַ

                    @index  ������ʵ��ַ
                    A=D+A
                    D=M

                    @SP
                    A=M		��ȡջ��Ԫ�ص�ַ
                    M=D     �����ݷ���ջ

                    @SP		ջָ���1
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

            //��ʱ�ֶε�ַ
            int temp0Address = PubConst.SEGMENT_TEMP;
            int temp1Address = temp0Address+1;

            String segmentAddress =Optional.ofNullable(segments.get(segment)).orElse("");
            if(segmentAddress.length() == 0){
                new RuntimeException("segment Name is null. segment:"+segment);
            }

            /*
                   ʵ�� pop ָ��-��ջ
                              pop local 0
                              pop argument 2
                              pop this 2
                              pop that 2
        ��ȡ����
                    @SP
                    A=M-1
                    D=M     ��ȡջ������
              1
                    @temp0Address
                    M=D     ��ջ�����ݴ��temp5��


                    @segmentAddress
                    D=M     ��ȡ�ε�ַ
                    @index
                    D=D+A   ������ʵ��ַ
              2
                    @temp1Address
                    M=D     ���ε���ʵ��ַtemp6��

        ��������
                    @temp0Address
                    D=M

                    @temp1Address
                    A=M
                    M=D    ��ջ���� ���� ָ��λ��

                    @SP		ջָ���1
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
                   ʵ�� pop ָ��-��ջ
                              pop temp 0
                              pop pointer 0
                              pop static 0
                    @SP
                    A=M-1
                    D=M     ��ȡջ������

                    @temp+index
                    M=D     ��ջ�����ݴ��temp5��
            */

            //��ʱ�ֶε�ַ
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

    public List<String> getVmInfo() {
        return vmInfo;
    }
}
