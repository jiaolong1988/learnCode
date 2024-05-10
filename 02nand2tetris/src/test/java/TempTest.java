import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jiaolong
 * @date 2024/04/21 15:33
 */
public class TempTest {
    public static void main(String[] args) {

        List<String> op = Arrays.asList("+", "-", "*", "/", "&", "|", "<", ">", "=");

        for (int i = 0; i < op.size(); i++) {
            String info = op.get(i);
            if (info.equals("<")) {
                op.set(i, "&lt;");
            } else if (info.equals(">")) {
                op.set(i, "&gt;");
            } else if (info.equals("&")) {
                op.set(i, "&amp;");
            }
        }

        op.forEach(System.out::println);


    }

    private static String extracted(String tockenInfo) {
        String lable = tockenInfo.split(" ")[0];
        return lable.replace("<","").replace(">", "");
    }
}
