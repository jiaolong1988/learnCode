package org.example.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 斐波那契数列
 *  	1、1、2、3、5、8、13、21、34、55
 *  计算方法
 * 		从第3项开始，每一项都等于前两项之和。
 *      既第一项、第二项都为1
 *
 * @author jiaolong
 * @date 2023-07-10 03:57:23
 */
public class ForkJoinTest1Fibo {

    public static void main(String[] args) {
        // 创建分治任务线程池
        ForkJoinPool fjp = new ForkJoinPool(4);
        // 创建分治任务
        Fibonacci fib = new Fibonacci(2);
        // 启动分治任务
        Integer result = fjp.invoke(fib);

        // 输出结果
        System.out.println(result);
    }

    // 递归任务
    static class Fibonacci extends RecursiveTask<Integer> {
        private static final long serialVersionUID = -4199481511415415519L;

        final int n;

        Fibonacci(int n) {
            this.n = n;
        }

        protected Integer compute() {
            System.out.println("==="+n);
            if (n <= 1)
                return n;
            Fibonacci f1 = new Fibonacci(n - 1);
            // 创建子任务
            f1.fork();

            Fibonacci f2 = new Fibonacci(n - 2);
            // 等待子任务结果，并合并结果
            return f2.compute() + f1.join();
        }
    }
}
