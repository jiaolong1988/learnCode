package util.seqExecFunction;

import java.lang.reflect.Field;

/**
 * @author jiaolong
 * @date 2024/12/16 15:58
 **/
public class Test {
    public static void main(String[] args) throws Exception {

        for (Field f : ExecTaskStatus.class.getDeclaredFields()) {
            String info = String.format("name:%1$-25s  value: %2$-7s" ,f.getName(),f.get(null));
            System.out.println(info);
        }

        boolean result = SeqExecFunctionUtiil.getExecResult(ExecServiceOperate.class);
        System.out.println("执行结果: "+result);
    }

}
