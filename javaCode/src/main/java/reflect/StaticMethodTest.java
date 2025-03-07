package reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 方法与字段的反射测试
 * @author jiaolong
 * @date 2024-11-20 12:00
 */
public class StaticMethodTest {
	public static void main(String[] args) throws Exception {
		fieldTest();
		System.out.println("====");
		methodUseTest();
	}

	public static void fieldTest() throws Exception {
		Field field = Mtest.class.getDeclaredField("name");
		System.out.println("泛型化名字:" + field.toGenericString());
		System.out.println("字段的名字:" + field.getName());
		System.out.println("字段的类型:" + field.getType());
		// 设置字段是否可以访问
		System.out.println("字段是否可以访问：" + field.isAccessible());
		field.setAccessible(true);
		System.out.println("字段是否可以访问：" + field.isAccessible());
		// 设置静态字段的值
		System.out.println("name字段的值：" + field.get("name"));
		field.set("name", "updateName");
		System.out.println("修改后name字段的值：" + field.get("name"));
		System.out.println("字段注解名字："+field.getAnnotation(Deprecated.class).annotationType().getName().toString());
		
	}

	public static void methodUseTest() throws Exception {
		//根据方法名称参数 获取 Mehtod对象
		Method method = Mtest.class.getDeclaredMethod("replace", String.class, List.class);
		// 调用静态方法
		method.invoke(null, "jiaolong", new ArrayList<String>());


		System.out.println("方法的名字：" + method.getName());
		System.out.println("方法的泛型描述：" + method.toGenericString());
		System.out.println("方法的return类型：" + method.getReturnType());
		System.out.println("方法的参数类型-Class格式：" + method.getParameterTypes()[0].getName());
		System.out.println("方法的注解：" + method.getAnnotation(Deprecated.class).toString());

		System.out.println("方法中参数的数量：" + method.getParameterCount());
		System.out.println("====");
		Parameter[] ps = method.getParameters();
		for (Parameter p : ps) {
			System.out.println("参数名子是否存在：" + p.isNamePresent());
			System.out.println("参数名字：" + p.getName());
			System.out.println("参数类型" + p.getType());
			System.out.println("参数的泛型：" + p.getParameterizedType());
			System.out.println("参数是否为可变参数：" + p.isVarArgs());
			System.out.println("====");
		}
	}
}

class Mtest {
	@Deprecated
	private static String name = "jiaolong";

	@Deprecated
	public static void replace(String str, List<String> list) {
		System.out.println("调用replace方法: " + str);
	}
}