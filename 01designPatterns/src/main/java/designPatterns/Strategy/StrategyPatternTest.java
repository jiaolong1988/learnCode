package designPatterns.Strategy;

/**
 * @author jiaolong
 * @date 2024/01/11 16:44
 * @description: 策略模式
 */
public class StrategyPatternTest {
    public static void main(String[] args) {

        //1.标准策略模式
//        Context contextA = new Context(new ConcreteStrategyA());
//        contextA.contextInterface();

        //2.策略模式与简单工厂模式结合
        Context context = new Context("A");
        context.contextInterface();

        //3.简单工厂模式
        Strategy strategy = SimpleFactory.createStrategy("A");
        strategy.algorithmInterface();

        /*
         * 简单工厂模式我需要让客户端认识两个类， Strategy和SimpleFactory，
         * 而策略模式与简单工厂结合的用法，客户端就只需要认识一个类Context就可以了。耦合更加降低。”
         */

    }
}

interface Strategy{
    public void algorithmInterface();
}

class ConcreteStrategyA implements Strategy{

    @Override
    public void algorithmInterface() {
        System.out.println("算法A");
    }
}
class ConcreteStrategyB implements Strategy{

    @Override
    public void algorithmInterface() {
        System.out.println("算法B");
    }
}

/**
 * @author jiaolong
 * @date 2024-1-11 17:00
 * 策略模式
 */
class Context{
    private Strategy strategy;

    //策略模式与简单工厂模式结合 或者用集合的方式取代if语句
    Context(String type){
        if(type.equals("A")){
            this.strategy = new ConcreteStrategyA();
        }
        if(type.equals("B")){
            this.strategy = new ConcreteStrategyB();
        }
    }

    //标准策略模式
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
 * 简单工程模式
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