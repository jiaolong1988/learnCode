package util.seqExecFunction;

import org.apache.log4j.Logger;
import util.seqExecFunction.base.BaseServiceOperate;

import java.util.function.Function;
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
    private InterruptStatusRecordUtil  interruptStatus = new InterruptStatusRecordUtil("ExecTaskStatus.flag");

    public boolean exec0() {
        return interruptStatus.createConfigFile(ExecTaskStatus.class);
    }
    
    public boolean exec01() {
        StatusInfoCheck statusInfoCheck = initCheck(ExecTaskStatus.taskEndFlag,"exec1", interruptStatus);

        Supplier<Boolean> func = ()->{
            logger.info("exec exec1 func.......");
            return false;
        } ;

        return commonExec(func, statusInfoCheck);
    }

    public boolean exec2() {
        StatusInfoCheck statusInfoCheck = initCheck(ExecTaskStatus.renameList,"exec2", interruptStatus);

        Supplier<Boolean> func = ()->{
            logger.info("exec exec2 func.......");
            return true;
        } ;

        return commonExec(func, statusInfoCheck);
    }



    public boolean exec3() {
        StatusInfoCheck statusInfoCheck = initCheck(ExecTaskStatus.createCheckDataFile,"exec3", interruptStatus);

        Supplier<Boolean> func = ()->{
            logger.info("exec exec3 func.......");
            return true;
        } ;

        return commonExec(func, statusInfoCheck);
    }

    public boolean exec9() {
        logger.info("exec10 func  delete interruptConfig file.......");
        return true;
    }
}

