package base.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * @author jiaolong
 * @date 2025/07/31 14:15
 * 计算时间间隔
 **/
public class DateMinusDateOfNumber {
    public static void main(String[] args) {
        execCheckStartTime();
    }
    private static void execCheckStartTime() {
        //当前日期时间
        LocalDateTime currentDateTime = LocalDateTime.now();
        //指定开始时间
        LocalDate givenDate = LocalDate.of(2025, 7, 24);
        LocalDateTime givenDateTime = givenDate.atStartOfDay();

        long sleepSeconds = ChronoUnit.SECONDS.between(currentDateTime, givenDateTime);
        System.out.println("sDate:"+currentDateTime+", eDate:"+givenDateTime+", interval(s)："+sleepSeconds);

        while(true){
            LocalDate currentDate = LocalDate.now();

            // 判断当前日期是否大于等于给定日期
            if (currentDate.isAfter(givenDate) || currentDate.isEqual(givenDate)) {
                //检查程序是否在工作时间

            } else {
                System.out.println("当前日期小于给定日期，程序不执行。");
                try {
                    TimeUnit.SECONDS.sleep(sleepSeconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
