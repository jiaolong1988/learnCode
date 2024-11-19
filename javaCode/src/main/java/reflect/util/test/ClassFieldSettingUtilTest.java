package reflect.util.test;

import reflect.util.ClassFieldSettingUtil;

import java.lang.reflect.Field;


/**
 * 将当前类的的静态变量设置为字段名称测试
 * @author: jiaolong
 * @date: 2024/11/19 13:55
 **/
public class ClassFieldSettingUtilTest {
    //创建清单任务执行完毕
    public static  String taskEndFlag  ;
    //重命名清单
    public static  String renameList;
    //创建核对文件,先存日期，再存T
    public static  String createCheckDataFile;
    //更新load文件临时表的use字段值
    public static  String updateLoadFileTableField ;

    static{
        ClassFieldSettingUtil.setClassStaticFieldValue();
    }

    public static void main(String[] args) throws Exception {
        ClassFieldSettingUtilTest.taskEndFlag="F";
        for (Field f : ClassFieldSettingUtilTest.class.getDeclaredFields()) {
            String info = String.format("name:%1$-25s  value: %2$-7s" ,f.getName(),f.get(null));
            System.out.println(info);
        }
    }
}
