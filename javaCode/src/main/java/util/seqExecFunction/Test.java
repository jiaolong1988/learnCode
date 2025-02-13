package util.seqExecFunction;

import util.seqExecFunction.base.BaseServiceOperate;
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

        //1.执行参数
        ExecParameter parameter = new ExecParameter();
        parameter.setImportFile(new File("D:\\test.txt"));
        parameter.setDelStatus(false);

        //2.batchNum文件初始化
        if (!TmpInfoConfig.getBatchNumFile().exists()) {
            BaseServiceOperate.writeBatchNum(TmpInfoConfig.getBatchNumFile(),"0");
        }

        //3.执行
        boolean result = SeqExecFunctionUtiil.getExecResult(ExecServiceOperate.class, ExecTaskStatus.class, parameter);
        System.out.println("执行结果: "+result);

    }

}
