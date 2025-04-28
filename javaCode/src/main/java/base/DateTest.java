package base;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @author jiaolong
 * @date 2025/03/31 16:07
 **/
public class DateTest {
    public static void main(String[] args) throws Exception {
        DateFormatTest();
        //    calendarTest();
        //  isLegalDate();


    }
    /**
     * 日期格式验证
     * @return
     **/
    public static boolean isLegalDate(int length, String sDate,String format) {
        int legalLen = length;
        if ((sDate == null) || (sDate.length() != legalLen)) {
            return false;
        }
        DateFormat formatter = new SimpleDateFormat(format);
        // 设置严格解析
        formatter.setLenient(false);
        try {
            Date date = formatter.parse(sDate);
            return sDate.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    public static void DateFormatTest()throws Exception{

        //1.String转换成Date类型
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        // 设置严格解析
        ft.setLenient(false);

        String time = "2019-09-19";
        Date date1 = ft.parse(time);

        //2.Date转换成String类型
       // SimpleDateFormat ft1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date2 = new Date();
        String time1 = ft.format(date2);




        //3.Date 转 LocalDateTime
        Date startDate = new Date();
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime localDateTime1 = startDate.toInstant() // 将 Date 转换为 Instant
                                                .atZone(ZoneId.systemDefault()) // 使用系统默认时区
                                                .toLocalDateTime(); // 转换为 LocalDateTime

        // LocalDateTime 转 Date
        Date date = Date.from(localDateTime1.atZone(ZoneId.systemDefault()).toInstant());



        //4.日期格式化-线程安全
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTimeStr = localDateTime1.format(dateTimeFormatter);
        System.out.println(dateTimeStr);

        // 定义格式化模式 Begin_time:2024-09-26-21.32.11.878110
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.ss.SSSSSS");
        // 格式化当前日期时间
        String formattedDateTime = LocalDateTime.now().format(formatter);
        System.out.println("formattedDateTime:"+formattedDateTime);


        //5.日期格式化-线程不安全
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("YYYYMMddHHmm");
        String transformDate=simpleDateFormat.format(new Date());
        System.out.println("日期转换后："+transformDate+"\n");

    }

    public static void calendarTest() {
        Calendar dataCompare = Calendar.getInstance();
        //日期减法操作
        dataCompare.add(Calendar.DATE, -1);
        System.out.println(dataCompare.getTime());

        //日期的比较
        boolean x = dataCompare.getTime().compareTo(new Date())<0;
        System.out.println("dataCompare 是否小于当前日期 ："+x+"\n");

        //设置 毫秒值为0
        dataCompare.set(Calendar.MILLISECOND, 0);


        Calendar now = Calendar.getInstance();
        //获取日期信息
        System.out.println("年: " + now.get(Calendar.YEAR));
        System.out.println("月: " + (now.get(Calendar.MONTH) + 1) + "");
        System.out.println("日: " + now.get(Calendar.DAY_OF_MONTH));


        //设置起时间
        Calendar cal = Calendar.getInstance();
        //cal.setTime(new Date());
        cal.set(2022, 3,4);
        System.out.println("设置时间："+cal.getTime());

        //时间操作
        cal.add(Calendar.YEAR, 1);//增加一年
        //cd.add(Calendar.DATE, 1);//增加一天
        //cd.add(Calendar.DATE, -10);//减10天
        //cd.add(Calendar.MONTH, 1);//增加一个月

        System.out.println("时间操作输出: "+cal.getTime());

    }


}

