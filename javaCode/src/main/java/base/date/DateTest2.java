package base.date;

import java.util.Calendar;

/**
 * @author jiaolong
 * @date 2025/07/31 14:02
 * java.util.Date与java.util.Calendar中的所有属性都是可变的
 * https://pdai.tech/md/java/java8/java8-localdatetime.html
 **/
public class DateTest2 {
    public static void main(String[] args) {
        Calendar birth = Calendar.getInstance();
        birth.set(1975, Calendar.MAY, 26);
        Calendar now = Calendar.getInstance();
        System.out.println(daysBetween(birth, now));
        System.out.println(daysBetween(birth, now)); // 显示 0?

    }

    /**
     * daysBetween有点问题，如果连续计算两个Date实例的话，第二次会取得0，因为Calendar状态是可变的，考虑到重复计算的场合，最好复制一个新的Calendar
     **/
    public static long daysBetween(Calendar begin, Calendar end) {
        long daysBetween = 0;
        while(begin.before(end)) {
            begin.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }
}
