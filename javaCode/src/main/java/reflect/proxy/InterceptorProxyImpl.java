package reflect.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//拦截器规则
interface Interceptor {
	public boolean before(Object proxy, Object target, Method method, Object[] args);

	public void around(Object proxy, Object target, Method method, Object[] args);

	public void after(Object proxy, Object target, Method method, Object[] args);
}

//被代理对象
interface Shopping {
	void learn();
	void play();
}

class Client implements Shopping {
	public void learn() {
		System.out.println("我想买这件商品");
	}
	public void play() {
		System.out.println("我想去旅行");
	}
}

class MyInterceptor implements Interceptor {

	/**
	 * proxy 	创建的代理对象
	 * target 	被代理的对象
	 * method	执行的方法
	 * args 	执行的方法参数
	 */
	@Override
	public boolean before(Object proxy, Object target, Method method, Object[] args) {
		System.out.println("before");
		
		if(method.getName().equals("learn")) {
			System.out.println("learn method 可以执行");
			return true;
		}else {
			System.out.println("play method 不可以执行");
			return false;
		}
	}

	@Override
	public void around(Object proxy, Object target, Method method, Object[] args) {
		System.out.println("around");
	}

	@Override
	public void after(Object proxy, Object target, Method method, Object[] args) {
		System.out.println("after");
	}
}



//代理对象处理规则
class InterceptorProxy implements InvocationHandler {
	//被代理的对象
	private Object target = null;
	//具有拦截规则的的对象
	Interceptor interceptor = null;

	InterceptorProxy(Interceptor interceptor) {
		this.interceptor = interceptor;
	}

	public Object bind(Object target) {
		this.target = target;
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
	}

	/**
	 * 代理方法逻辑
	 *
	 * @param proxy  代理对象
	 * @param method 调度方法
	 * @param args   调度方法参数
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (interceptor == null) {
			method.invoke(target, args);
		}
		Object result = null;
		if (interceptor.before(proxy, target, method, args)) {
			result = method.invoke(target, args);
		} else {
			interceptor.around(proxy, target, method, args);
		}
		interceptor.after(proxy, target, method, args);
		return result;
	}
}


/**
 * 动态代理实现拦截器
 */
public class InterceptorProxyImpl {
	public static void main(String[] args) {
		/**
		 * 使用动态代理实现拦截器
		 */
		
		//设置去拦截器规则
		InterceptorProxy interceptor = new InterceptorProxy(new MyInterceptor());
		//设置被代理的对象，并返回代理对象
		Shopping shop = (Shopping) interceptor.bind(new Client());
		System.out.println("....执行play方法");
		shop.play();
		
		System.out.println("\n....执行learn方法");
		shop.learn();
	}
}
