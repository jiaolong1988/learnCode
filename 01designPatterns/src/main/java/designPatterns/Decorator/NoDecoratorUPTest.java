package designPatterns.Decorator;


/**
 * @author jiaolong
 * @date 2024-1-10 15:02
 * 未使用装饰模式实现功能扩展-改良版
 */
public class NoDecoratorUPTest {
    public static void main(String[] args) {
        Person person = new Person("jiaolong");
        FineryX tshirt = new TShirtsX();
        FineryX bigTrouser = new WearBigTrouserX();

        person.Show();
        tshirt.show();
        bigTrouser.show();
    }

}

class PersonX {
    private String name;
    public PersonX(String name)
    {
        this.name = name;
    }

    public void show()
    {
        System.out.println(String.format("装扮的%1$s", name));
    }
}

abstract class FineryX {
    public abstract void show();
}


class TShirtsX extends FineryX {
    @Override
    public void show() {
        System.out.println("大T恤 ");
    }
}

class WearBigTrouserX extends FineryX {
    @Override
    public void show() {
        System.out.println("垮裤 ");
    }
}