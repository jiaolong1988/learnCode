package stream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * stream的常用方法
 * @author jiaolong
 * @date 2024-12-10 14:38
 */
public class StreamMethod {

	public static void main(String[] args) {		

		// 流自动关闭,stream(),of()都是串行的Stream,parallelStream()时并行的stream对象。
		Stream<String> mapStream = Arrays.asList("1", "2", "3", "4").stream();//.parallelStream();	
		mapStream.close();
		
		Stream<String> flatMapStream = Stream.of("test", "t1", "t2", "teeeee", "aaaa");	
		// reuseSupplier.get() 方式重新获取流，从而实现流的重用
		Supplier<Stream<String>> reuseSupplier = () -> Arrays.asList("1", "2", "3", "4","1","2").stream();
		
		// map()
		Function<String, Integer> map = num -> Integer.parseInt(num) + 1;
		List<Integer> mapList = mapStream.map(map).collect(Collectors.toList());
		System.out.println("map:"+mapList);
		 
		// flatMap()
		Function<String, Stream<String>> flatMap = n -> {return Stream.of(n.split(""));};//每条数据产生一个流，生成多个数据
		List<String> flatMapList = flatMapStream.flatMap(flatMap).collect(Collectors.toList());
		System.out.println("flatMap:"+flatMapList);
				
		//filter
		Predicate<String> fliter = a -> a.startsWith("1"); //过滤规则
		List<String> filterList = reuseSupplier.get().filter(fliter).collect(Collectors.toList());
		System.out.println("fliter:"+filterList);
		
		//forEach
		reuseSupplier.get().forEach(System.out::println);			
		
    	//count
    	long count = reuseSupplier.get().count();
    	System.out.println("count:"+count);
    	
    	//distinct
    	List<String> distinctList = reuseSupplier.get().distinct().collect(Collectors.toList());
    	System.out.println("distinctList:"+distinctList);
    	
        //sort排序
    	List<String> sortList = reuseSupplier.get()
    			.sorted((i1, i2) -> i2.compareTo(i1))
    			.collect(Collectors.toList());
    	System.out.println("sortList:"+sortList);
    	
    	//limit返回一个新的流
    	List<String> limitList = reuseSupplier.get().limit(2).collect(Collectors.toList());
    	System.out.println("limitList:"+limitList);
    	//skip返回一个新的流
    	List<String> skipList = reuseSupplier.get().skip(2).collect(Collectors.toList());
    	System.out.println("skipList:"+skipList);
    	 
    	//onClose
    	try(Stream<String> s1 = reuseSupplier.get();){
        	 s1.onClose(()->System.out.println("结束前动作。。"));
    	}
    	
    	//
    	 Iterator<String> iter = reuseSupplier.get().iterator();
    	 while(iter.hasNext()) {
    		String value= iter.next();
    		System.out.println("iter---"+value);
    	 }

//多线程执行    	 
		   List<Integer> numbers = Arrays.asList(1, 2, 5, 4);
		   numbers.parallelStream().forEach(num->System.out.println(Thread.currentThread().getName()+">>"+num));
		   
//stream转换为数组
    	 String line ="1 ,2 ,3";
		 Object[] objs = Arrays.stream(line.split(",")).toArray();
		 String[] strings = Arrays.stream(line.split(",")).map(String::trim).toArray(String[]::new);
		 Integer[] integers = Arrays.stream(line.split(",")).map(String::trim).toArray(Integer[]::new);
    	 
    	 
//文件Stream
      	try {
 			Stream<String> lines = Files.lines(Paths.get("D:\\test\\aaa.txt"));
 			System.out.println(lines.collect(Collectors.toList()));			
 		} catch (IOException e) {}
      	
    	 
//静态方法    	 
 		//concat 流合并	
        List<String> list1 = Arrays.asList("李四1","李四2"); 
        List<String> list2 =  Arrays.asList("1","2");
        List<String> resultList = Stream.concat(list1.stream(), list2.stream()).collect(Collectors.toList());
     	System.out.println("concat:"+resultList);
     	 
     	//builder
     	Stream.builder().add("a").add("2").build().forEach(System.out::println);

//数据分组
     	Supplier<Stream<String>> data = () ->  Stream.of("1", "22", "33", "4", "555");
		Map<Integer, List<String>> collect = data.get().collect(Collectors.groupingBy(String::length));
		System.out.println("数据分组："+collect);
			
		Map<Integer, Long> collect1 = data.get().collect(Collectors.groupingBy(String::length, Collectors.counting()));
		System.out.println("数据分组："+collect1);
    	
	}
	

}
