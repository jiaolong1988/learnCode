package util.seqExecFunction.base;

import org.apache.log4j.Logger;
import util.seqExecFunction.InterruptStatusRecordUtil;
import util.seqExecFunction.WorkTimeUtil;

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

    protected String worktime = null;
    //是否 打印执行日志
    protected boolean printExecLogFlag = true;

    //是否中断
    private boolean interruptFlag = false;
    protected boolean isInterruptFlag() {
        return interruptFlag;
    }

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

    //删除中断状态文件
    public boolean exec99() {
       return interruptStatus.delConfigFile();
    }


    /**
     * 1.当 (字段值有中间状态)，如有三种状态：T、2025、F时，<br>
     * 2.执行指定的方法，中断文件的字段值 【更新为T】
     * @param sm - 执行函数
     * @param statusField - 状态字段
     * @param execMethodName - 执行的方法名称
     * @return boolean
     **/
    protected boolean commonExecUpdateConfigFileValueOfMiddenValT(Supplier<Boolean> sm, String statusField, String execMethodName) {
        StatusInfoCheck statusInfoCheck = getStatusInfo(statusField, execMethodName, interruptStatus);
        statusInfoCheck.setStatusValIsMiddleStatus(true);
        return commonExec(sm, statusInfoCheck, true);
    }

    /**
     * 执行指定的方法，中断文件的字段值 【更新为T】
     * @param sm - 执行函数
     * @param statusField - 状态字段
     * @param execMethodName - 执行的方法名称
     * @return boolean
     **/
    protected boolean commonExecUpdateConfigFileValueT(Supplier<Boolean> sm, String statusField, String execMethodName) {
        StatusInfoCheck statusInfoCheck = getStatusInfo(statusField, execMethodName, interruptStatus);
        return commonExec(sm, statusInfoCheck, true);
    }

    /**
     * 执行指定的方法，但中断文件的字段值 【不更新】
     * @param sm - 执行函数
     * @param statusField - 状态字段
     * @param execMethodName - 执行的方法名称
     * @return boolean
     **/
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
        //工作时间检查
        WorkTimeUtil.getWorkTimeCheck(worktime);

        //执行状态对应的 业务方法
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
             * 处理所有非T状态的情况，且字段状态必须更新为T
             * 且字段中间状态为false时，报错返回false.
             * */
            if (!statusVal.equals(InterruptStatusRecordUtil.F_STATUS) && isUpdateConfigFileValueT
                    && !statusInfoCheck.isStatusValIsMiddleStatus()) {
                logger.error("==> status filed value is not F or T, please check it. filedName:" + statusInfoCheck.getStatusField() + "  filedValue:" + statusVal);
                return false;
            }

            if(isUpdateConfigFileValueT){
                if(printExecLogFlag){
                    logger.info(String.format(logX ,statusInfoCheck.getMethodName(),"start"));
                }

            }else{
                if(!statusVal.equals(InterruptStatusRecordUtil.F_STATUS)){
                    logger.info(String.format(logX ,statusInfoCheck.getMethodName(),"already"));
                }else{
                    if(printExecLogFlag){
                        logger.info(String.format(logX ,statusInfoCheck.getMethodName(),"start"));
                    }
                }
            }

            return true;
        }

        return false;
    }

    private class StatusInfoCheck {
        //中断文件的 字段名称
        private String statusField;
        //中断文件的 字段名称对应的 值
        private String statusVal;
        //中断文件的 字段名称 对应的执行方法名称
        private String methodName;
        /**
         * 状态值是否 有中间状态。
         *  true:有中间状态， false:没有中间状态
         ***/
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
