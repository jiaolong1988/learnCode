package designPatterns.Prototype;

/**
 * @author jiaolong
 * @date 2024/01/16 15:31
 * @description: 原型模式
 * 克隆对象 必须实现Cloneable接口，且实现 Object类的clone方法。
 *
 * 输出结果：
 * 获取元对象与克隆对象的信息：
 * pro1:chenglong
 * pro2:chenglong
 *
 * 设置克隆对象的信息，与原对象进行比较：
 * pro1:chenglong
 * pro2:jiaolong
 */
public class PrototypePatternTest {
    public static void main(String[] args) throws CloneNotSupportedException {

        ConcretePrototype pro1 = new ConcretePrototype();
        pro1.setName("chenglong");
        ConcretePrototype pro2 = (ConcretePrototype) pro1.clone();

        System.out.println("获取元对象与克隆对象的信息：");
        System.out.println("pro1:"+pro1.getName());
        System.out.println("pro2:"+pro2.getName());

        System.out.println("\n设置克隆对象的信息，与原对象进行比较：");
        pro2.setName("jiaolong");
        System.out.println("pro1:"+pro1.getName());
        System.out.println("pro2:"+pro2.getName());

    }
}

/**
 * @author jiaolong
 * @date 2024-1-17 15:54
 * 克隆对象必须实现Cloneable接口，且实现 Object类的clone方法
 */
class Prototype implements Cloneable {
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}


class ConcretePrototype extends Prototype {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}


