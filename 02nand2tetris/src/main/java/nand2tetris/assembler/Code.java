package nand2tetris.assembler;

import java.util.Optional;

/**
 * @author jiaolong
 * @date 2023/12/12 17:30
 */
public class Code {

    public static String destToBinary(String destString){
        return Optional.ofNullable(InitData.destCommand.get(destString)).orElse("000");
    }

    public static String compToBinary(String compString){
        String compInfo = Optional.ofNullable(InitData.compCommand.get(compString)).orElse("");

        return compInfo;
    }

    public static String jumpToBinary(String jumpString){
        return Optional.ofNullable(InitData.jumpCommand.get(jumpString)).orElse("000");
    }
}
