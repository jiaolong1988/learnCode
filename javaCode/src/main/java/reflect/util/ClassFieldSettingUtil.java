package reflect.util;

import java.lang.reflect.Field;

/**
 * 将类的静态字段值 设置为 字段名称
 * @author: jiaolong
 * @date: 2024/11/19 15:02
 **/
public class ClassFieldSettingUtil {


  // 设置类静态字段值
  public static void setClassStaticFieldValue(){
        //当前类字段
        Field[] fields = null;
        try {
            //当前栈
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            //获取当前类的名称【栈顶元素就是当前类名称】
            String currentClassName = stackTrace[stackTrace.length - 1].getClassName();
            System.out.println("currentClassName:" + currentClassName);
            //获取当前类的所有字段
            fields = Class.forName(currentClassName).getDeclaredFields();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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