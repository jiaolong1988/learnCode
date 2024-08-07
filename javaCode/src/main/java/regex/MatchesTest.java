package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: jiaolong
 * @date: 2024/07/02 16:08
 **/
public class MatchesTest {
    public static void main(String[] args) {

        String[] mails = {
                        "kongyeeku@163.com",
                        "kongyeeku@gmail.com",
                        "ligang@crazyit.org",
                        "wawa@abc.xx"
         };
        String mailRegEx = "\\w{3,20}@\\w+\\.(com|org|cn|net|gov)";

        Pattern mailPattern = Pattern.compile(mailRegEx);
        Matcher matcher = null;
        for (String mail : mails) {
            if (matcher == null) {
                //创建matcher
                matcher = mailPattern.matcher(mail);
            } else {
                //重置matcher
                matcher.reset(mail);
            }

            //验证是否匹配
            String result = mail + (matcher.matches() ? "是" : "不是")
                                 + "一个有效的邮件地址！";
            System.out.println(result);
        }
    }
}
