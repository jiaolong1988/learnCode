import jdk.nashorn.internal.runtime.linker.Bootstrap;

/**
 * @author: jiaolong
 * @date: 2024/06/24 12:00
 **/
public class ClassLoaderTest {
    public static void main(String[] args) {
/*      顺序是：
        1. Bootstrap CLassloder
        2. Extention ClassLoader
        3. AppClassLoader
*/

        //AppClassLoader
        System.out.println(ClassLoaderTest.class.getClassLoader());
        //ExtClassLoader的父加载器是null（因为是C语言实现的，所以打不出名字）;
        System.out.println(String.class.getClassLoader());

        System.out.println("================");

        ClassLoader cl = ClassLoaderTest.class.getClassLoader();
        System.out.println("ClassLoader is ==> "+cl.toString());
        System.out.println("ClassLoader's parent  ==> "+cl.getParent());
        System.out.println("ClassLoader's parent is ==> "+cl.getParent().getParent());

    }
}
