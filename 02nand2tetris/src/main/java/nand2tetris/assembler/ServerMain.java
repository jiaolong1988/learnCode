package nand2tetris.assembler;

import nand2tetris.CommonUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jiaolong
 * @date 2024-1-19 17:17
 * 将汇编语言 翻译成 二进制机器语言
 */
public class ServerMain {

    public static void main(String[] args) {

        /**
         1.读取文件
         2.符号表
         3.语法解析
         4.编码
         5.机器码输出
         */

        //获取汇编文件路径
        Path sourcePath = Paths.get("D:\\test\\06");
        List<String> files = CommonUtils.getFiles(sourcePath);

        for(String file : files){
            Path filePath = Paths.get(file);
            System.out.println("读取汇编文件："+file);
            //读取asm文件内容
            List<String> asmInfo = CommonUtils.readFile(filePath);
            //将asm文件中的 伪命令 生成 对应的生成符号表
            Map<String, Integer> symbolTable = getSymbolTable(asmInfo);
            //将汇编内容 翻译成 机器码
            List<String> binaryProgram = getBinaryCode(asmInfo, symbolTable);

            //将机器码程序输出
            CommonUtils.outputFile(filePath, binaryProgram,"hack");
            //binaryProgram.forEach(x -> System.out.println(x));
        }


    }


    /**
     * 将汇编中 伪命令生成 对应的符号表既，key:命令值, value:rom地址
     * @param asmInfo
     * @return
     */
    public static Map<String, Integer> getSymbolTable(List<String> asmInfo){
        Map<String, Integer> symbolTable = InitData.symbolTable;

        //指令位置
        int romAddress = 0;
        for(String info : asmInfo){
            String command = CommonUtils.getCommand(info);
            if(command.length() > 0){
                CmomandType ct = Parser.commandType(command);
                if(ct == CmomandType.L_COMMAND){
                    String symbolInfo = Parser.symbol(info);
                    symbolTable.put(symbolInfo, romAddress);
                }else{
                    romAddress++;
                }
            }
        }
        return symbolTable;
    }


    /**
     *
     * @author jiaolong
     * @date 2023-12-14 17:27
        获取二进制机器码程序
     */
    private static ArrayList<String> getBinaryCode(List<String> asmInfo, Map<String, Integer> symbolTable) {
        //变量在内存的位置
        AtomicInteger ramNUm = new AtomicInteger(16);
        //Integer ramNUm=16;
        ArrayList<String> binaryProgram = new ArrayList<>();

        for(String info : asmInfo) {
            String command = CommonUtils.getCommand(info);
            if (command.length() != 0) {

                //翻译A地址与C指令，伪指令跳过
                CmomandType ct = Parser.commandType(command);
                if(ct != CmomandType.L_COMMAND){
                    String binaryCode = transToBinary(command, symbolTable, ramNUm);
                    binaryProgram.add(binaryCode);
                }
            }
        }
        return binaryProgram;
    }

    /**
     *
     * @author jiaolong
     * @date 2023-12-14 16:54
        将指令汇编翻译为二进制机器代码
     */
    public static String transToBinary(final String command, final Map<String, Integer> symbolTable,  AtomicInteger ramNum){
        String returnInfo = "";

        CmomandType ct = Parser.commandType(command);
        if(ct == CmomandType.A_COMMAND){
            //获取地址
            String address = command.substring(1, command.length());
            if(Utils.isNumber(address)){
                //A地址是数字
                returnInfo = Utils.toBinary(address);
            }else{
                //A地址是符号标签
                if(symbolTable.containsKey(address)){
                   String newAdd = symbolTable.get(address).toString();
                   returnInfo = Utils.toBinary(newAdd);
                }else{
                    //A地址是变量
                    returnInfo = Utils.toBinary(ramNum.toString());
                    symbolTable.put(address, ramNum.get());

                    ramNum.incrementAndGet();
                }
            }
        }

        if(ct == CmomandType.C_COMMAND){
            String comp = Code.compToBinary(Parser.comp(command));
            String dest = Code.destToBinary(Parser.dest(command));
            String jump = Code.jumpToBinary(Parser.jump(command));

            StringBuilder cInfo = new StringBuilder("111");
            cInfo.append(comp);
            cInfo.append(dest);
            cInfo.append(jump);

            returnInfo = cInfo.toString();
        }

       return returnInfo;
    }


}
