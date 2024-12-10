package lambda.baseuse;

/**
 * 1-基本语法
 * lambda 语法：
 *      (Parameters) -> { Body }
 *
 * lambda表达式没有名称,因为它是匿名内部类,返回类型由编译器推断。
 * 1.参数类型可以省略
 * 2.单参数可省略括号
 * 3.无参数不可省略括号
 */
public class LambdaSyntax1 {
    public static void main(String[] args) {

        //1-接口无返回类型,必须以System.out.println()结尾
        MyVoidInfo myVoidInfo = (x, y) -> { System.out.println("无返回类型："+ Integer.valueOf(x+y)); };
        myVoidInfo.calcVoid(10, 20);

        //1.1-接口有返回类型,{}中必须以return结尾
        MyIntegerInfo myIntegerInfo2 = (x, y) -> {return x*2 +y; } ;
        int result = myIntegerInfo2.calcIt(2, 3);
        System.out.println("有返回类型：" + result);

        //1.2-接口有返回类型，单行可简写，不用添加 return关键字
        MyIntegerInfo myIntegerInfo3 = (x, y) ->  x*2 +y ;
        result = myIntegerInfo3.calcIt(2, 3);
        System.out.println("有返回类型：" + result);


        //2-lambda表达式作为参数进行传递
        engine((x, y) -> x*10+y);
        //2.1-方法重载，lambda作为变量进行传递
        MyVoidInfo voidInfo = (x, y) -> System.out.println("方法重载1："+Integer.valueOf(x+y));
        engine(voidInfo);
        //2,2-方法重载时，指定lambda类型
        engine((MyVoidInfo) (x, y) -> System.out.println("方法重载2："+Integer.valueOf(x+y)));


        //3-方法中返回lambad表达式
        result = create1().calcIt(3, 1);
        System.out.println("\n方法中返回lambad表达式1:"+result);
        create2().calcVoid(3, 1);
    }

    /**
     * 方法重载
     * @param integerInfo
     */
    private static void engine(MyIntegerInfo integerInfo){
        int x=2, y=3;
        int result = integerInfo.calcIt(x, y);
        System.out.println("\nlambda作为参数传递: "+result);
    }
    private static void engine(MyVoidInfo voidInfo){
        int x=10, y=20;
       voidInfo.calcVoid(x, y);
    }


    /**
     * 方法返回 lambda表达式
     */
    private static MyIntegerInfo create1(){
        //lambda表达式返回的是：有返回类型的接口函数
        return (x,y) -> x/y;
    }
    private static MyVoidInfo create2(){
        //lambda表达式返回的是：无返回类型的接口函数
        return (x,y) -> System.out.println("方法中返回lambad表达式2:"+x/y);
    }
}

/**
 * @FunctionalInterface:是只有一个方法的函数式接口，用作lambda表达式的类型，也可以不使用。
 **/
@FunctionalInterface
interface MyVoidInfo{
    void calcVoid(Integer x, Integer y);
}


interface MyIntegerInfo{
    Integer calcIt(Integer x, Integer y);
}
