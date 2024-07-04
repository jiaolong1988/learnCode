package regex;

/**
 * 正则表达式的匹配方式
 * @author: jiaolong
 * @date: 2024/07/02 15:45
 **/
public class ThreeModes {
    public static void main(String[] args) {
        String str = "hello , java!";
        //贪婪模式(最大匹配)
        System.out.println("最大匹配,替换第一个单词:"+
                str.replaceFirst("\\w*","[]"));

        //勉强模式（最小匹配）
        System.out.println("最小匹配,替换第一个单词:"+
                str.replaceFirst("\\w*?","[]"));

    }
}
