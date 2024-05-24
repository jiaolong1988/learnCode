package nand2tetris.compiler2.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jiaolong
 * @date: 2024/05/23 17:23
 **/
public class InitData {
   public static final  Map<String, ArithmeticOperate> op = new HashMap<>();
   static {
        op.put("+",ArithmeticOperate.ADD);
        op.put("-",ArithmeticOperate.SUB);
//        op.put("*",ArithmeticOperate);
//        op.put("/",ArithmeticOperate);
//        op.put("&",ArithmeticOperate);
//        op.put("|",ArithmeticOperate);
        op.put("<",ArithmeticOperate.LT);
        op.put(">",ArithmeticOperate.GT);
        op.put("=",ArithmeticOperate.EQ);
   }

}
