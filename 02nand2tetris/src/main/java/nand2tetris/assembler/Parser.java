package nand2tetris.assembler;


/**
 * @author jiaolong
 * @date 2023/12/12 17:30
 */
public class Parser {

    //返回指令格式
    public static CmomandType commandType(String command){
        CmomandType cmomandType = null;
        if(command.contains("@")){
            cmomandType=CmomandType.A_COMMAND;
        }
        if(command.contains("=") || command.contains(";")){
            cmomandType=CmomandType.C_COMMAND;
        }
        if(command.contains("(") && command.contains(")")){
            cmomandType=CmomandType.L_COMMAND;
        }
        return cmomandType;
    }

    //返回标签符号
    public static String symbol(String command){
        String res = "";
        CmomandType type = commandType(command);
        if(type == CmomandType.A_COMMAND){
            res = command.substring(1, command.length());
        }

        if(type == CmomandType.L_COMMAND){
            res = command.substring(1, command.length()-1);
        }
        return res;
    }

    //返回C指令汇编助记符
    public static String dest(String command){
        String res = "";
        if(command.contains("=")){
            res = command.substring(0, command.lastIndexOf("="));
        }
        return res;
    }

    public static String comp(String command){
        String res = "";

        int endIndex = 0;
        if(command.contains(";")){
            endIndex = command.indexOf(";");
        }else{
            endIndex = command.length();
        }

        if(command.contains("=") ){
            res =  command.substring(command.indexOf("=")+1, endIndex);
        }else{
            res =  command.substring(0, endIndex);
        }

        if(res.length()== 0){
            System.err.println("comp command info not exist, commadm:"+command);
            System.exit(0);
        }
        return res;
    }

    public static String jump(String command){
        String res = "";
        if(command.contains(";")){
            res = command.substring(command.indexOf(";")+1, command.length());
        }
        return res;
    }

}
