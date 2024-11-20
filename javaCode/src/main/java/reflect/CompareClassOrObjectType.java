package reflect;

/**
 * 判断实例的类型，判断类的子类，判断Integer.type与Integer.class的区别
 * @author jiaolong
 * @date 2022-10-06 10:27:12
 */
public class CompareClassOrObjectType {

	
	public static void main(String[] args) {
							
			/**
			  	1. obj instanceof 类名
				用来判断一个对象实例 是否是 【一个类或接口的或其子类子接口】 的实例
			*/	
			System.out.println("String对象为Object类型的实例："+("" instanceof Object));				
			System.out.println("obj对象是String类型的实例："+(new Object() instanceof String));					
			System.out.println("================================");
			
			/**
				2. class1.isAssignableFrom(class2)
			 	判断 class2 是不是 class1 的子类或者子接口	
			 */		
			System.out.println("String类是否是Object的子类-true："+ Object.class.isAssignableFrom(String.class));
			System.out.println("Object类是否是String的子类-false："+String.class.isAssignableFrom(Object.class));
			
			System.out.println("================================");
			
			/**
			 	3. asSubclass
					public <U> Class<? extends U> asSubclass(Class<U> clazz) {
				    	if (clazz.isAssignableFrom(this))
					        return (Class<? extends U>) this;
					    else
					        throw new ClassCastException(this.toString());
					}
		        作用是将调用这个方法的class对象转换成由clazz参数所表示的 class 对象的某个子类。
		    */
			System.out.println("String是Object的子类，获取Object的子类信息Class："+String.class.asSubclass(Object.class));
			//2.Object不是是String的子类,无法获取String的子类信息，并抛出异常：
			//Class<?> c2 = Object.class.asSubclass(String.class);

			
			/**
			 * 4.Integer.class 与int.class
			 */
			System.out.println("================================");
			Class<?> integerClass = Integer.class;
			System.out.println("Integer的class对象:"+integerClass);
			Class<?> intClass = int.class;
			System.out.println("int的class对象:"+intClass);
			Class<Integer> it = Integer.TYPE;
			System.out.println("Integer的原始类型对应的Class对象："+it);
		
	}

}
