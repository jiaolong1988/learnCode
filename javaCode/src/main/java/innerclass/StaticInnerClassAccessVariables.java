package innerclass;

/**
 * 静态内部类: 内部类访问外部变量， 外部类访问内部变量。
 *
 * @author jiaolong
 * @date 2024/12/31 13:50
 **/
public class StaticInnerClassAccessVariables {
    private int prop1 = 5;
    private static int prop2 = 9;

/*1.   静态内部类-内部类访问外部类变量*/
    static class StaticInnerClass1 {
        // 静态内部类里可以包含静态成员
        private static int age;

        public void accessOuterProp() {
            /* 下面代码出现错误：
               静态内部类无法访问外部类的实例变量
               System.out.println(prop1);
            */
            System.out.println(prop2);
        }
    }

/*2.   静态内部类-外部类访问内部类变量*/
    static class StaticInnerClass2 {
        private static int prop1 = 5;
        private int prop2 = 9;
    }

    public void accessInnerProp() {
        // System.out.println(prop1);
        // 上面代码出现错误，应改为如下形式：
        // 通过类名访问静态内部类的类成员
        System.out.println(StaticInnerClass2.prop1);
        // System.out.println(prop2);
        // 上面代码出现错误，应改为如下形式：
        // 通过实例访问静态内部类的实例成员
        System.out.println(new StaticInnerClass2().prop2);
    }
}

