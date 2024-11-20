package reflect.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface Person {
	String walk();
	void sayHello(String name);
}

class ChinaPerson implements Person{
	@Override
	public String walk() {
		System.out.println("china walk!");
		return "jiaolong";
		
	}
	@Override
	public void sayHello(String name) {
		System.out.println("china say hello! ");	
	}
}

//继承写法
class BaseInvokationHandler implements InvocationHandler {
	/*
	 * proxy：	已创建的代理对象
	 * method：	代表正在执行的方法
	 * args：	正在执行方法的参数。
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
		System.out.print("----正在执行的方法:" + method);
		if(args != null) 
			System.out.print("  参数:" + args[0].toString()+"\n");
		else
			System.out.print(" 无参数: \n" );
			
		//需要被代理的对象
		ChinaPerson cp = new ChinaPerson();
		Object result = method.invoke(cp, args);	
		
		return result;
	}
}

//终极写法
class AopInvokationHandler implements InvocationHandler {
	//需要被代理的对象
    private Object target = null;
    
    AopInvokationHandler(){}
    AopInvokationHandler(Object target) {
        this.target = target;
    }

    public Object bind(Object target) {
        this.target = target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), 	  //被代理对象的类加载器
        							  target.getClass().getInterfaces(),	  //被代理对象的接口信息
        							  this);								  //InvocationHandler实现类
    }

	/*
	 * proxy：	已创建的代理对象
	 * method：	代表正在执行的方法
	 * args：	正在执行方法的参数。
	 */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("代理前...");
        Object obj = method.invoke(target, args);
        System.out.println(obj==null?"没有返回值" :"返回值"+(String)obj);
        System.out.println("代理后...");
        
        //设置代理的返回结果
        return "jiao_val";
    }
}



public class ProxyBaseUse {
	/**
	 * 基础用法
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public static void baseUseWay() throws Exception {
				
		
//写法1-创建代理对象的过程		
		
//		  //获取person的代理class 注意：第二个参数一定要写接口类 
//		  Class<?> proxyClass = Proxy.getProxyClass(Person.class.getClassLoader(), Person.class);
//		  //设置代理类的构造方法 
//		  Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class); 
//		  //创建一个动态代理对象 
//		  Person p = (Person) constructor.newInstance(handler);
		 

//写法2-创建代理对象(写法1的简写)				
		//代理对象的业务处理逻辑
		InvocationHandler handler = new BaseInvokationHandler();
		
		//类加载器-接口
		ClassLoader personInterfaceLoader = Person.class.getClassLoader();
		//类加载器-接口实现类
		ClassLoader interfaceImplClassLoader = ChinaPerson.class.getClassLoader();
		//类加载器-接口实现类的实例
		ClassLoader interfaceImplClassObjectLoader = new ChinaPerson().getClass().getClassLoader();
		
		//使用指定的InvocationHandler来生成一个动态代理对象
		Person p = (Person)Proxy.newProxyInstance(interfaceImplClassObjectLoader,  	 	//类加载器-使用接口的类加载器即可
											    //new Class[]{Person.class},     		//接口
												  ChinaPerson.class.getInterfaces(),
												  handler);						 	    //业务方法
		
		//通过代理对象条用walk()和sayHello()方法
		String val = p.walk();
		System.out.println("===返回值"+val);
		
		System.out.println("");
		p.sayHello("孙悟空");
	}
	
	/**
	 * 动态代理AOP的实现
	 */
	public static void aopUseWay() {
		AopInvokationHandler dpih = new AopInvokationHandler();
		Person p = (Person)dpih.bind(new ChinaPerson());
		
		System.out.println("执行walk方法的返回结果："+p.walk());
		System.out.println("");
		p.sayHello("aop");
		
	}
	
	public static void main(String[] args) throws Exception {
		/**
		 * 动态代理的基本使用与AOP的实现案例
		 */
		baseUseWay();
		System.out.println(" \r\n----------------- \r\n");
		aopUseWay();
	}

}
