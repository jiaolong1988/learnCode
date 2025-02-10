package generics;

import java.util.Arrays;
import java.util.List;

/**
 * 在形参与类定义时均可设置 通配符。
 * @author jiaolong
 * @date 2025/02/10 14:24
 **/
public class GenericsClassUpDownTest {
    public static void main(String[] args) {

        //定义 类型形参不能使用通配符
        ClassDefinedRange1<Integer> a = new ClassDefinedRange1<>();
        List<Integer> listInteger = Arrays.asList(1, 2, 3);
        a.t1(listInteger);
        a.t2(listInteger);

    }
}

//定义类的形参
class ClassDefinedRange<T extends Number>{
    T t;

    //错误方式-因为T是一个确定类型，且Objct的子类有多个，因此使用T有误
    //public void t3 (List<T extends Object> s) {}

    public void t4 (List<T> s) {}
    public void t5 (T s) {}
}

//定义类的形参
class ClassDefinedRange1<T extends Number>{
    T t;
    public void t1 (List<? extends Object> s) {}
    public void t2 (List<T> s) {}
}
