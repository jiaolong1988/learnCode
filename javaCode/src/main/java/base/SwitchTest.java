package base;

/**
 * 标准写法
 * @author: jiaolong
 * @date: 2024/06/13 15:10
 **/
public class SwitchTest {
    public static void main(String[] args) {

        String x = "";
        int c = 1;
        switch (c) {
            case 1:
                x = case1();
                break;

            case 2:
                x = case2();
                break;

            default:
                x="未知等级";
        }
        System.out.println("return: " + x);
    }

    public static String case1(){
        return "优秀";
    }
    public static String case2(){
        return "及格";
    }

}
