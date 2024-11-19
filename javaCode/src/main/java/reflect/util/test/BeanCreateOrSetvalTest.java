package reflect.util.test;

import reflect.util.BeanCreatorUtil;
import reflect.util.BeanObjectSetValueUtil;

import java.util.HashMap;
import java.util.Map;

public class BeanCreateOrSetvalTest {

	public static void main(String[] args) {
		//java bean 中boolean类型变量不能已is开头进行命名。eg: isMale 这是错误的命名。
		Map<String,Object> params = new HashMap<>();
		params.put("age", 10);
		params.put("name", "zaibiechu");
		params.put("male", true);

		TestBean beanObject =  new BeanCreatorUtil().createBeanUseDefaultConstruct(TestBean.class);

		BeanObjectSetValueUtil setBeanValue = new BeanObjectSetValueUtil();
		setBeanValue.setProperties(beanObject, params);
		
		System.out.println(beanObject.getName());
		System.out.println(beanObject.getAge());
		System.out.println(beanObject.isMale());
	}

}
