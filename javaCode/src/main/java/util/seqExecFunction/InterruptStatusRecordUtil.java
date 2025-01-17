package util.seqExecFunction;


import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 中断状态记录工具类
 * @author jiaolong
 * @date 2024/02/01 11:26
 */
public class InterruptStatusRecordUtil {

    private static Logger logger = Logger.getLogger(InterruptStatusRecordUtil.class);
    public static final String F_STATUS = "F";
    public static final String T_STATUS = "T";

    public static final String TMP_DIR = "tmpinfo";
    public static String tmpinfoDir = Paths.get(TMP_DIR).toAbsolutePath().toString();

    private File interruptStatusRecordFile = null;
    private OrderedPropertiesUtil orderedPropertiesUtil = null;

    public InterruptStatusRecordUtil(){
        init();
    }

    public InterruptStatusRecordUtil(String fileName){
        init();
        interruptStatusRecordFile =  new File(tmpinfoDir + File.separator + fileName);
        orderedPropertiesUtil = new OrderedPropertiesUtil(interruptStatusRecordFile.getAbsolutePath());
    }

    public void setInterruptConfigFile(String fileName){
        interruptStatusRecordFile =  new File(tmpinfoDir + File.separator + fileName);
        orderedPropertiesUtil = new OrderedPropertiesUtil(interruptStatusRecordFile.getAbsolutePath());
    }

    /**
     * 中断配置文件相对路径
     * @return
     **/
    public String getInterruptConfigFile(){
        return TMP_DIR + File.separator + interruptStatusRecordFile.getName();
    }

    public File getConfigInterruptFile(){
        return interruptStatusRecordFile;
    }

    private void init(){
        File fdir = new File(tmpinfoDir);
        if (!fdir.exists()) {
            fdir.mkdirs();
        }
    }

    /**
     * 创建中断文件
     * @param c
     */
    public boolean createConfigFile(Class<?> c) {
        if (!interruptStatusRecordFile.exists()) {

            Map<String, String> infos = new LinkedHashMap<>();
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                infos.put(field.getName(), F_STATUS);
            }

            boolean flag = orderedPropertiesUtil.createProperFile(infos);
            logger.debug("create config.flag result:" + flag + "  fileName:" + interruptStatusRecordFile.getName());
            return flag;
        }else{
            String getAllValue = orderedPropertiesUtil.getAllValPrint();
            logger.warn("===>interrupt config file already exists. "+ getAllValue);
        }
        return true;
    }


    /**
     * 设置属性值为T
     *
     * @param attribute
     */
    public boolean updateConfigFileValueIsT(String attribute) {
        boolean flag = updateConfigFileValue(attribute, T_STATUS);
        if (flag) {
            logger.debug(interruptStatusRecordFile.getName() + " Update [" + attribute + "] status succeed.");
        } else {
            logger.error(interruptStatusRecordFile.getName() + " Update [" + attribute + "] status fail. ");
        }

        return flag;
    }


    /**
     * @param: attribute
     * @param: value
     * @param: file
     * @return: boolean
     **/
    public boolean updateConfigFileValue(String attribute, String value) {
            return orderedPropertiesUtil.setValToFile(attribute, value);
    }

    /**
     * 获取属性值
     *
     * @param attribute
     * @return
     */
    public String getConfigFileValue(String attribute) {
        return orderedPropertiesUtil.getVal(attribute);
    }



    /**
     * 删除文件
     */
    public boolean delConfigFile() {

        boolean flag = true;
        if (interruptStatusRecordFile.exists()) {
            logger.debug(interruptStatusRecordFile.getName() + " file is exist.");
            try {
                Files.delete(interruptStatusRecordFile.toPath());
                logger.debug("file delete successful. fileName:" + interruptStatusRecordFile.getName());
            } catch (IOException e) {
                flag = false;
                logger.error("file delete fail. fileName:" + interruptStatusRecordFile.getName() + "reason:" + e.getMessage(), e);
            }
        }

        return flag;
    }


}
