package generics;

/**
 * 泛型如何在类 接口 子类中的定义
 * @author jiaolong
 * @date 2025/02/10 13:51
 **/
public class GenericDefinedTest {
    public static void main(String[] args) {

        //1.定义泛型类
        Apple<String> a1 = new Apple<>("苹果");
        Apple<Double> a2 = new Apple<>(5.67);
        System.out.println(a1.getInfo());
        System.out.println(a2.getInfo()+"\r\n");

        //2.定义泛型子类
        AppleChild<String> ac = new AppleChild<>();
        ac.setB("jiaolong");
        System.out.println(ac.getB());

        //3.定义泛型接口
        AppleInterfaceImpl impl = new AppleInterfaceImpl();
        System.out.println(impl.test("chenglong"));
    }
}

class Apple<T> {
    public Apple() {}
    //泛型变量(不能定义static 泛型变量)
    private T info;
    //泛型构造方法
    public Apple(T info) {
        this.info = info;
    }
    //泛型参数
    public void setInfo(T info) {
        this.info = info;
    }
    //泛型返回值
    public T getInfo() {
        return this.info;
    }
    // 下面代码错误，不能在静态方法中使用泛型参数
    //public static void bar(T msg){}
    //下面代码错误，静态变量不能声明泛型
    //static T info;
}

//子类继承泛型父类时，必须指定父类的泛型类型，子类也可定义泛型
class AppleChild<T> extends Apple<String> {
    T b;
    //重写父类方法
    public String getInfo() {
        return super.getInfo();
    }

    public void setB(T b) {
        this.b= b;
    }
    public T getB() {
        return this.b;
    }
}

//泛型接口
interface AppleInterface<T>{
    //定义泛型方法
    public T test(T info);
}
class AppleInterfaceImpl implements AppleInterface<String>{

    @Override
    public String test(String info) {
        return info;
    }

}