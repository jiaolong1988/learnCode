package designPatterns.Template;

/**
 * @author jiaolong
 * @date 2024/01/19 14:26
 * @description: 模板方法模式
 */
public class TemplageMethodPattern {
    public static void main(String[] args) {
        AbstractClass c =  null;
        c = new ConcreteClassA();
        c.templateMethod();

        c = new ConcreteClassB();
        c.templateMethod();
    }
}

abstract class AbstractClass{
    public abstract void primitiveOperation1();
    public abstract void primitiveOperation2();

    public void templateMethod(){
        primitiveOperation1();
        primitiveOperation2();
        System.out.println("执行结束。。。");
    }
}
//具体类
class ConcreteClassA extends AbstractClass{
    @Override
    public void primitiveOperation1() {
        System.out.println("具体类A方法1实现");
    }

    @Override
    public void primitiveOperation2() {
        System.out.println("具体类A方法2实现");
    }
}

class ConcreteClassB extends AbstractClass{
    @Override
    public void primitiveOperation1() {
        System.out.println("具体类B方法1实现");
    }

    @Override
    public void primitiveOperation2() {
        System.out.println("具体类B方法2实现");
    }
}