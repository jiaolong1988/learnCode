package generics;

/**
 * 泛型构造器测试
 * @author jiaolong
 * @date 2025/02/10 13:53
 **/
public class GenericConstructorDefinedTest {
    public static void main(String[] args) {
        // 1.泛型构造器中的T参数为String。
        new Foo("疯狂Java讲义");

        // 2.泛型构造器中的T参数为Integer。
        new Foo(200);

        // 3.显式指定泛型构造器中的T参数为String，
        // 传给Foo构造器的实参也是String对象，完全正确。
        new <String>Foo("疯狂Android讲义");

        /*
          4.显式指定泛型构造器中的T参数为String，
            但传给Foo构造器的实参是Double对象，下面代码出错
         */
        //new <String> Foo(12.3);
    }

   static class Foo {
        public <T> Foo(T t) {
            System.out.println(t);
        }
    }
}

