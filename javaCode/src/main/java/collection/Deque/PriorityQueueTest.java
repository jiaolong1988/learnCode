package collection.Deque;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 队列 排序
 * @author jiaolong
 * @date 2024-11-5 17:07
 */
public class PriorityQueueTest {
    public static void main(String[] args) {
		Comparator<Integer> cmp = new Comparator<Integer>(){
			@Override
			public int compare(Integer a, Integer b) {
				//第一个cmp.compare(a, b) > 0 说明a>b，返回-1，说明a在b前面，返回1，说明a在b后面
				//-1、0、1分别表示 第一个参数小于、等于或大于 第二个参数
				return a>b ? -1 : a==b ? 0 : 1 ;
			}
		};

        PriorityQueue pq = new PriorityQueue(cmp);
        // 下面代码依次向pq中加入四个元素
        pq.offer(6);
        pq.offer(-3);
        pq.offer(20);
        pq.offer(18);
        // 输出pq队列，并不是按元素的加入顺序排列
        System.out.println(pq); // 输出[-3, 6, 20, 18]

        // 访问队列第一个元素，其实就是队列中最小的元素：-3
        System.out.println("--> "+pq.poll());
        System.out.println("--> "+pq.poll());
    }
}
