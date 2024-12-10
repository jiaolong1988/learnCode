package stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * java 集合转换为 stream 流
 * @author 		jiaolong
 * @date 		2022-09-20 11:03:56
 */
public class ConverStreamWay {
	public static void main(String[] args) {
		// Collction接口的集合转换为stream
		Collection<String> collection = new ArrayList();
		Stream<String> stream = collection.stream();
		// List集合
		List<String> list = new ArrayList<>();
		Stream<String> stream1 = list.stream();
		// ArrayList集合
		ArrayList<String> arrayList = new ArrayList<>();
		Stream<String> stream2 = arrayList.stream();
		// LinkedList集合
		LinkedList<String> linkedList = new LinkedList<>();
		Stream<String> stream3 = linkedList.stream();
		
		// Map集合转Stream
		Map<String, String> map = new HashMap<>();
		Stream<String> mapKeyStream = map.keySet().stream();
		Stream<String> mapValuesStream = map.values().stream();
		Stream<Map.Entry<String, String>> mapEntrysetStream = map.entrySet().stream();
		
		//of()方式 转换stream
		List<String> list1 = new ArrayList<>();
		Stream<List<String>> stream4 = Stream.of(list1);
		// 传入可变数组
		Stream<String> stream5 = Stream.of("1", "2", "3", "4");
		Stream<Integer> integerStream = Stream.of(1, 2, 3, 4);

	}

}
