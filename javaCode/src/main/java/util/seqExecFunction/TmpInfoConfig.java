package util.seqExecFunction;

import java.io.File;
import java.nio.file.Paths;

/**
 * @author jiaolong
 * @date 2025/01/06 16:41
 **/
public class TmpInfoConfig {
    //存储目录
    public static final String tmpinfoDir = "tmpinfo";
    //绝对地址
    public static String tmpinfoDirAddress = Paths.get(tmpinfoDir).toAbsolutePath().toString();

    //中断文件状态名称
    public static final String execTaskStatusFileName = "execTaskStatus.flag";
    public static final String batchNumFileName = "batchNum.flag";

    public static String getExecTaskStatusPath(){
        return String.join(File.separator,tmpinfoDirAddress, execTaskStatusFileName);
    }
    public static File getExecTaskStatusFile(){
        return Paths.get(getExecTaskStatusPath()).toFile();
    }

    public static String getBatchNumPath(){
        return String.join(File.separator,tmpinfoDirAddress, batchNumFileName);
    }

    public static File getBatchNumFile(){
        return Paths.get(getBatchNumPath()).toFile();
    }
}
