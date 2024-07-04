package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 查找电话号码
 * @author: jiaolong
 * @date: 2024/07/02 15:28
 **/
public class FindGroup {
    public static void main(String[] args){

        String str = "我想求购一本《疯狂Java讲义》，尽快联系我13500006665"
                + "交朋友，电话号码是13611125565"
                + "出售二手电脑，联系方式15899903312";
        // 创建一个Pattern对象，并用它建立一个Matcher对象
        // 该正则表达式只抓取13X和15X段的手机号，
        // 实际要抓取哪些电话号码，只要修改正则表达式即可。
        String rule = "((13\\d)|(15\\d))\\d{8}";
        Matcher m = Pattern.compile(rule)
                           .matcher(str);
        System.out.println(m.matches());
        // 将所有符合正则表达式的子串（电话号码）全部输出
        while(m.find()){
            System.out.println(m.group());
        }
    }
}
