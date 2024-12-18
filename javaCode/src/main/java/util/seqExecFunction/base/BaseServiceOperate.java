package util.seqExecFunction.base;

import org.apache.log4j.Logger;
import util.seqExecFunction.ExecTaskStatus;
import util.seqExecFunction.InterruptStatusRecordUtil;

import java.util.function.Supplier;

/**
 * @author jiaolong
 * @date 2024/12/17 14:18
 **/
public class BaseServiceOperate {
    private static Logger logger = Logger.getLogger(BaseServiceOperate.class);

    public boolean commonExec(Supplier<Boolean> sm, StatusInfoCheck statusInfoCheck) {
        String statusField = statusInfoCheck.getStatusField();
        boolean isExec = isExecCheck(statusInfoCheck);
        if(isExec){
            if(sm.get()){
                //写具体的业务逻辑代码 并给给出返回结果
                return statusInfoCheck.getInterruptStatus().updateConfigFileValueIsT(statusField);
            }
        }
        return !isExec;
    }


    public boolean beforeExec(String statusField, String methodName, InterruptStatusRecordUtil  interruptStatus) {
        StatusInfoCheck statusInfoCheck = initCheck(statusField, methodName, interruptStatus);
        return isExecCheck(statusInfoCheck);
    }

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

    public boolean isExecCheck(StatusInfoCheck statusInfoCheck){
        String statusVal= statusInfoCheck.getStatusVal();
        if(statusVal == null || statusVal.isEmpty()){
            String interruptConfigFile = statusInfoCheck.getInterruptStatus().getInterruptConfigFile();
            logger.error(interruptConfigFile+" : 【"+statusInfoCheck.getStatusField()+"】 field is null.");
            System.exit(0);
        }

        if(statusVal.equals(InterruptStatusRecordUtil.T_STATUS)){
            logger.info(statusInfoCheck.getMethodName()+": method already exec.");
        }

        if(statusVal.equals(InterruptStatusRecordUtil.F_STATUS)){
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
