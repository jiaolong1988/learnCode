package lambda.baseuse;
/**
 * 2-延申
 * 通用函数式接口
 *
 * 1、lambda表达式是函数式接口的实例, 函数式接口是lambda表达式的类型。
 * 例：Processor stringProcessor = (String str) -> str.length();
 *
 * 2、函数式接口是只有一个抽象方法的接口，lambda表达式本身不能用作独立的表达式。
 *
 * 3、常用函数式接口
 *  java.util.function包中有常用函数式接口
 *  BiFunction、Predicate、Consumer、BiConsumer
 *  参考地址：https://www.w3cschool.cn/java/java-functional-interface.html
 */
public class LambdaSyntax2 {
    public static void main(String[] args) {
//        LambdaFunction<Integer,String> a = (x) -> x.toString();
//        System.out.println(a.apply(5));

//        LambdaFunction<Integer,Integer> a = (x,y) -> x>y;
//        System.out.println(a.apply(5, 6));

        LambdaFunction<Integer,Integer> a = (x,y) -> x+y;
        System.out.println(a.apply(5, 6));

//        LambdaFunction<Integer,String> a = () -> 4;
//        System.out.println(a.apply());

//        LambdaFunction<Integer,String> a = (x,y) -> System.out.println();
//        a.apply(5, "jiaolong");
    }
}

@FunctionalInterface
interface LambdaFunction<T,U>{
    //   U apply(T t);
    //    boolean apply(T t, U u);
        T apply(T t1, T t2);
    //    T apply();
    //   void apply(T t, U  u);
}