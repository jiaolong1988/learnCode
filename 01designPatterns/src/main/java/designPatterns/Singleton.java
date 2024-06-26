package designPatterns;

/**
 * 单例模式:
 *      多线程高性能写法。Balking(犹豫模式)，通过双重检查实现。
 * @author: jiaolong
 * @date: 2024/06/26 17:33
 **/
class Singleton {
    //[保证可见性质]
    private static volatile Singleton singleton;

    // 构造方法私有化
    private Singleton() {
    }

    // 获取实例（单例）
    public static Singleton getInstance() {
        // 第一次检查
        if (singleton == null) {
            //[保证原子性质]
            synchronized (Singleton.class) {
                // 获取锁后二次检查
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }

    public static void main(String[] args) {
        Singleton.getInstance();
    }
}
