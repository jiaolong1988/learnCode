package util.seqExecFunction.base;

import java.lang.reflect.Field;

/**
 * @author jiaolong
 * @date 2024/12/17 14:11
 **/
public class BaseTaskStatus {

    // 设置类静态字段值
    public static void setAutoClassStaticFieldValue(Class clazz){
        //当前类字段
        Field[] fields = null;
        try {
            fields = clazz.getDeclaredFields();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        //设置当前类的字段值
        if(fields!=null && fields.length>0){
            for(Field field:fields){
                try {
                    //设置当前类的字段值
                    field.set(null, field.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
