import java.util.ArrayList;
import java.util.List;

/**
 * @author jiaolong
 * @date 2024/04/21 15:33
 */
public class TempTest {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2 ");
        list.add("3  ");
        list.forEach(System.out::println);
        list.remove(list.size()-1);
        list.forEach(System.out::println);

    }
}
