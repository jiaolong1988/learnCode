package org.example.lock;

/**
 * 1.当修饰静态方法的时候，锁定的是当前类的 Class 对象，在上面的例子中就是 SynchronizedTest类;
 * 2.当修饰非静态方法的时候，锁定的是当前实例对象 this;
 *
 * @author jiaolong
 * @date 2025/02/26 14:43
 **/
public class SynchronizedTest {
    /**
     1.修饰非静态方法
     锁是this,也就是调用的这个方法的对象，因此锁保护的范围是 这个对象内的资源，
     无法对其他对象的字样进行保护。
     该锁也可以说是细粒度的锁
     **/
    synchronized void foo() {
        // 临界区
    }

    /**
     2.修饰静态方法
     该锁是当前调用这个方法的类，因此该锁的保护范围就是整个类。
     该锁也可以说粗粒度的锁
     **/
    synchronized static void bar() {
        // 临界区
    }

    // 3.修饰代码块
    Object obj = new Object();
    void baz() {
        /**
         该同步代码可以实现两种的同步，关键在于lock是实例对象 还是 静态对象(也可以是class)

         Object lock= new Object()；        该锁 等同于 1修饰非静态同步方法
         static Object lock= new Object()； 该锁 等同于 2修饰静态同步方法
         Class<? > lock = Account.class     该锁 等同于 2修饰静态同步方法
         **/
        synchronized(obj) {
            // 临界区
        }
    }
}
