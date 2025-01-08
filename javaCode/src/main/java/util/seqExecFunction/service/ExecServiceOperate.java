package util.seqExecFunction.service;

import org.apache.log4j.Logger;
import util.seqExecFunction.InterruptStatusRecordUtil;
import util.seqExecFunction.base.BaseServiceOperate;

import java.io.File;
import java.util.function.Supplier;

/**
 * 顺序执行的业务逻辑
 * 方法必须以exec开头，数字x表示顺序，数字越小，执行顺序越靠前.
 *
 * @author jiaolong
 * @date 2024/12/16 15:56
 **/
public class ExecServiceOperate extends BaseServiceOperate {
    private static final Logger logger = Logger.getLogger(ExecServiceOperate.class);
    private InterruptStatusRecordUtil interruptStatus = new InterruptStatusRecordUtil("ExecTaskStatus.flag");
    private ExecParameter parameter;

    public ExecServiceOperate(ExecParameter parameter){
        logger.info("构造函数初始化："+ parameter.getImportFile().getName());
        this.parameter = parameter;
    }

    public boolean exec0() {
        //固定写法 修改参数，将中断信息同步
        if (interruptStatus.getConfigInterruptFile().exists()) {
            String ftpFileName = interruptStatus.getConfigFileValue(ExecTaskStatus.ftpFileName);
            if(!ftpFileName.equals(STATUS_F)){
                File ftpFile = new File(interruptStatus.getConfigFileValue(ExecTaskStatus.ftpFileName));
                this.parameter.setImportFile(ftpFile);
            }
        }
        return interruptStatus.createConfigFile(ExecTaskStatus.class);
    }
    
    public boolean exec01() {
        StatusInfoCheck statusInfoCheck = initCheck(ExecTaskStatus.ftpFileName,"exec1", interruptStatus);

        Supplier<Boolean> func = ()->{
            logger.info("exec exec1 func.......");
            return true;
        } ;
        return commonExecNotUpdateConfigFileValueT(func, statusInfoCheck);
    }

    public boolean exec2() {
        StatusInfoCheck statusInfoCheck = initCheck(ExecTaskStatus.ftpFileBak,"exec2", interruptStatus);

        Supplier<Boolean> func = ()->{
            logger.info("exec exec2 func.......");
            return true;
        } ;

        return commonExecUpdateConfigFileValueT(func, statusInfoCheck);
    }



    public boolean exec3() {
        StatusInfoCheck statusInfoCheck = initCheck(ExecTaskStatus.createTempTable,"exec3", interruptStatus);

        Supplier<Boolean> func = ()->{
            logger.info("exec exec3 func.......");
            return true;
        } ;

        return commonExecUpdateConfigFileValueT(func, statusInfoCheck);
    }

    public boolean exec9() {
        logger.info("exec10 func  delete interruptConfig file.......");
        return true;
    }
}

