package designPatterns.Strategy;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jiaolong
 * @date 2024/01/15 17:21
 * @description: 策略模式升级版本，杜绝if条件判断
 *  https://mp.weixin.qq.com/s/WK80UZEjhPZnIPwgQc1dYQ
 */
public class StrategyPatternUpgradeTest {

    public static void main(String[] args) {
        CouponDiscount<Integer> c1 = new ACouponDiscount();
        CouponDiscount<Integer> c2 = new BCouponDiscount();

        List<CouponDiscount> list = new ArrayList<>();
        list.add(c1);
        list.add(c2);


        CouponDiscountService cs = new CouponDiscountService();
        cs.setList(list);

        int x = cs.discountAmount(2,300);
        int y = cs.discountAmount(3,600);

        System.out.println(  String.format("%1$s %2$s",x,y));

    }
}


interface CouponDiscount<T> {
    /**
     * 判断该策略是否支持
     */
    Boolean support(int price);
    int discountAmount(int skuPrice, T coupon);
}

class ACouponDiscount implements CouponDiscount<Integer> {

    public Boolean support(int price) {
        return price < 300;
    }
    public int discountAmount(int skuPrice, Integer coupon) {
        // TODO: 实现折扣A业务处理逻辑
        return 1;
    }
}

class BCouponDiscount implements CouponDiscount<Integer> {

    public Boolean support(int price) {
        return price > 500;
    }
    public int discountAmount(int skuPrice, Integer coupon) {
        // TODO: 实现折扣B业务处理逻辑
        return 2;
    }
}

class CouponDiscountService {
    private static List<CouponDiscount> list;
    public void setList(List<CouponDiscount> list){
        this.list = list;
    }

    public int discountAmount(int coupon, int skuPrice) {
        for (CouponDiscount v : list) {
            // 这里如果满足条件, 那么就使用该策略，不满足继续循环
            if (v.support(skuPrice)) {
               return v.discountAmount(skuPrice, coupon);
            }
        }
        return -1;
    }
}