import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.LinkedList;

/**
 * @author: jiaolong
 * @date: 2024/05/21 13:43
 **/
public class DequeTest {
    public static void main(String[] args) throws FileNotFoundException {
        ArrayDeque<String> stack = new ArrayDeque<>();
        LinkedList<String> listDeque = new LinkedList<>();

        System.out.println("判断listDeque是否为空："+listDeque.isEmpty());

        //队列操作-先进先出  [Dog, Cat, Horse]
        stack.offer("Dog");
        stack.offer("Cat");
        stack.offer("Horse");

        //栈操作-后进先出  [Horse, Cat, Dog]
//        stack.push("Dog");
//        stack.push("Cat");
//        stack.push("Horse");
        System.out.println("Stack: " + stack);

        //从顶部访问元素
        String element = stack.peek();
        System.out.println("访问元素： " + element);


        //返回元素，并删除顶部元素
        String remElement = stack.poll();
        System.out.println("删除element: " + remElement);

        stack.push("Dog");
        System.out.println("访问元素： " + stack);
    }
}
