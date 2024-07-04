package regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://regexr.com/
 * @author: jiaolong
 * @date: 2024/07/03 10:29
 **/
public class Test {
    public static void main(String[] args) {
        String str = " The fat cat sat on the mat.";
        String regex = "(T|t)he(?=\\sfat)";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);

        List<String> matches = new ArrayList<>();
        while (m.find()) {
            System.out.println(m.group());
            matches.add(m.group());
        }
    }
}
