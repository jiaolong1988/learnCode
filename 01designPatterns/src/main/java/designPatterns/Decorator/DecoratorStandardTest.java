package designPatterns.Decorator;

/**
 * @author jiaolong
 * @date 2024-1-10 10:18
 * @version 1.0
 *
 * 装饰模式的标准写法
 */
public class DecoratorStandardTest {
		
	public static void main(String[] args) {

		//基础功能
		Component cc = new ConcreteComponent("jiaolong");

		//扩展功能
		Decorator tshirt = new TShirtsA();
		Decorator trouse = new BigTrouserA();
		Decorator sneak = new SneakersA();

		//必须先扩展component基础功能，因为都是在此功能上进行扩展的
		tshirt.setComponent(cc);
		trouse.setComponent(tshirt);
		sneak.setComponent(trouse);
		
		sneak.show();
	}

}

interface Component{
	void show();
}


class ConcreteComponent implements Component {
	private String name;

	public ConcreteComponent(String name) {
		this.name = name;
	}

	@Override
	public void show() {
		System.out.format("装扮的%1$s \n", name);
	}

}

abstract class Decorator implements Component{

	protected Component component;
	
	@Override
	public void show() {
		//必须添加判断，否则 子类对象直接调用该方法会报错，因为没有设置 装饰对象
		if (component != null){
			component.show();
		}

	}
	
	public void setComponent(Component component) {
		this.component = component;
	}
}



class TShirtsA extends Decorator {

	@Override
	public void show() {
		super.show();
		System.out.println("大T恤 ");
		
	}

}

class BigTrouserA extends Decorator {

	@Override
	public void show() {
		super.show();
		System.out.println("垮裤 ");
		
	}

}

class SneakersA extends Decorator {

	@Override
	public void show() {
		super.show();
		System.out.println("破球鞋 ");	
		
	}

}
