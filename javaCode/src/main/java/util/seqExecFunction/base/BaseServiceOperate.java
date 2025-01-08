package util.seqExecFunction.base;

import org.apache.log4j.Logger;
import util.seqExecFunction.InterruptStatusRecordUtil;

import java.util.function.Supplier;

/**
 *
 * @author jiaolong
 * @date 2024 /12/17 14:18
 */
public class BaseServiceOperate {
    private static Logger logger = Logger.getLogger(BaseServiceOperate.class);

    protected static final String STATUS_F = InterruptStatusRecordUtil.F_STATUS;

    public boolean commonExecUpdateConfigFileValueT(Supplier<Boolean> sm, StatusInfoCheck statusInfoCheck) {
       return commonExec(sm,statusInfoCheck,true);
    }
    public boolean commonExecNotUpdateConfigFileValueT(Supplier<Boolean> sm, StatusInfoCheck statusInfoCheck) {
        return commonExec(sm,statusInfoCheck,false);
    }

    /**
     * @param sm                           任务函数
     * @param isUpdateConfigFileValueT     true:修改，false：不修改
     * @param statusInfoCheck              the status info check
     * @return the boolean
     */
    private boolean commonExec(Supplier<Boolean> sm, StatusInfoCheck statusInfoCheck, boolean isUpdateConfigFileValueT) {
        String statusField = statusInfoCheck.getStatusField();
        boolean isExec = isExecCheck(statusInfoCheck, isUpdateConfigFileValueT);
        if(isExec){
            //sm.get() 返回true,表示任务执行成功
            if(sm.get()){
                if(isUpdateConfigFileValueT){
                    //将字段状态更新为T
                    return statusInfoCheck.getInterruptStatus().updateConfigFileValueIsT(statusField);
                }else{
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
    public StatusInfoCheck initCheck(String statusField, String methodName, InterruptStatusRecordUtil  interruptStatus ){
        String statusVal = interruptStatus.getConfigFileValue(statusField);
        String methodNameAll = this.getClass().getName()+"."+methodName;

        StatusInfoCheck statusInfoCheck = new StatusInfoCheck();
        statusInfoCheck.setStatusField(statusField);
        statusInfoCheck.setStatusVal(statusVal);
        statusInfoCheck.setMethodName(methodNameAll);
        statusInfoCheck.setInterruptStatus(interruptStatus);

        return statusInfoCheck;
    }


    /**
     * 执行字段检查
     * @param isUpdateConfigFileValueT     true:修改，false：不修改
     * @return true: 执行任务， false: 不执行任务
     */
    public boolean isExecCheck(StatusInfoCheck statusInfoCheck, boolean isUpdateConfigFileValueT){
        String statusVal= statusInfoCheck.getStatusVal();
        if(statusVal == null || statusVal.length() == 0){
            String interruptConfigFile = statusInfoCheck.getInterruptStatus().getInterruptConfigFile();
            logger.error(interruptConfigFile+" : 【"+statusInfoCheck.getStatusField()+"】 field is null.");
            System.exit(0);
        }

        if(statusVal.trim().equals(InterruptStatusRecordUtil.T_STATUS)){
            logger.info(statusInfoCheck.getMethodName()+": method already exec.");

        }else{
            if(!statusVal.equals(InterruptStatusRecordUtil.F_STATUS) && isUpdateConfigFileValueT){
               logger.error("==> status filed value is not F or T, please check it. filedName:"+statusInfoCheck.getStatusField()+"  filedValue:"+statusVal);
            }
            return true;
        }

        return false;
    }


    public class StatusInfoCheck {
        private String statusField;
        private String statusVal;
        private String methodName;
        private InterruptStatusRecordUtil interruptStatus ;

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
    }

}
