package util.seqExecFunction;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Properties工具类，支持顺序存储
 * @author jiaolong
 * @date 2023-11-16 04:24:12
 */
public class OrderedPropertiesUtil{
	private static final Logger logger = Logger.getLogger(OrderedPropertiesUtil.class);

	private final OrderedProperties prop ;
	//属性文件的绝对地址
	private final String propertyFilePath ;

	/**
	 * @param propertyFilePath - 配置文件路径
	 **/
	public OrderedPropertiesUtil(String propertyFilePath) {
		this.propertyFilePath = propertyFilePath;
		this.prop = new OrderedProperties();
	}

	/**
	 * 创建Properties 文件
	 * @param values - 属性信息
	 * @return boolean
	 **/
	public synchronized boolean createProperFile(Map<String, String> values) {
		for (Map.Entry<String, String> entry : values.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			prop.setProperty(key, value);
		}

		return outPropFile();
	}


	/**
	 * 修改属性至文件中
	 * @param propName 属性名称
	 * @param newValue 属性值
	 */
	public synchronized boolean setValToFile(String propName, String newValue) {
		loadFile();
		prop.setProperty(propName, newValue);
		outPropFile();

		return getVal(propName).equals(newValue);
	}

	/**
	 *
	 * @param propName - 获取指定属性的值
	 * @return java.lang.String
	 **/
	public synchronized String getVal(String propName) {
		loadFile();

		String val = prop.getProperty(propName);
		if(val == null) {
			logger.error("=====================>>>>>>>>>>>>>>>>>> read interrupt file info is null. attrName:"+propName +"  filePath:"+propertyFilePath);
			for (Map.Entry<Object, Object> entry :prop.entrySet()) {
				Object key = entry.getKey();
				Object value = entry.getValue();

				logger.error("getVal ["+key+"] ==> key:"+key+" value:"+value);
			}

			System.exit(0);
		}

		return val.trim();
	}

	/**
	 * 获取指定属性的值
	 * @return 返回单项属性的所有值
	 */
	public synchronized Map<String,String> getAllVal() {

		if(!loadFile()) {
			return Collections.emptyMap();
		}

		Map<String,String> result = new HashMap<>();
		for (Map.Entry<Object, Object> entry :prop.entrySet()) {
			String key = (String)entry.getKey();
			String value =(String) entry.getValue();

			result.put(key, value);
		}
		return result;
	}

	public synchronized void getAllValPrint() {
		loadFile();

		StringBuilder sb = new StringBuilder("config info:\n");
		Map<String,String> map = getAllVal();

		prop.getSortedKeys().forEach(k->{
			String v = map.get(k);
			sb.append(k).append("=").append(v).append("\n");
		});

		logger.info(sb.toString());
		sb.setLength(0);
	}

	private synchronized boolean outPropFile() {
		try(OutputStream out = new FileOutputStream(propertyFilePath)) {
			prop.store(out, "Project configuration file");
			return true;
		} catch (IOException e) {
			logger.error(" Interrupt file write fail, filePath:"+propertyFilePath,e);
			System.exit(0);
		}
		return false;
	}


	/**
	 * 读取中断文件将
	 * @return true：加载文件成功， false:文件加载失败
	 */
	private boolean loadFile() {
		if(new File(propertyFilePath).exists()) {
			try(FileInputStream fis = new FileInputStream(propertyFilePath)) {
				prop.load(fis);
				return true;
			} catch (IOException e) {
				logger.error("load file error."+propertyFilePath, e);
			}
		}else {
			logger.error("properties file not exist."+propertyFilePath);
		}

		return false;
	}

	 class OrderedProperties extends Properties{

		private static final long serialVersionUID = 4710927773256743817L;

		private final LinkedHashSet<Object> keys = new LinkedHashSet<>();

		@Override
		public Enumeration<Object> keys() {
			return Collections.enumeration(keys);
		}

		@Override
		public synchronized Object put(Object key, Object value) {
			keys.add(key);
			return super.put(key, value);
		}

		@Override
		public Set<Object> keySet() {
			return keys;
		}

		@Override
		public Collection<Object> values() {
			Set<Object> set = new LinkedHashSet<>();

			for (Object key : this.keys) {
				set.add(this.getProperty((String) key));
			}

			return set;
		}

		@Override
		public synchronized Object remove(Object key) {
			keys.remove(key);
			return super.remove(key);
		}

		@Override
		public Set<String> stringPropertyNames() {
			Set<String> set = new LinkedHashSet<>();

			for (Object key : this.keys) {
				set.add((String) key);
			}

			return set;
		}

		public LinkedHashSet<Object> getSortedKeys() {
			return keys;
		}
	}
}

