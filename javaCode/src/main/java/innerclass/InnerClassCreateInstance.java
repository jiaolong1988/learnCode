package innerclass;

/**
 * 内部类 创建实例对象
 * @author jiaolong
 * @date 2024/12/31 13:52
 **/
public class InnerClassCreateInstance {
    public static void main(String[] args) {

        //1. 非静态内部类 创建实例对象
        Out.In outIn = new Out().new In("测试信息");
		/*
		上面代码可改为如下三行代码：
		使用OutterClass.InnerClass的形式定义内部类变量
		Out.In in;
		创建外部类实例，非静态内部类实例将寄存在该实例中
		Out out = new Out();
		通过外部类实例和new来调用内部类构造器创建非静态内部类实例
		in = out.new In("测试信息");
		*/

        //2. 静态内部类 创建实例对象
        Out.StaticIn outStaticIn = new Out.StaticIn();
		/*
		上面代码可改为如下两行代码：
		使用OutterClass.InnerClass的形式定义内部类变量
		StaticOut.StaticIn in;
		通过new来调用内部类构造器创建静态内部类实例
		in = new StaticOut.StaticIn();
		*/
    }
}



class Out {
    // 定义一个静态内部类，不使用访问控制符，
    // 即同一个包中其他类可访问该内部类
    static class StaticIn {

        public StaticIn() {
            System.out.println("静态内部类的构造器");
        }
    }


    // 定义一个内部类，不使用访问控制符，
    // 即只有同一个包中其他类可访问该内部类
    class In{
        public In(String msg){
            System.out.println("非静态内部类的构造器："+msg);
        }
    }
}