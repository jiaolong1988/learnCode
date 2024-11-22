package designPatterns.Decorator;

/**
 * @author jiaolong
 * @date 2024-1-10 10:21
 * 未使用装饰模式实现功能扩展-基础版本
 */
public class NoDecoratorTest {


	public static void main(String[] args) {
		Person xc = new Person("小菜");
        xc.Show();

		System.out.println("第一种装扮：");
		xc.WearTShirts();
		xc.WearBigTrouser();
		xc.WearSneakers();
		xc.Show();

		System.out.println("\n第二种装扮：");
		xc.WearSuit();
		xc.WearTie();
		xc.WearLeatherShoes();
		xc.Show();
	}
}

class Person {
    private String name;
    public Person(String name)
    {
        this.name = name;
    }
 
    public void WearTShirts()
    {
       System.out.println("大T恤 ");
    }
 
    public void WearBigTrouser()
    {
       System.out.println("垮裤 ");
    }
 
    public void WearSneakers()
    {
       System.out.println("破球鞋 ");
    }
 
    public void WearSuit()
    {
       System.out.println("西装 ");
    }
 
    public void WearTie()
    {
       System.out.println("领带 ");
    }
 
    public void WearLeatherShoes()
    {
       System.out.println("皮鞋 ");
    }
 
    public void Show()
    {
        System.out.println(String.format("装扮的%1$s", name));
    }
}