package generics;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 泛型方法定义
 * @author jiaolong
 * @date 2025/02/10 13:57
 **/
public class GenericMethodDefinedTest {

    static <T> T tt(T t1, T t2) {
        return t1;
    }


    public static void main(String[] args){

        /**
         * 泛型方法通过 参数类型中(带泛型的参数)  是否存在父子关系 的来确定方法中泛型的实际类型
        */

        Collection<String> t1 = new ArrayList<>();
        Number[] t2 = new Number[100];

        Class c= tt(t1, t2).getClass();

        System.out.println("返回类型："+c.getName());
        System.out.println("返回类型的父类："+c.getSuperclass().getName());


    }
}

