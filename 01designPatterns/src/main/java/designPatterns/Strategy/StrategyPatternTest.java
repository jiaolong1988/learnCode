package designPatterns.Strategy;

/**
 * @author jiaolong
 * @date 2024/01/11 16:44
 * @description: ����ģʽ
 */
public class StrategyPatternTest {
    public static void main(String[] args) {

        //1.��׼����ģʽ
//        Context contextA = new Context(new ConcreteStrategyA());
//        contextA.contextInterface();

        //2.����ģʽ��򵥹���ģʽ���
        Context context = new Context("A");
        context.contextInterface();

        //3.�򵥹���ģʽ
        Strategy strategy = SimpleFactory.createStrategy("A");
        strategy.algorithmInterface();

        /*
         * �򵥹���ģʽ����Ҫ�ÿͻ�����ʶ�����࣬ Strategy��SimpleFactory��
         * ������ģʽ��򵥹�����ϵ��÷����ͻ��˾�ֻ��Ҫ��ʶһ����Context�Ϳ����ˡ���ϸ��ӽ��͡���
         */

    }
}

interface Strategy{
    public void algorithmInterface();
}

class ConcreteStrategyA implements Strategy{

    @Override
    public void algorithmInterface() {
        System.out.println("�㷨A");
    }
}
class ConcreteStrategyB implements Strategy{

    @Override
    public void algorithmInterface() {
        System.out.println("�㷨B");
    }
}

/**
 * @author jiaolong
 * @date 2024-1-11 17:00
 * ����ģʽ
 */
class Context{
    private Strategy strategy;

    //����ģʽ��򵥹���ģʽ��� �����ü��ϵķ�ʽȡ��if���
    Context(String type){
        if(type.equals("A")){
            this.strategy = new ConcreteStrategyA();
        }
        if(type.equals("B")){
            this.strategy = new ConcreteStrategyB();
        }
    }

    //��׼����ģʽ
//    Context(Strategy strategy){
//        this.strategy = strategy;
//    }

    public void contextInterface(){
        this.strategy.algorithmInterface();
    }
}

/**
 * @author jiaolong
 * @date 2024-1-11 17:06
 * �򵥹���ģʽ
 */
class SimpleFactory{

    public static Strategy createStrategy(String type){
        Strategy strategy = null;
        switch (type){
            case "A":
                strategy = new ConcreteStrategyA();
                break;
            case "B":
                strategy = new ConcreteStrategyB();
                break;
        }

        return strategy;
    }

}