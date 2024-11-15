package demo.a12_sorts;

import java.util.Arrays;

/**
 * 快速排序
 * Created by wangzheng on 2018/10/16.
 */
public class QuickSort {
  public static void main(String[] args) {
    int[] a = {6,11,3,9,8};
    quickSort(a, a.length);
    System.out.println(Arrays.toString(a));
  }

  // 快速排序，a是数组，n表示数组的大小
  public static void quickSort(int[] a, int n) {
    quickSortInternally(a, 0, n-1);
  }

  // 快速排序递归函数，p,r为下标
  private static void quickSortInternally(int[] a, int p, int r) {
    if (p >= r) return;

    int q = partition(a, p, r); // 获取分区点
    quickSortInternally(a, p, q-1);
    quickSortInternally(a, q+1, r);
  }

  private static int partition(int[] a, int p, int r) {
    int pivot = a[r];
    int i = p;
    for(int j = p; j < r; ++j) {
      //只有元素小于时执行
      if (a[j] < pivot) {
            if (i == j) {
              ++i;
            } else {
              //a[i] 与 a[j]元素交换
              int tmp = a[i];
              a[i++] = a[j];
              a[j] = tmp;
            }
      }
    }

    //交换最后一个元素
    int tmp = a[i];
    a[i] = a[r];
    a[r] = tmp;

    System.out.println("i=" + i);
    return i;
  }
}
