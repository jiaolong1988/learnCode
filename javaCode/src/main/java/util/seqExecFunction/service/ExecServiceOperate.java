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

    public ExecServiceOperate(ExecParameter execParameter){
        //固定写法 初始化中断文件
        interruptStatus.interruptConfigFileInit(TmpInfoConfig.tmpinfoDir,execTaskStatusFile);
        this.execParameter = execParameter;
        //this.isDelStatus = execParameter.isDelStatus();

        this.addAttribute("aaa");
        this.addAttributeAndValue("bbb","222");
    }

    public boolean exec1_ftpFileName(String methodName, String statusName) {
        Supplier<Boolean> func = ()->{
            String ftpFileName = interruptStatus.getConfigFileValue(statusName);
            if(ftpFileName.equals(STATUS_F)){
                //设置配置文件 ftp文件名称
                return interruptStatus.updateConfigFileValue(statusName, execParameter.getImportFile().getName());
            }else{
                //同步ftp文件名称到执行参数中
                String filePath = String.join(File.separator,"test", ftpFileName);
                File ftpFile = new File(filePath);
                execParameter.setImportFile(ftpFile);
                return true;
            }
        } ;
        return commonExecNotUpdateConfigFileValueT(func, statusName, methodName);
    }

    public boolean exec2_ftpFileBak(String methodName, String statusName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;
        return commonExecUpdateConfigFileValueT(func,statusName, methodName);
    }


    public boolean exec3_createTempTable(String methodName, String statusName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;
        return commonExecUpdateConfigFileValueT(func,statusName, methodName);
    }

    public boolean exec4_importData(String methodName, String statusName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;
        return commonExecUpdateConfigFileValueT(func, statusName, methodName);
    }

    public boolean exec5_bakImportData(String methodName, String statusName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;
        return commonExecUpdateConfigFileValueT(func, statusName, methodName);
    }
    public boolean exec6_outputPkuuidData(String methodName, String statusName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;
        return commonExecUpdateConfigFileValueT(func, statusName, methodName);
    }
    public boolean exec7_renameOutputPkuuidDataUniq(String methodName, String statusName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;
        return commonExecUpdateConfigFileValueT(func, statusName, methodName);
    }
    public boolean exec8_outputBizData(String methodName, String statusName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;
        return commonExecUpdateConfigFileValueT(func, statusName, methodName);
    }

    public boolean exec9_outputPageData(String methodName, String statusName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;
        return commonExecUpdateConfigFileValueT(func, statusName, methodName);
    }

    public boolean exec10_dropTempTable(String methodName, String statusName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;
        return commonExecUpdateConfigFileValueT(func, statusName, methodName);
    }

    public boolean exec11_batchNumUpdate(String methodName, String statusName) {
        Supplier<Boolean> func = ()->{
            return true;
        } ;

        return commonExecUpdateConfigFileValueOfMiddenValT(func, statusName, methodName);
    }

}

