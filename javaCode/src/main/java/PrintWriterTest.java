import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author: jiaolong
 * @date: 2024/05/24 15:04
 **/
public class PrintWriterTest {
    public static void main(String[] args) throws IOException {

        PrintWriter pw = new PrintWriter(new FileWriter("pw2.txt"), true);
        //写任意类型
        pw.println("hello");
        pw.println(100);
        pw.println(true);

        pw.close();


    }
}
