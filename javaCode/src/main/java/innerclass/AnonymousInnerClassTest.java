package innerclass;

/**
 * 匿名内部类 测试
 * @author jiaolong
 * @date 2024/12/31 14:16
 **/
public class AnonymousInnerClassTest {
    public static void main(String[] args) {
        AnonymousInnerClassTest anonymousInnerClass = new AnonymousInnerClassTest();

        // 1. 接口匿名内部类
        anonymousInnerClass.test(
            new Product() {
                {
                    System.out.println("00-接口 匿名内部类的初始化块...");
                }

                @Override
                public double getPrice() {
                    return 567.8;
                }

                @Override
                public String getName() {
                    return "AGP显卡";
                }
            }
        );

        // 2.抽象类 调用有参数的构造器创建Device匿名实现类的对象
        anonymousInnerClass.test(
            new Device("电子示波器") {
                // 初始化实例块
                {
                    System.out.println("11-抽象类 匿名内部类的初始化块...");
                }

                public double getPrice() {
                    return 67.8;
                }
            }
        );

        // 3.抽象类 调用无参数的构造器创建Device匿名实现类的对象
        Device d = new Device() {
            // 初始化实例块
            {
                System.out.println("22-抽象类 匿名内部类的初始化块...");
            }

            // 实现抽象方法
            public double getPrice() {
                return 56.2;
            }

            // 重写父类的实例方法
            public String getName() {
                return "键盘";
            }
        };
        anonymousInnerClass.test(d);

    }

    public void test(Product p) {
        System.out.println("interface: 购买了一个" + p.getName() + "，花掉了" + p.getPrice()+"\n");
    }

    public void test(Device d) {
        System.out.println("abstract: 购买了一个" + d.getName() + "，花掉了" + d.getPrice()+"\n");
    }
}


interface Product {
    double getPrice();

    String getName();
}

abstract class Device {
    private String name;

    public abstract double getPrice();

    public Device() {
    }

    public Device(String name) {
        this.name = name;
    }

    // 此处省略了name的setter和getter方法
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}