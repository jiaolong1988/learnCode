package nand2tetris.vm1;

import nand2tetris.CommonUtils;

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

        List<String> assemblerList = Arrays.asList(assemblerInfo.split(" "));
        vmInfo.add("//" + command);
        vmInfo.addAll(assemblerList);
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
            int lableIndex = StackSPUtils.LableIndex.getAssemblerLableIndex();

          /* eq ����
                   ��ȡ����ֵ ��ָ���1
            @SP
            AM=M-1
            D=M
            A=A-1
            D=M-D

                  ����ȵĽ������0
            M=0
            @eq_0
            D;JNE

                  ��ȵĽ������Ϊ-1
            @SP
            A=M-1
            M=-1

            (eq_0)
    */
            StringBuilder sb = new StringBuilder();
            //��ȡ����ֵ ��ָ���1
            sb.append("@SP ");
            sb.append("AM=M-1 ");
            sb.append("D=M ");
            sb.append("A=A-1 ");
            sb.append("D=M-D ");

            //����ȵĽ������0
            sb.append("M=0 ");
            //sb.append("@eq_0 ");
            sb.append("@%1$s ");
            //sb.append("D;JNE ");
            sb.append("D;%2$s ");

            //��ȵĽ������Ϊ-1
            sb.append("@SP ");
            sb.append("A=M-1 ");
            sb.append("M=-1 ");

            //���ñ�ǩ
            //sb.append("(eq_0) ");
            sb.append("(%3$s) ");

            //��ǩ�� ������
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
            System.out.println("    " + returnAsserbler);

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
            System.out.println("    " + returnAsserbler);

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
                        @17
                        D=A         ��17���뵽D�洢��

                        @16         ָ��
                        M=D		  ������д��ָ����ַ

                        D=A		  ��ȡָ��
                        @SP       ָ���1
                        M=D+1
            */

            //��ȡ��ǰָ��
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
                   ʵ�� push ָ��-��ջ
                                push temp 2

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

}
