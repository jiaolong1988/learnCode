package util.seqExecFunction.service;


import util.seqExecFunction.base.BaseTaskStatus;

/**
 * @author jiaolong
 * @date 2024/12/17 13:41
 **/
public class ExecTaskStatus extends BaseTaskStatus {
    public static String ftpFileName;
    public static String ftpFileBak;
    public static String createTempTable;
    public static String importData;
    public static String bakImportData;

    public static String outputPkuuidData;
    public static String renameOutputPkuuidDataUniq;
    public static String outputBizData;
    public static String outputPageData;

    public static String batchNumUpdate;
    public static String dropTempTable;

    static {
        setAutoClassStaticFieldValue(ExecTaskStatus.class);
    }
}
