package my;

/**
 * The type Binary search change.
 *
 * @author jiaolong
 * @date 2024 /12/25 10:11
 */
public class BinarySearchChange {
    public static void main(String[] args) {
        int[] arr = {1, 3, 4, 5, 6, 8, 8, 8, 11, 18};
        System.out.println("变体一：查找第一个值等于给定值的元素:"+
                BinarySearchChange.bsearchFirst(arr, arr.length, 8));

        //变体二：查找最后一个值等于给定值的元素
        System.out.println("变体二：查找最后一个值等于给定值的元素:"+
                BinarySearchChange.bsearchLast(arr, arr.length, 8));

        //变体三：查找第一个大于等于给定值的元素
        System.out.println("变体三：查找第一个大于等于给定值的元素:"+
                BinarySearchChange.bsearchFirstGreaterOrEqual(arr, arr.length, 8));


        //变体四：查找最后一个小于等于给定值的元素
        System.out.println("变体四：查找最后一个小于等于给定值的元素:"+
                BinarySearchChange.bsearchLastLessOrEqual(arr, arr.length, 9));
    }

    /**
     * 变体二：查找最后一个值等于给定值的元素
     */
    public static int bsearchFirst(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {

            //int mid = (low + high)/2 [这种写法有可能会导致溢出，比如low很大，high也很大，之和就溢出了]
            int mid =  low + ((high - low) >> 1);
            if (a[mid] > value) {
                //大于
                high = mid - 1;
            } else if (a[mid] < value) {
                //小于
                low = mid + 1;
            } else {
                //等于
                if ((mid == 0) || (a[mid - 1] != value)) return mid;
                else high = mid - 1;
                /* 如果 mid 等于 0，那这个元素已经是数组的第一个元素，那它肯定是我们要找的；
                   如果 mid 不等于 0，但 a[mid] 的前一个元素 a[mid-1] 不等于 value，那也说明 a[mid] 就是我们要找的第一个值等于给定值的元素。
                   如果经过检查之后发现 a[mid] 前面的一个元素 a[mid-1] 也等于 value，那说明此时的 a[mid] 肯定不是我们要查找的第一个值等于给定值的元素。
                   那我们就更新 high=mid-1，因为要找的元素肯定出现在 [low, mid-1] 之间。
                */
            }
        }
        return -1;
    }

    /**
     * 变体一：查找第一个值等于给定值的元素
     */
    public static int bsearchLast(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid =  low + ((high - low) >> 1);
            if (a[mid] > value) {
                high = mid - 1;
            } else if (a[mid] < value) {
                low = mid + 1;
            } else {
                if ((mid == n - 1) || (a[mid + 1] != value)) return mid;
                else low = mid + 1;
            }
        }
        return -1;
    }

    //变体三：查找第一个大于等于给定值的元素
    public static int bsearchFirstGreaterOrEqual(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid =  low + ((high - low) >> 1);
            if (a[mid] >= value) {
                /*如果 a[mid] 前面已经没有元素，或者前面一个元素小于要查找的值 value，那 a[mid] 就是我们要找的元素。*/
                if ((mid == 0) || (a[mid - 1] < value)) return mid;
                else high = mid - 1;

            } else {
                low = mid + 1;
            }
        }
        return -1;
    }

    //变体四：查找最后一个小于等于给定值的元素
    public static int bsearchLastLessOrEqual(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid =  low + ((high - low) >> 1);
            if (a[mid] > value) {
                high = mid - 1;
            } else {
                if ((mid == n - 1) || (a[mid + 1] > value)) return mid;
                else low = mid + 1;
            }
        }
        return -1;
    }
}
