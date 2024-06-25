import java.util.HashMap;
import java.util.Map;

/**
 * @author: jiaolong
 * @date: 2024/06/24 16:45
 **/
public class TempTest {
    public static void main(String[] args) {
        Map<String, String> test =new HashMap<>();
        test.put("a","111");
        test.put("b","222");
        test.put("a","ccc");

        System.out.println(test.remove("a"));
    }
}
