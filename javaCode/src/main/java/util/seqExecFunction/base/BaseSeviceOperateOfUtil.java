package util.seqExecFunction.base;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * 常用工具方法汇总
 * @author jiaolong
 * @date 2025/04/22 16:22
 **/
public class BaseSeviceOperateOfUtil {
    private static Logger logger = Logger.getLogger(BaseSeviceOperateOfUtil.class);
    protected int pageCount;

    /**
     * 如果读取的文件内容不是数字，则退出程序
     * @param readFile -
     * @return int
     **/
    protected int readFileToInt(File readFile){
        try {
            String content = readFileToString(readFile);
            if(content != null){
                return Integer.parseInt(content);
            }

        }catch (NumberFormatException e) {
            logger.error("read file content is not a number."+readFile.getName(), e);
        }
        System.exit(0);
        return -1;
    }

    /**
     * 读取文件内容失败，返回结果为null
     * @param readFile -
     * @return java.lang.String
     **/
    protected String readFileToString(File readFile){
        try {
            return new String(Files.readAllBytes(readFile.toPath()));
        }catch (IOException e) {
            logger.error("read file info fail."+readFile.getName(),e);
        }
        return null;
    }

    protected boolean writeFileInfo(File writeFile, String writeInfo) {
        try {
            Files.write(writeFile.toPath(), writeInfo.getBytes());
            return true;
        } catch (IOException e) {
            logger.error("write file info fail."+writeFile.getName(), e);
        }
        return false;
    }

    /**
     * 删除文件目录
     * @param delDir
     * @return
     */
    protected boolean deleteDir(File delDir) {
        Path path = delDir.toPath();
        if (!delDir.exists()) {
            return true;
        }

        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            return true;

        } catch (IOException e) {
            logger.error("delete dir fail. dir:" + delDir, e);
        }

        return false;
    }

    /**
     * 删除文件
     * @param outputPath -
     * @return boolean
     **/
    protected boolean delFile(File outputPath) {
        if(!outputPath.exists()){
            return true;
        }

        try {
            Files.delete(outputPath.toPath());
            return true;
        } catch (IOException e) {
            logger.error("create .list fail , file exit, delete fail.",e);
        }
        return false;
    }

    //常用分页工具

    protected <T> List<List<T>> splitList(List<T> list, int len) {
        List<List<T>> result = new ArrayList<>();
        int size = list.size();
        int count = (size + len - 1) / len;
        for (int i = 0; i < count; i++) {
            List<T> subList = list.subList(i * len, ((i + 1) * len > size ? size : len * (i + 1)));
            result.add(subList);
        }
        return result;
    }

    protected int getBatchCount(int totalCount) {
        int totalPages=0;
        if (totalCount > 0){
            totalPages = (totalCount + pageCount - 1) / pageCount;
        }else{
            totalPages = 0;
        }
        return totalPages;
    }

    protected int getBeginIndex(int pageIndex){
        return (pageCount*pageIndex)+1;
    }

    protected int getEndIndex(int pageIndex){
        return pageCount*(pageIndex+1);
    }

}
