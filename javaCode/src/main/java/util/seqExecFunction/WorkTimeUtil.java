package util.seqExecFunction;

import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 工作时间检查工具
 */
public class WorkTimeUtil {
    private static Logger logger = Logger.getLogger(WorkTimeUtil.class);

    public static void main(String[] args) {
        String worktime = "10:00-12:00,15:00-18:00";
       // worktime = ",17:00-18:00";
    //     worktime = "";
        boolean flag = getWorkTimeFlag(worktime);
        System.out.println("是否是工作时间: " + flag);
    }

    /**
     * @param workTimeStr 如果为空时，程序不进行将时间检查
     **/
    public static boolean getWorkTimeFlag(String workTimeStr) {
        if(workTimeStr.trim().length() == 0){
            logger.info("=================>>>WorkTime is null, not check worktime.");
            return true;
        }

        boolean logFlag = true;
        while (true) {
            List<String> workTimeList = getWorkTimeList(workTimeStr);
            if (checkTime(workTimeList,workTimeStr)) {
                break;
            } else {
                if (logFlag) {
                    logger.info("=================>>>Not in workTime[" + workTimeStr + "],Task No Exec.");
                    logFlag = false;
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    logger.error("sleep is error. ", e);
                }
            }
        }
        return true;
    }

    /**
     * 判断时间格式是否符合要求，
     * 1. 已逗号分行为多组
     * 2. 每组分为:开始时间、结束时间
     * 3. 开始时间、结束时间都必须是HH:mm格式
     * 4. 开始时间小于结束时间
     * eg: 09:00-12:00,13:00-18:00
     * @return
     */
    private static List<String> getWorkTimeList(String workTimeStr) {
        List<String> tempList = new ArrayList<>();

        if (isNotBlank(workTimeStr) && workTimeStr.indexOf(",") >= 0) {
            //包含 ,字符
            String[] timeGroups = string2Array(workTimeStr, ",");
            for (String timeGroup : timeGroups) {
                String[] timeArrays = string2Array(timeGroup, "-");
                if (timeArrays == null || timeArrays.length % 2 == 1) {
                    logger.error("workTimeStr format error. workTimeStr[" + workTimeStr + "]");
                    System.exit(0);
                }
                getTimeInfo(workTimeStr, tempList, timeArrays);
            }
        }else{
            //包含-字符
            if (isNotBlank(workTimeStr) && workTimeStr.indexOf("-") > 0) {
                String[] timeArrays = string2Array(workTimeStr,"-");
                getTimeInfo(workTimeStr, tempList, timeArrays);
            }else{
                logger.error("workTimeStr format error. workTimeStr[" + workTimeStr + "]");
                System.exit(0);
            }
        }

        if (tempList.size() == 0){
            logger.info("=================>>>WorkTime is null, worktime is . ["+workTimeStr+"]");
            System.exit(0);
        }

        return tempList;
    }

    private static void getTimeInfo(String workTimeStr, List<String> tempList, String[] timeArrays) {
        for (int i = 0; i < timeArrays.length; i++) {
            if (i % 2 == 0 && i + 1 < timeArrays.length) {
                String begin = timeArrays[i].trim();
                String end = timeArrays[i + 1].trim();
                if (isTimestr(begin) && isTimestr(end) && timeCompare(begin, end)) {
                     tempList.add(begin);
                     tempList.add(end);
                } else {
                    logger.error("workTimeStr error. workTimeStr[" + workTimeStr + "]");
                    System.exit(0);
                }
            }
        }
    }

    private static boolean checkTime(List<String> workTimeList, String workTimeString) {
        boolean result = true;
        if (workTimeList != null && workTimeList.size() >= 2 && workTimeList.size() % 2 == 0) {
            String timeStr = "";
            for (int i = 0; i < workTimeList.size(); i++) {
                if (i % 2 == 0) {
                    timeStr = WorkTimeUtil.getCurrentTimeStr();
                    String begin = workTimeList.get(i);
                    String end = workTimeList.get(i + 1);
                    //当前时间 大于 【开始时间】小于【结束时间】，则返回true
                    if (timeStr.compareTo(begin) >= 0 && timeStr.compareTo(end) <= 0) {
                        return true;
                    }
                }
            }
            result = false;
        } else {
            logger.error("workTimeList exception error. "+workTimeString);
        }
        return result;
    }



    private static String[] string2Array(String srcString, String sep) {
        String[] ret = null;
        if (isNotBlank(srcString)) {
            ret = srcString.split(sep);
        }
        return ret;
    }

    private static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    private static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }


    /**
     * 得到当前时分秒字符串
     */
    private static String getCurrentTimeStr() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(currentTime);
    }

    /**
     * 判断是否合法的时分秒字符串
     */
    private static boolean isTimestr(String timeStr) {
        DateFormat format = new SimpleDateFormat("HH:mm");
        // 设置严格解析
        format.setLenient(false);

        try {
            format.parse(timeStr);
            return true;
        } catch (Exception e) {
            logger.error("error timeStr[" + timeStr + "], " + e.getMessage());
        }
        return false;
    }

    private static boolean timeCompare(String begin, String end) {
        if(!(begin.compareTo(end) < 0) ){
            logger.error("begin !< end . error timeStr[" + begin + "]" + "  timeStr[" + end + "]");
            return false;
        }else{
            return true;
        }
    }

}