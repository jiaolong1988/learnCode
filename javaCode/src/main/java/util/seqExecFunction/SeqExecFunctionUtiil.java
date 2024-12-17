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

    /**
     *  获取顺序执行function的执行结果
     * @param clazz -
     * @return boolean
     **/
    public static boolean getExecResult(Class clazz) {
        List<Function<Boolean, Boolean>> execList = getExecListFunction(clazz);
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
    private static List<Function<Boolean, Boolean>> getExecListFunction(Class clazz) {

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
        execMethods.sort((m1, m2) -> m1.getName().compareTo(m2.getName()));

        //创建对象
        Object clazzObj = null;
        try {
            clazzObj = clazz.newInstance();
        } catch (Exception e) {
            logger.info(clazz.getName() + " create instance failed");
        }

        //创建function list
        List<Function<Boolean, Boolean>> list = new ArrayList<>(execMethods.size());
        for (Method method : execMethods) {
            list.add(createFunction( method,clazzObj));
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
            if (checkInfo) {
                try {
                    logger.info(execMethodName + ": method start exec.");

                    return (boolean) execMethod.invoke(clazzObj, null);
                } catch (Exception e) {
                    logger.info(execMethodName + " method exec failed,possible the method contains parameters.", e);
                }
            }else{
                logger.info(execMethodName + ":  no have exec.");
            }
            return false;
        };
    }

}
