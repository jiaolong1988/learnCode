package stream;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 将String 转换为 List<Character> 测试类
 * @author jiaolong
 * @date 2024/12/10 14:29
 **/
public class StringToCharListTest {
    public static void main(String[] args) {
        String str = "hello world";
        List<Character> charList1 = str.chars()
                    .mapToObj(c -> (char) c)
                    .collect(Collectors.toList());
        System.out.println(charList1);
    }
}
