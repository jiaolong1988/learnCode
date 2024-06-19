package nand2tetris.vm2;

import nand2tetris.CommonUtils;
import nand2tetris.vm1.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author jiaolong
 * @date 2024/01/22 10:19
 * @description: 对vm命令进行解析
 */
public class Parser {
    //vm命令集合迭代器
    private Iterator<String> iterator = null;
    //当前命令
    private String currentCommand = "";

    //构造函数初始化
    public Parser(String file){
        Path filePath = Paths.get(file);
        System.out.println(file);

        //读取vm文件内容
        List<String> vmInfo = CommonUtils.readFile(filePath);

        //获取真正的命令集合
        List<String> list = new ArrayList<>();
        for(String tempVmInfo :vmInfo){
            String command = CommonUtils.getCommand(tempVmInfo);
            if(command.length()>0){
                list.add(command);
            }
        }

        iterator = list.iterator();
    }

    //是否有命令
    private boolean hasMoreCommands(){
       return iterator.hasNext();
    }

    //读取下一条指令，并将其设置为当前命令
    public boolean advance(){
        boolean flag = hasMoreCommands();
        if(flag){
            currentCommand  = iterator.next().trim();
            System.out.println("vm info："+currentCommand);
        }
        return flag;
    }

    //返回当前vm命令的类型
    public CmomandType commandType(){
        String operationCommand  = currentCommand.split(" ")[0];
        if(!InnerUtil.arithmeticList.containsKey(operationCommand)){
            throw new RuntimeException("当前命令类型不存在，命令："+currentCommand);
        }

        return InnerUtil.arithmeticList.get(operationCommand);

    }

    public String arg1(){
        String result = "";
        if(CmomandType.C_ARITHMETIC == commandType()){
            //算术运算符直接返回命令本身
            result = currentCommand;
        }else{
            //命令的第一个参数
            result = currentCommand.split(" ")[1];
        }
        return result;
    }

    public String arg2(){
        String result = "";
        if(CmomandType.C_PUSH == commandType() || CmomandType.C_POP == commandType()||
            CmomandType.C_FUNCTION == commandType() ||  CmomandType.C_CALL == commandType()){
            //命令的第二个参数
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

            //流程控制
            arithmeticList.put("label",CmomandType.C_LABLE);
            arithmeticList.put("if-goto",CmomandType.C_IF);
            arithmeticList.put("goto",CmomandType.C_GOTO);

            //方法
            arithmeticList.put("function",CmomandType.C_FUNCTION);
            arithmeticList.put("return",CmomandType.C_RETURNN);
            arithmeticList.put("call",CmomandType.C_CALL);
        }

    }


}
