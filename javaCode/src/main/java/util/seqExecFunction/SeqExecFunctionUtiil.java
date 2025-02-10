package util.seqExecFunction;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * @author jiaolong
 * @date 2024/12/16 15:13
 **/
public class SeqExecFunctionUtiil {
    private static final Logger logger = Logger.getLogger(SeqExecFunctionUtiil.class);

    private static final String EXEC_PREFIX = "exec";
    private static final String METHOD_NAME_SPLIT_CHAR = "_";

    /**
     *  获取顺序执行function的执行结果
     * @param execOperate -
     * @return boolean
     **/
    public static boolean getExecResult(Class execOperate, Class execStatus, Object parameter) {
        List<Function<Boolean, Boolean>> execList = getExecListFunction(execOperate, execStatus, parameter);
        if(execList== null || execList.size() == 0){
            return false;
        }

        Function<Boolean, Boolean> exec = null;
        boolean result = false;
        for (int i = 0; i <= execList.size(); i++) {
            if (i == 0) {
                exec = execList.get(i);

            } else if (i == execList.size()) {
                result = exec.apply(true);

            } else {
                exec = exec.andThen(execList.get(i));
            }
        }
        return result;
    }

    /**
     * 获取function的 列表
     * @param clazz -
     * @return java.util.List<java.util.function.Function<java.lang.Boolean,java.lang.Boolean>>
     **/
    private static List<Function<Boolean, Boolean>> getExecListFunction(Class clazz, Class execStatus, Object parameter) {

        Method[] methods = clazz.getMethods();
        if(methods == null || methods.length == 0){
            logger.info(clazz.getName() + " no exec method found:");
            return null;
        }

        List<Method> execMethods = new ArrayList<>();
        for (Method method : methods) {
            if (method.getName().startsWith(EXEC_PREFIX)) {
                execMethods.add(method);
            }
        }

        //根据方法名进行排序
        execMethods.sort((m1, m2) -> {
            try {
                Integer m1Int = Integer.parseInt(execMethodNameOrder(m1));
                Integer m2Int = Integer.parseInt(execMethodNameOrder(m2));

                return m1Int.compareTo(m2Int);
            } catch (NumberFormatException e) {
                String m1AllName = clazz.getName()+"."+m1.getName();
                String m2AllName = clazz.getName()+"."+m2.getName();
                logger.error("【"+m1AllName + "】 or 【" + m2AllName + "】 method name format error, exec method name must be start with exec and number." +
                        " [exec num _ statusName] or[exec num] eg: exec0_init or exec0",e);
                System.exit(1);
            }

            return 0;
        });

        //创建对象
        Object clazzObj = null;
        try {
            if(parameter == null)
                clazzObj = clazz.newInstance();
            else
                clazzObj = clazz.getConstructor(parameter.getClass()).newInstance(parameter);
        } catch (Exception e) {
            boolean flag = parameter!=null;
            logger.info("["+clazz.getName() + "] create instance failed, have parameter:"+flag,e);
            return null;
        }

        //获取方法名称 与 状态的对应关系
        HashMap<String, String> execMethodAndParam = new HashMap();
        for (Method method : execMethods) {
            String methodStatus = method.getName();
            if(methodStatus.contains(METHOD_NAME_SPLIT_CHAR)){
                methodStatus = method.getName().split(METHOD_NAME_SPLIT_CHAR)[1];
            }

            try {
                execStatus.getField(methodStatus);
                execMethodAndParam.put(method.getName(), methodStatus);
            } catch (NoSuchFieldException e) {
            }
        }

        //正确性校验
        if(execStatus.getDeclaredFields().length != execMethodAndParam.size()){
            int length = execStatus.getDeclaredFields().length;
            logger.error("exec method and exec status not match, execStatus.size !=execMethodAndParam.size, execStatus.size:"+length
                    +"execMethodAndParam: "+execMethodAndParam);
            System.exit(1);
        }


        //创建function list
        List<Function<Boolean, Boolean>> list = new ArrayList<>(execMethods.size());
        for (Method method : execMethods) {
            list.add(createFunction(execStatus, method, clazzObj, execMethodAndParam.get(method.getName())));
        }

        return list;
    }

    /**
     * exec0_init ==> 0
     * exec0 ==> 0
     * @param method -
     * @return java.lang.String
     **/
    private static String execMethodNameOrder(Method method) {
        String mExecName = method.getName().replace(EXEC_PREFIX, "");
        int mLastIndex = mExecName.lastIndexOf(METHOD_NAME_SPLIT_CHAR);
        if(mLastIndex >0){
            return mExecName.substring(0, mLastIndex);
        }else{
            return mExecName;
        }

    }

    /**
     * 创建 function
     * @param execMethod -
     * @param clazzObj -
     * @return java.util.function.Function<java.lang.Boolean,java.lang.Boolean>
     **/
    private static Function<Boolean, Boolean> createFunction(Class statusClass, Method execMethod, Object clazzObj, String execStatusName) {
        return (checkInfo) -> {
            String execMethodName = clazzObj.getClass().getName()+ "."+ execMethod.getName();
            //上一个方法执行成功才执行当前方法
            if (checkInfo) {
                try {
                    boolean resultFlag = false;
                    if(execMethod.getParameterCount() == 2){
                         resultFlag = (boolean) execMethod.invoke(clazzObj, execMethod.getName(), execStatusName);

                    }else if(execMethod.getParameterCount() == 1){
                        resultFlag = (boolean) execMethod.invoke(clazzObj, statusClass);

                    }else{
                        resultFlag = (boolean) execMethod.invoke(clazzObj);
                    }
                    if(!resultFlag){
                        logger.warn(execMethodName+" method exec failed,return false.");
                    }

                    return resultFlag;
                } catch (Exception e) {
                    logger.info(execMethodName + " method exec failed,possible the method contains parameters.", e);
                }
            }else{
                //上一个方法没执行成功，当前方法不执行
                logger.info(execMethodName + ": no have exec. [reason:the previous method was not executed successfully.]");
            }
            return false;
        };
    }

}
