package stream;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jiaolong
 * @date 2025/03/06 16:41
 **/
public class StreamCollectorsTest {
    public static void main(String[] args) {
        ArrayList nums = new ArrayList();
        nums.add(2);
        nums.add(-5);
        nums.add(3);
        nums.add(0);

        commonlyUsed();
        searchTest(nums);
        sortTest(nums);
        SynchronizedTest();
        UnmodifiableTest();

    }

    static void commonlyUsed(){

/*        1. toList()
        将流中的元素收集到一个 List 中。*/
        String str = "hello world";
        List<Character> charList1 = str.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        System.out.println(charList1);

 /*       2. toSet()
        将流中的元素收集到一个 Set 中。*/
        List<String> names = Arrays.asList("Alice", "Bob", "Alice");
        Set<String> nameSet = names.stream().collect(Collectors.toSet());
        System.out.println(nameSet); // 输出: [Alice, Bob]

/*        3. toMap()
        将流中的元素收集到一个 Map 中。*/
        Map<String, Integer> nameLengthMap = names.stream()
                .collect(Collectors.toMap(name -> name, String::length));
        System.out.println(nameLengthMap); // 输出: {Alice=5, Bob=3, Charlie=7}


/*        4. joining()
        将流中的元素连接成一个字符串。*/
        String result = names.stream().collect(Collectors.joining(", "));
        System.out.println(result); // 输出: Alice, Bob, Charlie

/*        5. groupingBy()
        根据某个条件对流中的元素进行分组。*/
        final Supplier<Stream<String>> data = () ->  Stream.of("1", "22", "33", "4", "555");
        Map<Integer, List<String>> collect = data.get().collect(Collectors.groupingBy(String::length));
        System.out.println("数据分组："+collect);

      /*  6. counting()
        计算流中元素的数量。*/
        long count = names.stream().collect(Collectors.counting());
        System.out.println(count); // 输出: 3

/*        7. summarizingInt()
        对流中元素的整数值进行统计。*/
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        IntSummaryStatistics stats = numbers.stream().collect(Collectors.summarizingInt(Integer::intValue));
        System.out.println(stats); // 输出: IntSummaryStatistics{count=5, sum=15, min=1, average=3.000000, max=5}

/*        8. partitioningBy()
        将流中的元素根据某个条件进行分区。*/
        Map<Boolean, List<String>> partitioned = names.stream()
                .collect(Collectors.partitioningBy(name -> name.length() > 3));
        System.out.println(partitioned); // 输出: {false=[Bob, Eve], true=[Alice, Charlie, David]}

    }

    static void searchTest(ArrayList nums){

        System.out.println(nums); // 输出:[2, -5, 3, 0]

        System.out.println(Collections.max(nums)); // 输出最大元素，将输出3
        System.out.println(Collections.min(nums)); // 输出最小元素，将输出-5

        Collections.replaceAll(nums , 0 , 1); // 将nums中的0使用1来代替
        System.out.println(nums); // 输出:[2, -5, 3, 1]

        // 判断-5在List集合中出现的次数，返回1
        System.out.println(Collections.frequency(nums , -5));
        Collections.sort(nums); // 对nums集合排序
        System.out.println(nums); // 输出:[-5, 1, 2, 3]
        //只有排序后的List集合才可用二分法查询，输出3
        System.out.println(Collections.binarySearch(nums , 3));
    }

    static void sortTest(ArrayList nums){
        System.out.println(nums); // 输出:[2, -5, 3, 0]
        Collections.reverse(nums); // 将List集合元素的次序反转
        System.out.println(nums); // 输出:[0, 3, -5, 2]
        Collections.sort(nums); // 将List集合元素的按自然顺序排序
        System.out.println(nums); // 输出:[-5, 0, 2, 3]
        Collections.shuffle(nums); // 将List集合元素的按随机顺序排序
        System.out.println(nums); // 每次输出的次序不固定
    }

    static void SynchronizedTest(){
        // 下面程序创建了四个线程安全的集合对象
        Collection c = Collections.synchronizedCollection(new ArrayList());
        List list = Collections.synchronizedList(new ArrayList());
        Set s = Collections.synchronizedSet(new HashSet());
        Map m = Collections.synchronizedMap(new HashMap());
    }

    static void UnmodifiableTest(){
        // 创建一个空的、不可改变的List对象
        List unmodifiableList = Collections.emptyList();
        // 创建一个只有一个元素，且不可改变的Set对象
        Set unmodifiableSet = Collections.singleton("疯狂Java讲义");
        // 创建一个普通Map对象
        Map scores = new HashMap();
        scores.put("语文" , 80);
        scores.put("Java" , 82);
        // 返回普通Map对象对应的不可变版本
        Map unmodifiableMap = Collections.unmodifiableMap(scores);
        // 下面任意一行代码都将引发UnsupportedOperationException异常
        unmodifiableList.add("测试元素");   //①
        unmodifiableSet.add("测试元素");    //②
        unmodifiableMap.put("语文" , 90);   //③
    }
}
