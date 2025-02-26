package org.example.forkjoin;

/**
 * 递归案例的练习
 * @author jiaolong
 * @date 2023-07-10 05:50:23
 */
public class RecursiveLearn {

    public static void main(String[] args) {

        System.out.println("递归实现方式1，求和结果："+sum(4));
        System.out.println("递归实现方式2，求和结果："+a(4));
    }

    /**
     * 递归，前n项求和
     * @param n
     * @return
     */
    public static int sum(int n) {
        if(n == 1) {
            //递归出口
            return 1;

        }else {
            //调用递归

            //n-1
            int j = n - 1;

            //n 加上 n-1项的和
            return n + sum(j);
        }

    }

    /**
     * 计算 1+2+3+4的和
     * 	递归分解步骤，a、b、c、d 方法的步骤，无非是名字不一样，递归 无非是 将这些方法 进行归纳汇总
     * @param x
     * @return
     */
    public  static int a(int x) {
        //4+(3+2+1) 意思是 第4项的值 加上 前三项的和
        int res = x + b(x - 1);
        return res;
    }

    private static int b(int x) {
        //3+(2+1)
        int res = x + c(x - 1);
        return res;
    }

    private static int c(int x) {
        //2+(1)
        int res = x + d (x - 1);
        return res;
    }

    private static int d(int i) {
        //i=1
        return 1;
    }

}
