package reflect.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface People {
	public People work(String workName);

	public String time();
}

class Student implements People {

	@Override
	public People work(String workName) {
		System.out.println("工作内容是" + workName);
		return this;
	}

	@Override
	public String time() {
		return "2018-06-12";
	}
}

class WorkHandler implements InvocationHandler {
	// 被代理对象
	private Object obj;

	public WorkHandler(Object obj) {
		this.obj = obj;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("	before 动态代理...");
		System.out.println("	代理对象名称：" + proxy.getClass().getName());
		System.out.println("	被代理对象名称" + this.obj.getClass().getName());
		if (method.getName().equals("work")) {
			method.invoke(this.obj, args);
			System.out.println("	work() after 动态代理...");
			//this代表的是InvocationHandler接口实现类本身，并不是真实的代理对象。
			return proxy;
		} else {
			System.out.println("	time() after 动态代理...");
			return method.invoke(this.obj, args);
		}
	}

}

public class ProxyOfProxyObjectUseWay {

	public static void main(String[] args) {
		/**
		 * 本案例主要用于invoke(Object proxy, Method method, Object[] args)中 proxy对象如何使用
		 * 参看：https://www.jianshu.com/p/f4159d53a0c0
		 */
		People people = new Student();
		InvocationHandler handler = new WorkHandler(people);

		People proxy = (People) Proxy.newProxyInstance(
				people.getClass().getClassLoader(),
				people.getClass().getInterfaces(), 
				handler);
		People p = proxy.work("写代码").work("开会").work("上课");

		System.out.println("-----------------打印返回的对象" + p.getClass());
		System.out.println("");

		String time = proxy.time();
		System.out.println(time);
	}

}
