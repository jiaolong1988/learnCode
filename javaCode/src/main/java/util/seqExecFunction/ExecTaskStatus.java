package util.seqExecFunction;


import util.seqExecFunction.base.BaseTaskStatus;

/**
 * @author jiaolong
 * @date 2024/12/17 13:41
 **/
public class ExecTaskStatus extends BaseTaskStatus {
    //创建清单任务执行完毕
    public static String taskEndFlag;
    //重命名清单
    public static String renameList;
    //创建核对文件,先存日期，再存T
    public static String createCheckDataFile;

    static {
        setAutoClassStaticFieldValue(ExecTaskStatus.class);
    }
}
