package my;

/**
 * @author jiaolong
 * @date 2024/12/13 16:05
 **/
public class BinarySearchLearn {
    public static void main(String[] args) {
        int[] a = {1, 3, 5, 7, 9};
        int x = bsearch(a, a.length, 3);
        int y = bsearch2(a, a.length, 3);
        System.out.println(y);
    }

    public static int bsearch(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            if (a[mid] == value) {
                return mid;
            } else if (a[mid] < value) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return -1;
    }

    // 二分查找的递归实现
    public static int bsearch2(int[] a, int n, int val) {
        return bsearchInternally(a, 0, n - 1, val);
    }

    private static int bsearchInternally(int[] a, int low, int high, int value) {
        if (low > high) return -1;

        int mid =  low + ((high - low) >> 1);
        if (a[mid] == value) {
            return mid;
        } else if (a[mid] < value) {
            return bsearchInternally(a, mid+1, high, value);
        } else {
            return bsearchInternally(a, low, mid-1, value);
        }
    }
}
