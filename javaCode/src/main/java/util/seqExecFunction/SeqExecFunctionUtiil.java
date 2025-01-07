package util.seqExecFunction;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author jiaolong
 * @date 2024/12/16 15:13
 **/
public class SeqExecFunctionUtiil {
    private static final Logger logger = Logger.getLogger(SeqExecFunctionUtiil.class);

    private static final String EXEC_PREFIX = "exec";
    public static final boolean PRINT_LOG_FLAG = false;

    /**
     *  获取顺序执行function的执行结果
     * @param clazz -
     * @return boolean
     **/
    public static boolean getExecResult(Class clazz, Object parameter) {
        List<Function<Boolean, Boolean>> execList = getExecListFunction(clazz, parameter);
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
    private static List<Function<Boolean, Boolean>> getExecListFunction(Class clazz, Object parameter) {

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
                Integer m1Int = Integer.parseInt(m1.getName().replace(EXEC_PREFIX, ""));
                Integer m2Int = Integer.parseInt(m2.getName().replace(EXEC_PREFIX, ""));

                return m1Int.compareTo(m2Int);
            } catch (NumberFormatException e) {
                String m1AllName = clazz.getName()+"."+m1.getName();
                String m2AllName = clazz.getName()+"."+m2.getName();
                logger.error("【"+m1AllName + "】 or 【" + m2AllName + "】 method name format error, exec method name must be start with exec and end with number.",e);
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
            logger.info(clazz.getName() + " create instance failed, have parameter:"+parameter!=null);
        }

        //创建function list
        List<Function<Boolean, Boolean>> list = new ArrayList<>(execMethods.size());
        for (Method method : execMethods) {
            list.add(createFunction(method, clazzObj));
        }

        return list;
    }

    /**
     * 创建 function
     * @param execMethod -
     * @param clazzObj -
     * @return java.util.function.Function<java.lang.Boolean,java.lang.Boolean>
     **/
    private static Function<Boolean, Boolean> createFunction(Method execMethod, Object clazzObj) {
        return (checkInfo) -> {
            String execMethodName = clazzObj.getClass().getName()+ "."+ execMethod.getName();
            //上一个方法执行成功才执行当前方法
            if (checkInfo) {
                try {
                    if(PRINT_LOG_FLAG){
                        logger.info(execMethodName + ": method start exec.");
                    }

                    boolean resultFlag = (boolean) execMethod.invoke(clazzObj, null);
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
