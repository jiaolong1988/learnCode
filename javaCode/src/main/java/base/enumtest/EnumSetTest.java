package base.enumtest;

import java.util.EnumSet;

/**
 * 枚举的集合操作
 * @author jiaolong
 * @date 2025/01/20 10:34
 **/
enum Season {
    SPRING, SUMMER, FALL, WINTER
}

public class EnumSetTest {
    public static void main(String[] args) {
        // 创建一个EnumSet集合，集合元素就是Season枚举类的全部枚举值
        EnumSet<Season> es1 = EnumSet.allOf(Season.class);
        System.out.println("es1:" + es1); // 输出[SPRING,SUMMER,FALL,WINTER]

        // 创建一个EnumSet空集合，指定其集合元素是Season类的枚举值。
        EnumSet<Season> es2 = EnumSet.noneOf(Season.class);
        System.out.println("es2:" + es2); // 输出[]

        // 手动添加两个元素
        es2.add(Season.WINTER);
        es2.add(Season.SPRING);
        System.out.println("es2:" + es2);


        // 以指定枚举值创建EnumSet集合
        EnumSet es3 = EnumSet.of(Season.SUMMER, Season.WINTER);
        System.out.println("es3:" + es3); // 输出[SUMMER,WINTER]


        EnumSet es4 = EnumSet.range(Season.SUMMER, Season.WINTER);
        System.out.println("es4:" + es4); // 输出[SUMMER,FALL,WINTER]
        // 新创建的EnumSet集合的元素和es4集合的元素有相同类型，
        // es5的集合元素 + es4集合元素 = Season枚举类的全部枚举值
        EnumSet es5 = EnumSet.complementOf(es4);
        System.out.println("es5:" + es5); // 输出[SPRING]

        EnumSet es6 = es4.complementOf(es2);
        System.out.println("es6:" + es6); // 输出[SUMMER, FALL]
    }
}
