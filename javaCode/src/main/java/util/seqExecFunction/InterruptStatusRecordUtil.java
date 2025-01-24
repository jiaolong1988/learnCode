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

    private String TMP_DIR = null;
    private String tmpinfoDir = null;
    private File interruptStatusRecordFile = null;
    private OrderedPropertiesUtil orderedPropertiesUtil = null;


    public void interruptConfigFileInit(String tmpinfoDirName, String fileName){
        this.TMP_DIR = tmpinfoDirName;
        this.tmpinfoDir = Paths.get(TMP_DIR).toAbsolutePath().toString();
        init();

        interruptStatusRecordFile = Paths.get(tmpinfoDir,fileName).toFile();
        orderedPropertiesUtil = new OrderedPropertiesUtil(interruptStatusRecordFile.getAbsolutePath());
    }

    /**
     * 创建初始化目录
     * @return void
     **/
    private void init(){
        File fdir = new File(tmpinfoDir);
        if (!fdir.exists()) {
            fdir.mkdirs();
        }
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
        if (interruptStatusRecordFile.exists()) {
            logger.debug(interruptStatusRecordFile.getName() + " file is exist.");
            try {
                Files.delete(interruptStatusRecordFile.toPath());
                logger.debug("file delete successful. fileName:" + interruptStatusRecordFile.getName());
                return true;
            } catch (IOException e) {
                logger.error("file delete fail. fileName:" + interruptStatusRecordFile.getName() + "reason:" + e.getMessage(), e);
            }
        }

        return false;
    }


}
