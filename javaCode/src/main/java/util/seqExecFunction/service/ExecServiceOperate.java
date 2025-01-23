package util.seqExecFunction.service;

import org.apache.log4j.Logger;
import util.seqExecFunction.TmpInfoConfig;
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
    private String execTaskStatusFile = TmpInfoConfig.execTaskStatusFileName;
    private ExecParameter execParameter;
    //是否中断
    private boolean interruptFlag = false;

    public ExecServiceOperate(ExecParameter execParameter){
        //固定写法 初始化中断文件
        interruptStatus.interruptConfigFileInit(TmpInfoConfig.tmpinfoDir,execTaskStatusFile);
        this.execParameter = execParameter;
    }

    public boolean exec0_init(String methodName) {
        //固定写法
        if (interruptStatus.getConfigInterruptFile().exists()) {
            interruptFlag = true;
        }
        return interruptStatus.createConfigFile(ExecTaskStatus.class);
    }
    
    public boolean exec1_ftpFileName(String methodName) {
        Supplier<Boolean> func = ()->{
            String ftpFileName = interruptStatus.getConfigFileValue(ExecTaskStatus.ftpFileName);
            if(ftpFileName.equals(STATUS_F)){
                //设置配置文件 ftp文件名称
                return interruptStatus.updateConfigFileValue(ExecTaskStatus.ftpFileName, execParameter.getImportFile().getName());
            }else{
                //同步ftp文件名称到执行参数中
                String filePath = String.join(File.separator,"test", ftpFileName);
                File ftpFile = new File(filePath);
                execParameter.setImportFile(ftpFile);
                return true;
            }
        } ;
        return commonExecNotUpdateConfigFileValueT(func, ExecTaskStatus.ftpFileName, methodName);
    }

    public boolean exec2_ftpFileBak(String methodName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;
        return commonExecUpdateConfigFileValueT(func, ExecTaskStatus.ftpFileBak, methodName);
    }


    public boolean exec3_createTempTable(String methodName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;
        return commonExecUpdateConfigFileValueT(func, ExecTaskStatus.createTempTable, methodName);
    }

    public boolean exec4_importData(String methodName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;
        return commonExecUpdateConfigFileValueT(func, ExecTaskStatus.importData, methodName);
    }

    public boolean exec5_bakImportData(String methodName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;
        return commonExecUpdateConfigFileValueT(func, ExecTaskStatus.bakImportData, methodName);
    }
    public boolean exec6_outputPkuuidData(String methodName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;
        return commonExecUpdateConfigFileValueT(func, ExecTaskStatus.outputPkuuidData, methodName);
    }
    public boolean exec7_renameOutputPkuuidDataUniq(String methodName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;
        return commonExecUpdateConfigFileValueT(func, ExecTaskStatus.renameOutputPkuuidDataUniq, methodName);
    }
    public boolean exec8_outputBizData(String methodName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;
        return commonExecUpdateConfigFileValueT(func, ExecTaskStatus.outputBizData, methodName);
    }

    public boolean exec9_outputPageData(String methodName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;
        return commonExecUpdateConfigFileValueT(func, ExecTaskStatus.outputPageData, methodName);
    }

    public boolean exec10_batchNumUpdate(String methodName) {

        Supplier<Boolean> func = ()->{
            //模拟创建批量号文件
            writeBatchNum(TmpInfoConfig.getBatchNumFile(),"0");
            return  updateBatchNum(ExecTaskStatus.batchNumUpdate, TmpInfoConfig.getBatchNumFile());
        } ;
        return commonExecUpdateConfigFileValueOfMiddenValT(func, ExecTaskStatus.batchNumUpdate, methodName);
    }
    public boolean exec11_dropTempTable(String methodName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;
        return commonExecUpdateConfigFileValueT(func, ExecTaskStatus.dropTempTable, methodName);
    }

    public boolean exec9_delInterruptedFile(String methodName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;
        return commonExecUpdateConfigFileValueT(func, ExecTaskStatus.delInterruptedFile, methodName);
    }
}

