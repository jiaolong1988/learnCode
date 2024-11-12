/**
 * ++i : 先自增，再赋值
 * i++ : 先赋值，再自增
 * @author: jiaolong
 * @date: 2024/11/01 14:29
 **/
public class Test {
    public static void main(String[] args) {
      /*  i++: 先自增，然后返回 自增之前的值
          ++i: 先自增，然后返回 自增之后的值
         本质上都是执行了 i = i + 1 操作，只是返回值不同而已
      */

        int a=1, b=1;
        System.out.println("a:"+ ++a);  //2
        System.out.println("a:"+ a);    //2
        System.out.println("b:"+ b++);  //1
        System.out.println("b:"+ b);    //2

        System.out.println("================");

        int i = 0;
        int j = 0;
        i = i++;  //等同于 i=i
        System.out.println("i="+i);
        j = i++;
        System.out.println("i = " + i + ", j = " + j);
    }

}
