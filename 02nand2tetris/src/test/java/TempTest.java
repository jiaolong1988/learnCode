import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jiaolong
 * @date 2024/04/21 15:33
 */
public class TempTest {
    public static void main(String[] args) {

        String x ="";
        if(x == null){

        }


    }

    private static String extracted(String tockenInfo) {
        String lable = tockenInfo.split(" ")[0];
        return lable.replace("<","").replace(">", "");
    }
}
