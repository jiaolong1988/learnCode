package base;

/**
 * ��׼д��
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
                x="δ֪�ȼ�";
        }
        System.out.println("return: " + x);
    }

    public static String case1(){
        return "����";
    }
    public static String case2(){
        return "����";
    }

}
