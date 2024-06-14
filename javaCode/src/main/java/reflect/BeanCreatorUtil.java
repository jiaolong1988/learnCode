package reflect;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author jiaolong
 * @date 2024-2-1 11:24
 */
public class BeanCreatorUtil  {
	/**
	 * 使用无参的构造器创建bean实例, 不设置任何属性
	 * @param clazz
	 * @return
	 */
	public <T> T createBeanUseDefaultConstruct(Class<T> clazz) {
		try {
			return clazz.newInstance();
		}  catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 使用有参数的构造器创建bean实例, 不设置任何属性
	 * @param  clazz
	 * @param args 参数集合
	 * @return
	 */
	public <T> T createBeanUseDefineConstruce(Class<T> clazz, Object ... args) {
		Class<?>[] argsClass = getArgsClasses(args);
		try {
			Constructor<T> constructor = findConstructor(clazz, argsClass);
			return constructor.newInstance(args);
		}  catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("no such constructor " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * 得到参数的类型
	 * @param args
	 * @return
	 */
	private Class<?>[] getArgsClasses(Object ... args) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		for (Object arg : args) {
			result.add(getClass(arg));
		}
		Class<?>[] a = new Class[result.size()];
		return result.toArray(a);
	}
	
	/**
	 * 查找一个类的构造器, 一个类中构造器参数有可能是原来构造器类型的子类
	 * @param clazz
	 * @param argsClass
	 * @return
	 * @throws NoSuchMethodException
	 */
	private <T> Constructor<T> findConstructor(Class<T> clazz, Class<?>[] argsClass)throws NoSuchMethodException {
		Constructor<T> constructor = getConstructor(clazz, argsClass);
		if (constructor == null) {
			System.out.println("creating object of constructor method does immediate get");

			Constructor<?>[] constructors = clazz.getConstructors();
			for (Constructor<?> c : constructors) {
				//获取该类所有的构造器
				Class<?>[] constructorArgsCLass = c.getParameterTypes();
				//参数数量与构造器的参数数量相同
				if (constructorArgsCLass.length == argsClass.length) {
					if (isSameArgs(argsClass, constructorArgsCLass)) {
						return (Constructor<T>)c;
					}
				}
			}
		} else {
			return constructor;
		}
		throw new NoSuchMethodException("could not find any constructor");
	}
	
	
	/**
	 * 查找一个类的构造器
	 * @param clazz 类型
	 * @param argsClass 构造参数
	 * @return
	 */
	private <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>[] argsClass) {
		try {
			Constructor<T> constructor = clazz.getConstructor(argsClass);
			return constructor;
		} catch (NoSuchMethodException e) {
			return null;
		}
	}
	
	/**
	 * 判断两个参数数组类型是否匹配
	 * @param argsClass
	 * @param constructorArgsCLass
	 * @return
	 */
	private boolean isSameArgs(Class<?>[] argsClass, Class<?>[] constructorArgsCLass) {
		for (int i = 0; i < argsClass.length; i++) {
			try {
				//将参数类型与构造器中的参数类型进行强制转换
				argsClass[i].asSubclass(constructorArgsCLass[i]);
				//循环到最后一个都没有出错, 表示该构造器合适
				if (i == (argsClass.length - 1)) {
					return true;
				}
			} catch (Exception e) {
				//有一个参数类型不符合, 跳出该循环
				break;
			}
		}
		return false;
	}
	
	
	/**
	 * 获得一个Object的class
	 * @param obj
	 * @return
	 */
	private Class<?> getClass(Object obj) {
		if (obj instanceof Integer) {
			return Integer.TYPE;
		} else if (obj instanceof Boolean) {
			return Boolean.TYPE;
		} else if (obj instanceof Long) {
			return Long.TYPE;
		} else if (obj instanceof Short) {
			return Short.TYPE;
		} else if (obj instanceof Double) {
			return Double.TYPE;
		} else if (obj instanceof Float) {
			return Float.TYPE;
		} else if (obj instanceof Character) {
			return Character.TYPE;
		} else if (obj instanceof Byte) {
			return Byte.TYPE;
		}
		return obj.getClass();
	}

}
