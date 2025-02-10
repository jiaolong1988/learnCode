package generics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 泛型中的上限和下限测试
 * @author jiaolong
 * @date 2025/02/10 10:19
 **/
public class GenericsMethodUpDownTest {

    public static void main(String[] args) {
        testUp();

        testDown();
    }

    /**
     * 泛型上限测试
     * T类型是：Object类型 ,from 集合元素类型必须是Object类型，或其子类类型。
     * @return void
     **/
    private static void testUp() {
        List<String> from = new ArrayList<>();
        from.add("BB");

        List<Object> to = new ArrayList<>();
        to.add("aaa");
        to.add(123);

        //泛型上限测试 T类型是：Object
        GenericsUp(from, to);
    }


    /**
     * 声明一个泛型方法，该泛型方法中带一个T形参
     * @param from - T类型子类 的集合
     * @param to - T类型 的集合
     * @return void
     **/
    static <T> void GenericsUp(Collection<? extends T> from, Collection<T> to) {
        for (T ele : from) {
            boolean checkTypeResult = ele instanceof String ;
            System.out.println(" ==> testUp中T类型是否为String 类型："+checkTypeResult);
            to.add(ele);
        }
    }

    /**
     * 泛型下限测试
     *  T类型是：String类型 ,dest 集合元素类型必须是String类型，或其父类Object类型。
     * @return void
     **/
    private static void testDown() {
        List<Object> dest = new ArrayList<>();

        List<String> src = new ArrayList<>();
        src.add("1");

        //泛型下限测试 T类型是：String类型 ,dest 集合元素类型必须是String类型，或其父类Object类型。
        GenericsDown(dest , src);
    }

    /**
     *
     * 声明一个泛型方法，dest集合元素类型 必须是 T类型或其父类。
     * @param dest -
     * @param src -
     * @return T
     **/
    public static <T> T GenericsDown(Collection<? super T> dest, Collection<T> src) {
        for (T ele : src) {
            boolean checkTypeResult = ele instanceof String ;
            System.out.println(" ==> GenericsDownp中T类型是否为String 类型："+checkTypeResult);

            dest.add(ele);
        }
        return null;
    }

}
