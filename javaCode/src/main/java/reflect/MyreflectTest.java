package reflect;

import java.util.HashMap;
import java.util.Map;

public class MyreflectTest {

	public static void main(String[] args) {
		Map<String,String> params = new HashMap<>();
		params.put("name", "zaibiechu");
		
		TestBean beanObject = new BeanCreatorUtil().createBeanUseDefaultConstruct(TestBean.class);

		BeanObjectSetValueUtil setBeanValue = new BeanObjectSetValueUtil();
		setBeanValue.setProperties(beanObject, params);
		
		System.out.println(beanObject.getName());
	}

}
