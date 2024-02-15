package designPatterns.Template;

/**
 * @author jiaolong
 * @date 2024/01/19 14:26
 * @description: ģ�巽��ģʽ
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
        System.out.println("ִ�н���������");
    }
}
//������
class ConcreteClassA extends AbstractClass{
    @Override
    public void primitiveOperation1() {
        System.out.println("������A����1ʵ��");
    }

    @Override
    public void primitiveOperation2() {
        System.out.println("������A����2ʵ��");
    }
}

class ConcreteClassB extends AbstractClass{
    @Override
    public void primitiveOperation1() {
        System.out.println("������B����1ʵ��");
    }

    @Override
    public void primitiveOperation2() {
        System.out.println("������B����2ʵ��");
    }
}