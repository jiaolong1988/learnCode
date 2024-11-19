package base;

/**
 * 字符串对象的创建方式，1.常量池创建、2.对象创建
 * @author: jiaolong
 * @date: 2024/07/05 15:10
 **/
public class StringObjectTest {
    public static void main(String[] args) {

        //写入常量池，将常量池对象的引用给 堆中的对象，再将对象引用赋值 给变量a
        String a = new String("abc");
        String b = new String("abc");

        System.out.println(a==b); //false

        //变量在内存中的地址
        System.out.println(System.identityHashCode(a));
        System.out.println(System.identityHashCode(b));

        System.out.println("======");

        //将常量池对象 的引用 赋值给变量(如果对象不存在，会在运行时常量池中创建字符串对象。)
        String e = a.intern();
        String f = b.intern();

        //将 字面量"abc" 写入常量池
        String c = "abc";

        System.out.println(System.identityHashCode(e));
        System.out.println(System.identityHashCode(f));
        System.out.println(System.identityHashCode(c));
    }
}
