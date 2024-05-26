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

        System.out.println("�ж�listDeque�Ƿ�Ϊ�գ�"+listDeque.isEmpty());

        //���в���-�Ƚ��ȳ�  [Dog, Cat, Horse]
        stack.offer("Dog");
        stack.offer("Cat");
        stack.offer("Horse");

        //ջ����-����ȳ�  [Horse, Cat, Dog]
//        stack.push("Dog");
//        stack.push("Cat");
//        stack.push("Horse");
        System.out.println("Stack: " + stack);

        //�Ӷ�������Ԫ��
        String element = stack.peek();
        System.out.println("����Ԫ�أ� " + element);


        //����Ԫ�أ���ɾ������Ԫ��
        String remElement = stack.poll();
        System.out.println("ɾ��element: " + remElement);

        stack.push("Dog");
        System.out.println("����Ԫ�أ� " + stack);
    }
}
