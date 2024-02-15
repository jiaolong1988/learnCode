package designPatterns.Strategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jiaolong
 * @date 2024/01/15 17:21
 * @description: ����ģʽ�����汾���ž�if�����ж�
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
     * �жϸò����Ƿ�֧��
     */
    Boolean support(int price);
    int discountAmount(int skuPrice, T coupon);
}

class ACouponDiscount implements CouponDiscount<Integer> {

    public Boolean support(int price) {
        return price < 300;
    }
    public int discountAmount(int skuPrice, Integer coupon) {
        // TODO: ʵ���ۿ�Aҵ�����߼�
        return 1;
    }
}

class BCouponDiscount implements CouponDiscount<Integer> {

    public Boolean support(int price) {
        return price > 500;
    }
    public int discountAmount(int skuPrice, Integer coupon) {
        // TODO: ʵ���ۿ�Bҵ�����߼�
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
            // ���������������, ��ô��ʹ�øò��ԣ����������ѭ��
            if (v.support(skuPrice)) {
               return v.discountAmount(skuPrice, coupon);
            }
        }
        return -1;
    }
}