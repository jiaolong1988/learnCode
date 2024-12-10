package lambda;

import org.junit.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 2-四大函数接口使用
 * 函数式接口使用方法,既：Consumer，Function，Predicate，
 * Supplier接口的所有方法测试
 *
 * @author 		jiaolong
 * @date 		2022-09-21 10:30:19
 */
public class UseFunctionInterfaceWay {
    public static void main(String[] args) {
        consumerTest();
        functionTest();
        predicateTest();
        supplierTest();
    }

    public static void consumerTest() {
        Consumer<String> f = System.out::println;
        Consumer<String> f2 = n -> System.out.println(n + "-F2");
        Consumer<String> f3 = n -> System.out.println(n + "-F3");
        Consumer<String> f4 = n -> System.out.println(n + "-F4");

        // 执行完F后再执行F2的Accept方法
        f.andThen(f2).accept("test");

        System.out.println("===============");
        // 连续执行F的Accept方法
        f.andThen(f2).andThen(f3).andThen(f4).accept("test");

    }

    public static void functionTest() {
        Function<Integer, Integer> f = s -> {
            return ++s;
        };
        Function<Integer, Integer> g = s -> s * 2;

        /**
         * 下面表示执行F时使用 G的输出 当作输入。
         * 相当于以下代码：
         * Integer a = g.apply(1);
         * System.out.println(f.apply(a));
         */
        System.out.println("compose:"+f.compose(g).apply(1));

        /**
         * 表示执行F的Apply后使用其返回的值当作输入再执行G的Apply；
         * 相当于以下代码
         * Integer a = f.apply(1);
         * System.out.println(g.apply(a));
         */
        System.out.println("andThen:"+f.andThen(g).apply(1));

        /**
         * identity方法会返回一个不进行任何处理的Function实例，即输出与输入值相等；
         * 与f,g的实现一样，只是系统的默认实现而已
         */
        System.out.println("identity:"+Function.identity().apply("a"));
    }

    /**
     * Assert junit的测试类
     * Predicate测试
     */
    private static void predicateTest() {
        //实现test方法
        Predicate<String> p = o -> o.equals("test");
        Predicate<String> g = o -> o.startsWith("test");

        /**
         * negate: 用于对原来的Predicate做取反处理；
         * 如当调用p.test("test")为True时，调用p.negate().test("test")就会是False；
         */
        boolean negate = p.negate().test("test");
        System.out.println("negate:"+negate);
        Assert.assertFalse("测试异常negate:", negate);

        //and: 针对同一输入值，多个Predicate均返回True时返回True，否则返回False；
        boolean and = p.and(g).test("test");
        System.out.println("and:"+and);
        Assert.assertTrue("测试异常and:", and);


        //or: 针对同一输入值，多个Predicate只要有一个返回True则返回True，否则返回False
        boolean or = p.or(g).test("test");
        System.out.println("or:"+or);
        Assert.assertTrue("测试异常or:", or);

        //静态的isEqual方法
        boolean eq= p.equals(g);
        System.out.println("eq:"+eq);
    }


    private static void supplierTest() {
        //get方法每次生成一个Stream，用完之后流就自动关闭。
        Supplier<Stream<String>> s = () -> Arrays.asList("1", "2", "3", "4").stream();
        List<String> l = s.get().collect(Collectors.toList());
        System.out.println(l);

    }

}
