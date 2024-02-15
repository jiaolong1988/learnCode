package nand2tetris.assembler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author jiaolong
 * @date 2023/12/13 11:33
 */
public class Utils {


    /**
     * @author yingfeng
     * @date 2023-12-13 12:00
     * 判断字符串是否是数字
     */
    public static boolean isNumber(String str) {
        return str != null && str.chars().allMatch(Character::isDigit);
    }

    /**
     * @author yingfeng
     * @date 2023-12-14 16:17
     * 将字符串数字转换位16位的二进制
     */
    public static String toBinary(String address) {
        String binaryString = Integer.toBinaryString(Integer.valueOf(address));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < (16 - binaryString.length()); i++) {
            sb.append("0");
        }
        sb.append(binaryString);
        return sb.toString();
    }

}