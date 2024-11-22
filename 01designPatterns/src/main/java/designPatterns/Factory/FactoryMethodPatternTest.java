package designPatterns.Factory;

/**
 * @author jiaolong
 * @date 2024/01/11 15:21
 * @description: TODO
 */
public class FactoryMethodPatternTest {

    public static void main(String[] args) {

        /*
         * 根据指 定的工厂实现类 找出 具体的实现类。
         *
         */
        IFactory addFactory = new AddFactory();
        Operation addOperation = addFactory.crateOperation();
        double resAdd = addOperation.getResult(1,2);
        System.out.println(resAdd);


        IFactory subFactory  = new SubFactory();
        Operation subOperation = subFactory.crateOperation();
        double resSub = subOperation.getResult(1,2);
        System.out.println(resSub);

    }
}

interface Operation {
    public double getResult(double numberA, double numberB);
}

class OperationAdd implements Operation{
    @Override
    public double getResult(double numberA, double numberB) {
        return numberA+numberB;
    }
}

class OperationSub implements Operation{
    @Override
    public double getResult(double numberA, double numberB) {
        return numberA-numberB;
    }
}


interface IFactory{
    public Operation crateOperation();
}

class AddFactory implements IFactory{
    @Override
    public Operation crateOperation() {
        return new OperationAdd();
    }
}

class SubFactory implements IFactory{
    @Override
    public Operation crateOperation() {
        return new OperationSub();
    }
}