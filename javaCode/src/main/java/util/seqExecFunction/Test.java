package util.seqExecFunction;

import util.seqExecFunction.service.ExecParameter;
import util.seqExecFunction.service.ExecServiceOperate;
import util.seqExecFunction.service.ExecTaskStatus;

import java.io.File;
import java.lang.reflect.Field;

/**
 * @author jiaolong
 * @date 2024/12/16 15:58
 **/
public class Test {
    public static void main(String[] args) throws Exception {

//        for (Field f : ExecTaskStatus.class.getDeclaredFields()) {
//            String info = String.format("name:%1$-25s  value: %2$-7s" ,f.getName(),f.get(null));
//            System.out.println(info);
//        }

        ExecParameter parameter = new ExecParameter();
        File file = new File("D:\\test.txt");
        parameter.setImportFile(file);
        boolean result = SeqExecFunctionUtiil.getExecResult(ExecServiceOperate.class, parameter);
        System.out.println("执行结果: "+result);
    }

}
