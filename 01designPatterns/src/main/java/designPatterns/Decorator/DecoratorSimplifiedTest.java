package designPatterns.Decorator;

/**
 * @author jiaolong
 * @date 2024-1-10 10:18
 * 装饰模式的简化写法-将Component与ConcreateComponent合并的写法
 */
public class DecoratorSimplifiedTest {

	public static void main(String[] args) {
		//基础功能
		PersonN person = new PersonN("jiaolong");
		
		//扩展功能
		Finery tShirts = new TShirts();
		Finery trouser = new BigTrouser(); 
		Finery sneakers = new Sneakers();


		//在person的基础上扩展功能
		trouser.decorate(person);
		//在trouser的基础上扩展功能
		sneakers.decorate(trouser);
		//在sneakers的基础上扩展功能
		tShirts.decorate(sneakers);

		tShirts.show();
	}

}

class PersonN {
	private String name;

	public PersonN() {
	}

	public PersonN(String name) {
		this.name = name;
	}

	public void show() {
		System.out.format("装扮的%1$s \n", name);
	}
}

class Finery extends PersonN {

	protected PersonN personN;

	public void decorate(PersonN personN) {
		this.personN = personN;
	}

	@Override
	public void show() {
		//必须添加判断，否则 子类对象直接调用该方法会报错，因为没有设置 装饰对象
		if (personN != null){
			personN.show();
		}

	}
}

class TShirts extends Finery {

	@Override
	public void show() {
		super.show();
		System.out.println("大T恤 ");
		
	}

}

class BigTrouser extends Finery {

	@Override
	public void show() {
		super.show();
		System.out.println("垮裤 ");
		
	}

}

class Sneakers extends Finery {

	@Override
	public void show() {
		super.show();
		System.out.println("破球鞋 ");	
		
	}

}
