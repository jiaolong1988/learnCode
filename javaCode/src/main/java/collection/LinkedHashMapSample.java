package collection;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LinkedHashMap 自定义删除策略，保持指定的数据
 * @author jiaolong
 * @date 2024-6-14 11:48
 */
public class LinkedHashMapSample {
    public static void main(String[] args) {
        LinkedHashMap<String, String> accessOrderedMap = new LinkedHashMap<String, String>(16, 0.75F, true) {
            private static final long serialVersionUID = -1954333245094366905L;

            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) { // 实现自定义删除策略，否则行为就和普遍 Map 没有区别
                return size() > 1;
            }
        };
        accessOrderedMap.put("Project1", "Valhalla");
        accessOrderedMap.put("Project2", "Panama");
        accessOrderedMap.put("Project3", "Loom");
        accessOrderedMap.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });
        System.out.println("-------------init---------");
        // 模拟访问
        accessOrderedMap.get("Project2");
        accessOrderedMap.get("Project2");
        accessOrderedMap.get("Project3");
        System.out.println("Iterate over should be not affected:");
        accessOrderedMap.forEach((k, v) -> {
            System.out.println(k + ":" + v);
        });

        System.out.println("-------------模拟访问---------");
        // 触发删除
        accessOrderedMap.put("Project4", "Mission Control4");
        accessOrderedMap.put("Project5", "Mission Control5");
        accessOrderedMap.put("Project6", "Mission Control6");
        System.out.println("Oldest entry should be removed:");
        accessOrderedMap.forEach((k, v) -> {// 遍历顺序不变
            System.out.println(k + ":" + v);
        });
    }
}
	 