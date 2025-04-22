package util.seqExecFunction.base;

import org.apache.log4j.Logger;
import util.seqExecFunction.InterruptStatusRecordUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author jiaolong
 * @date 2024 /12/17 14:18
 */
public class BaseServiceOperate extends BaseSeviceOperateOfUtil{
    private static Logger logger = Logger.getLogger(BaseServiceOperate.class);
    protected InterruptStatusRecordUtil interruptStatus = new InterruptStatusRecordUtil();
    protected static final String STATUS_F = InterruptStatusRecordUtil.F_STATUS;
    protected static final String STATUS_T = InterruptStatusRecordUtil.T_STATUS;
    //true:删除状态文件，false：不删除
    protected static boolean isDelStatus = true;
    //是否中断
    protected boolean interruptFlag = false;

    //状态字段扩展信息
    private Map<String, String> expandInterruptStatusMap = new LinkedHashMap<>();
    public static final String nullValue = "";
    protected void addAttribute(String attributeName){
        expandInterruptStatusMap.put(attributeName,nullValue);
    }
    protected void addAttributeAndValue(String attributeName,String value){
        expandInterruptStatusMap.put(attributeName,value);
    }

    //第一个执行的方法：创建状态文件
    public boolean exec0(Class statusClass) {
        //固定写法
        if (interruptStatus.getConfigInterruptFile().exists()) {
            interruptFlag = true;
        }

        return interruptStatus.createConfigFileExpand(statusClass, expandInterruptStatusMap);
    }

    public boolean exec99() {
        if(isDelStatus){
            return interruptStatus.delConfigFile();
        }

        return true;
    }

    protected boolean commonExecUpdateConfigFileValueOfMiddenValT(Supplier<Boolean> sm, String statusField, String execMethodName) {
        StatusInfoCheck statusInfoCheck = getStatusInfo(statusField, execMethodName, interruptStatus);
        statusInfoCheck.setStatusValIsMiddleStatus(true);
        return commonExec(sm, statusInfoCheck, true);
    }

    protected boolean commonExecUpdateConfigFileValueT(Supplier<Boolean> sm, String statusField, String execMethodName) {
        StatusInfoCheck statusInfoCheck = getStatusInfo(statusField, execMethodName, interruptStatus);
        return commonExec(sm, statusInfoCheck, true);
    }

    protected boolean commonExecNotUpdateConfigFileValueT(Supplier<Boolean> sm, String statusField, String execMethodName) {
        StatusInfoCheck statusInfoCheck = getStatusInfo(statusField, execMethodName, interruptStatus);
        return commonExec(sm, statusInfoCheck, false);
    }

    /**
     * @param sm                       任务函数
     * @param isUpdateConfigFileValueT true:修改，false：不修改
     * @param statusInfoCheck          the status info check
     * @return the boolean
     */
    private boolean commonExec(Supplier<Boolean> sm, StatusInfoCheck statusInfoCheck, boolean isUpdateConfigFileValueT) {
        String statusField = statusInfoCheck.getStatusField();
        boolean isExec = fieldExecBeforeCheck(statusInfoCheck, isUpdateConfigFileValueT);
        if (isExec) {
            //sm.get() 返回true,表示任务执行成功
            if (sm.get()) {
                if (isUpdateConfigFileValueT) {
                    //将字段状态更新为T
                    return statusInfoCheck.getInterruptStatus().updateConfigFileValueIsT(statusField);
                } else {
                    return true;
                }
            }
        }
        return !isExec;
    }


    /**
     * Init check status info check.
     *
     * @param statusField     the status field
     * @param methodName      the method name
     * @param interruptStatus the interrupt status
     * @return the status info check
     */
    private StatusInfoCheck getStatusInfo(String statusField, String methodName, InterruptStatusRecordUtil interruptStatus) {
        String statusVal = interruptStatus.getConfigFileValue(statusField);
        String methodNameAll = this.getClass().getName() + "." + methodName;

        StatusInfoCheck statusInfoCheck = new StatusInfoCheck();
        statusInfoCheck.setStatusField(statusField);
        statusInfoCheck.setStatusVal(statusVal);
        statusInfoCheck.setMethodName(methodNameAll);
        statusInfoCheck.setInterruptStatus(interruptStatus);

        return statusInfoCheck;
    }


    /**
     * 执行前字段检查
     * @param isUpdateConfigFileValueT true:修改，false：不修改
     * @return true: 执行任务， false: 不执行任务
     */
    private boolean fieldExecBeforeCheck(StatusInfoCheck statusInfoCheck, boolean isUpdateConfigFileValueT) {
        String statusVal = statusInfoCheck.getStatusVal();
        if (statusVal == null || statusVal.length() == 0) {
            String interruptConfigFile = statusInfoCheck.getInterruptStatus().getInterruptConfigFile();
            logger.error(interruptConfigFile + " : 【" + statusInfoCheck.getStatusField() + "】 field is null.");
            System.exit(0);
        }

        String logX = "%1$-100s :method %2$-7s exec." ;

        if (statusVal.trim().equals(InterruptStatusRecordUtil.T_STATUS)) {
            logger.info(String.format(logX ,statusInfoCheck.getMethodName(),"already"));
        } else {
            /*
             * 处理所有非T状态的情况
             * 当状态为值为非F时，且字段必须修改为T时，报错返回false.
             * */
            if (!statusVal.equals(InterruptStatusRecordUtil.F_STATUS) && isUpdateConfigFileValueT && !statusInfoCheck.isStatusValIsMiddleStatus()) {
                logger.error("==> status filed value is not F or T, please check it. filedName:" + statusInfoCheck.getStatusField() + "  filedValue:" + statusVal);
                return false;
            }

            if(isUpdateConfigFileValueT){
                logger.info(String.format(logX ,statusInfoCheck.getMethodName(),"start"));

            }else{
                if(!statusVal.equals(InterruptStatusRecordUtil.F_STATUS)){
                    logger.info(String.format(logX ,statusInfoCheck.getMethodName(),"already"));
                }else{
                    logger.info(String.format(logX ,statusInfoCheck.getMethodName(),"start"));
                }
            }

            return true;
        }

        return false;
    }

    private class StatusInfoCheck {
        private String statusField;
        private String statusVal;
        private String methodName;
        //状态值是否 有中间状态
        private boolean statusValIsMiddleStatus = false;
        private InterruptStatusRecordUtil interruptStatus;

        public String getStatusField() {
            return statusField;
        }


        public void setStatusField(String statusField) {
            this.statusField = statusField;
        }


        public String getStatusVal() {
            return statusVal;
        }


        public void setStatusVal(String statusVal) {
            this.statusVal = statusVal;
        }


        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public InterruptStatusRecordUtil getInterruptStatus() {
            return interruptStatus;
        }

        public void setInterruptStatus(InterruptStatusRecordUtil interruptStatus) {
            this.interruptStatus = interruptStatus;
        }

        public boolean isStatusValIsMiddleStatus() {
            return statusValIsMiddleStatus;
        }

        public void setStatusValIsMiddleStatus(boolean statusValIsMiddleStatus) {
            this.statusValIsMiddleStatus = statusValIsMiddleStatus;
        }
    }


}
