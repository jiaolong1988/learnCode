package reflect.util.test;

public class TestBean {

	private String name;
	private int age;
	//boolean类型变量不能已is开头进行命名。eg: isMale 这是错误的命名。
	private boolean male;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

	public boolean isMale() {
		return male;
	}

	public void setMale(boolean male) {
		this.male = male;
	}
}
