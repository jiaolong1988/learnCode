package collection;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 非迭代器 动态删除 集合数据异常案例：ConcurrentModificationException
 * @author: jiaolong
 * @date: 2024/07/10 10:36
 **/
public class ListDelError {

    /*     for(:)循环[这里指的不是for(;;)]是一个语法糖，这里会被解释为迭代器，在使用迭代器遍历时，
           ArrayList内部创建了一个内部迭代器iterator，在使用next()方法来取下一个元素时，
           会使用ArrayList里保存的一个用来记录List修改次数的变量modCount，
           与iterator保存了一个expectedModCount来表示期望的修改次数进行比较，如果不相等则会抛出异常；

            而在在foreach循环中调用list中的remove()方法，会走到fastRemove()方法，该方法不是iterator中的方法，
            而是ArrayList中的方法，在该方法只做了modCount++，而没有同步到expectedModCount。

            当再次遍历时，会先调用内部类iteator中的hasNext(),再调用next(),在调用next()方法时，会对modCount和expectedModCount进行比较，
            此时两者不一致，就抛出了ConcurrentModificationException异常。

            所以关键是用ArrayList的remove还是iterator中的remove。
    */
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        list.add("a");
        list.add("a");
        list.add("b");
        list.add("b");
        list.add("c");
        list.add("c");
        remove2(list);// 删除指定的“b”元素

        for(int i=0; i<list.size(); i++)
        {
            System.out.println("element : " + list.get(i));
        }

    }
    public static void remove1(ArrayList<String> list)
    {
        Iterator<String> it = list.iterator();

        while (it.hasNext()) {
            String str = it.next();

            if (str.equals("b")) {
                it.remove();
            }
        }

    }
    public static void remove2(ArrayList<String> list)
    {
        for (String s : list)
        {
            if (s.equals("b"))
            {
                list.remove(s);
            }
        }
    }
}
