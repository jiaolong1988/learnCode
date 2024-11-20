package reflect;

import javax.xml.transform.Source;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.lang.annotation.*;

public class ClassTest {

	public static void main(String[] args) throws Exception {
		// 下面代码可以获取ClassTest对应的Class
		Class<DefinedTestClass> clazz = DefinedTestClass.class;
		
		// 获取该Class对象所对应类的全部构造器
//		Constructor<?>[] ctors = clazz.getDeclaredConstructors();
//		System.out.println("ClassTest的全部构造器如下：");
//		for (Constructor<?> c : ctors) {
//			System.out.println(c);
//		}
//		// 获取该Class对象所对应类的全部public构造器
//		Constructor<?>[] publicCtors = clazz.getConstructors();
//		System.out.println("ClassTest的全部public构造器如下：");
//		for (Constructor<?> c : publicCtors) {
//			System.out.println(c);
//		}
		
		
//		// 获取该Class对象所对应类的全部public方法
//		Method[] mtds = clazz.getMethods();
//		System.out.println("ClassTest的全部public方法如下：");
//		for (Method md : mtds) {
//			System.out.println(md);
//		}

//		// 获取该Class对象所对应类的指定方法
//		Method m = clazz.getMethod("info", String.class);
//		System.out.println("DefinedTestClass里带一个字符串参数的info()方法为：" + m);
	
		// 获取该Class对象所对应类的上的全部注解
		Annotation[] anns = clazz.getAnnotations();
		//System.out.println("ClassTest的全部Annotation如下：");
		for (Annotation an : anns) {

			//方式一、Annos.class是一个对象
//			if(Annos.class.isInstance(an)) {
//				System.out.println(an.annotationType());
//				Annos annos = (Annos)an;
//				System.out.println("0===="+annos.value()[0].aa());
//			}
			//方式二、Annos是一个接口
//			if(an instanceof  Annos) {
//				System.out.println(an.annotationType());
//				Annos annos = (Annos)an;
//				System.out.println("1===="+annos.value()[0].aa());
//			}
			try {
				//对注解进行类型转换
				System.out.println(""+an.annotationType());
				Annos annos = (Annos)an;
				//获取注解的值
				for(Anno a: annos.value()){
					System.out.println("2===="+a.aa());
				}
			}catch(Exception e) {
			//	e.printStackTrace();
			}
		}
		
				
//		System.out.println("该Class元素上的@SuppressWarnings注解为：" + Arrays.toString(clazz.getAnnotationsByType(SuppressWarnings.class)));
//		System.out.println("该Class元素上的@Anno注解为：" + Arrays.toString(clazz.getAnnotationsByType(Anno.class)));

		// 获取该Class对象所对应类的全部内部类
//		Class<?>[] inners = clazz.getDeclaredClasses();
//		System.out.println("ClassTest的全部内部类如下：");
//		for (Class<?> c : inners) {
//			System.out.println(c);
//		}

//		// 使用Class.forName方法加载ClassTest的Inner内部类
//		Class<?> inClazz = Class.forName("com.founder.ClassTest$Inner");		
//		// 通过getDeclaringClass()访问该类所在的外部类
//		System.out.println("inClazz对应类的外部类为：" + inClazz.getDeclaringClass());
//		System.out.println("ClassTest的包为：" + clazz.getPackage());
//		System.out.println("ClassTest的父类为：" + clazz.getSuperclass());

		//判断类是否继承于某个类
//		//Object转化为String,既Object类是否继承于一个String类
//		System.out.println(String.class.isAssignableFrom(Object.class));
//		//String转化为Objct,既String类是否继承于一个Object类
//		System.out.println(Object.class.isAssignableFrom(String.class));
	}
}


//1.定义可重复使用的注解
@Repeatable(Annos.class)
@interface Anno {
	String aa() default "jiaolong";
}

@Retention(value = RetentionPolicy.RUNTIME)
@interface Annos {
	Anno[] value();
}

// 2.使用4个注解修饰该类
@SuppressWarnings(value = "unchecked")
@Deprecated //表示此类作废
// 使用重复注解修饰该类
@Annos({@Anno(aa="xxx"),@Anno(aa="yyy")})
class DefinedTestClass{
	// 为该类定义一个私有的构造器
	private DefinedTestClass() {
	}

	// 定义一个有参数的构造器
	public DefinedTestClass(String name) {
		System.out.println("执行有参数的构造器");
	}

	// 定义一个无参数的info方法
	public void info() {
		System.out.println("执行无参数的info方法");
	}

	// 定义一个有参数的info方法
	public void info(String str) {
		System.out.println("执行有参数的info方法" + "，其str参数值：" + str);
	}

	private void privateMethod() {

	}
	// 定义一个测试用的内部类
	private class Inner {
	}
}