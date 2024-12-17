package base;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author jiaolong
 * @date 2024/12/17 17:45
 **/
public class StringFormatTest {
    public static void main(String[] args) {
        for (Field f : ArrayList.class.getDeclaredFields()) {
            String info = String.format("name:%1$-25s  value: %2$-7s" ,f.getName(),"aaa");
            System.out.println(info);
        }
    }
}
