package my;

/**
 *
 * 数组中下标为 i 的节点的[左子节点]，就是下标为 i∗2的节点，
 * 右子节点就是下标为 i∗2+1 的节点，
 * 父节点就是下标为  i/2 的节点。
 * @author jiaolong
 * @date 2025/05/13 16:28
 **/
public class Heap {
    private int[] a; // 数组，从下标 1 开始存储数据
    private int n;  // 堆可以存储的最大数据个数
    private int count; // 堆中已经存储的数据个数

    public Heap(int capacity) {
        a = new int[capacity + 1];
        n = capacity;
        count = 0;
    }

    public void insert(int data) {
        if (count >= n) return; // 堆满了
        ++count;
        a[count] = data;
        int i = count;
        while (i/2 > 0 && a[i] > a[i/2]) { // 自下往上堆化
            swap(a, i, i/2); // swap() 函数作用：交换下标为 i 和 i/2 的两个元素
            i = i/2;
        }
    }

    public int removeMax() {
        if (count == 0) return -1; // 堆中没有数据
        a[1] = a[count];
        --count;
        heapify(a, count, 1);

        return count;
    }

    private void heapify(int[] a, int n, int i) { // 自上往下堆化
        while (true) {
            int maxPos = i;
            if (i*2 <= n && a[i] < a[i*2]) maxPos = i*2;
            if (i*2+1 <= n && a[maxPos] < a[i*2+1]) maxPos = i*2+1;
            if (maxPos == i) break;
            swap(a, i, maxPos);
            i = maxPos;
        }
    }


    private static void swap(int[] arr, int i, int j) {
        if (i == j) {
            return;
        }

        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public void printHeap(){
        int treeCountHight = (int) (Math.log(a.length-1) / Math.log(2))+2;
        int treeHight = 1;
        String rightChar = "\\";
        for(int arr=1 ; arr<a.length; arr++){
            if(arr == 1){
                System.out.println(getSpace(treeCountHight+1)+a[arr]);
            }else{
                int childNum = treeHight*2;

                System.out.print(getSpace(treeCountHight));
                for(int i=0;i<treeHight;i++){
                    if(i>0){
                        System.out.print(getSpace(treeCountHight));
                    }
                    System.out.print("/  \\");
                }
                System.out.println ("");

                System.out.print(getSpace(treeCountHight));
                for(int i=0;i<childNum;i++){
                    if(arr+i<a.length){
                        if(i>0){
                            System.out.print(getSpace(treeCountHight-1));
                        }

                        System.out.print(a[arr+i]);
                    }

                }
                System.out.println ("");

                arr = arr+childNum-1;
                ++treeHight;
                --treeCountHight;
            }

        }
        System.out.print("");
    }

    public String getSpace(int n){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<n;i++){
            sb.append(" ");
        }
        return sb.toString();
    }
}
