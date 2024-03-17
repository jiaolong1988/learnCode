package nand2tetris.vm2;

import nand2tetris.CommonUtils;
import nand2tetris.vm1.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author jiaolong
 * @date 2024/01/22 10:19
 * @description: ��vm������н���
 */
public class Parser {
    //vm����ϵ�����
    private Iterator<String> iterator = null;
    //��ǰ����
    private String currentCommand = "";

    //���캯����ʼ��
    public Parser(String file){
        Path filePath = Paths.get(file);
        System.out.println(file);

        //��ȡvm�ļ�����
        List<String> vmInfo = CommonUtils.readFile(filePath);

        //��ȡ�����������
        List<String> list = new ArrayList<>();
        for(String tempVmInfo :vmInfo){
            String command = CommonUtils.getCommand(tempVmInfo);
            if(command.length()>0){
                list.add(command);
            }
        }

        iterator = list.iterator();
    }

    //�Ƿ�������
    private boolean hasMoreCommands(){
       return iterator.hasNext();
    }

    //��ȡ��һ��ָ�����������Ϊ��ǰ����
    public boolean advance(){
        boolean flag = hasMoreCommands();
        if(flag){
            currentCommand  = iterator.next().trim();
            System.out.println("vm info��"+currentCommand);
        }
        return flag;
    }

    //���ص�ǰvm���������
    public CmomandType commandType(){
        String operationCommand  = currentCommand.split(" ")[0];
        if(!InnerUtil.arithmeticList.containsKey(operationCommand)){
            throw new RuntimeException("��ǰ�������Ͳ����ڣ����"+currentCommand);
        }

        return InnerUtil.arithmeticList.get(operationCommand);

    }

    public String arg1(){
        String result = "";
        if(CmomandType.C_ARITHMETIC == commandType()){
            //���������ֱ�ӷ��������
            result = currentCommand;
        }else{
            //����ĵ�һ������
            result = currentCommand.split(" ")[1];
        }
        return result;
    }

    public String arg2(){
        String result = "";
        if(CmomandType.C_PUSH == commandType() || CmomandType.C_POP == commandType()||
            CmomandType.C_FUNCTION == commandType()){
            //����ĵڶ�������
            result = currentCommand.split(" ")[2];
        }
        return result;
    }

    static class InnerUtil{
        private static Map<String,CmomandType> arithmeticList= new HashMap<>();
        static {
            arithmeticList.put("add",CmomandType.C_ARITHMETIC);
            arithmeticList.put("sub",CmomandType.C_ARITHMETIC);
            arithmeticList.put("neg",CmomandType.C_ARITHMETIC);
            arithmeticList.put("eq",CmomandType.C_ARITHMETIC);
            arithmeticList.put("gt",CmomandType.C_ARITHMETIC);
            arithmeticList.put("lt",CmomandType.C_ARITHMETIC);
            arithmeticList.put("and",CmomandType.C_ARITHMETIC);
            arithmeticList.put("or",CmomandType.C_ARITHMETIC);
            arithmeticList.put("not",CmomandType.C_ARITHMETIC);

            arithmeticList.put("push",CmomandType.C_PUSH);
            arithmeticList.put("pop",CmomandType.C_POP);

            //���̿���
            arithmeticList.put("label",CmomandType.C_LABLE);
            arithmeticList.put("if-goto",CmomandType.C_IF);
            arithmeticList.put("goto",CmomandType.C_GOTO);

            //����
            arithmeticList.put("function",CmomandType.C_FUNCTION);
            arithmeticList.put("return",CmomandType.C_RETURNN);
        }

    }


}
