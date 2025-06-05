package my;

/**
 * @author jiaolong
 * @date 2025/05/13 16:36
 **/
public class HeapTest {
    public static void main(String[] args) {
        Heap heap = new Heap(13);


        heap.insert(33);
        heap.insert(27);
        heap.insert(21);
        heap.insert(16);
        heap.insert(13);
        heap.insert(15);
        heap.insert(19);
        heap.insert(5);
        heap.insert(6);
        heap.insert(7);
        heap.insert(8);
        heap.insert(1);
        heap.insert(2);

        heap.printHeap();
    }
}
