package nand2tetris.vm2Pro;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: jiaolong
 * @date: 2024/03/18 17:16
 **/
public class ReturnAddressValueUtil {
    private static AtomicInteger returnAddressValue = new AtomicInteger(0);
    //重置
    public static void reset(){
        returnAddressValue.set(0);
    }

    //获取返回地址的唯一值
    public static int getOnlyValue(){
       return returnAddressValue.incrementAndGet();
    }

    public static void main(String[] args) {
        System.out.println(ReturnAddressValueUtil.getOnlyValue());
        System.out.println(ReturnAddressValueUtil.getOnlyValue());
        ReturnAddressValueUtil.reset();
        System.out.println(ReturnAddressValueUtil.getOnlyValue());
    }
}
