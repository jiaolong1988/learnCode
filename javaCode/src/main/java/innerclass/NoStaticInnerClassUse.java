package innerclass;

/**
 * 非静态内部类使用总结
 * 	1.只能在外部类中进行 实例化调用，例如test方法。
 *
 * 	2.当外部类-内部类-内部类方法 变量名相同时
 * 		 通过 外部类类名.this.varName 访问外部类实例变量
 * 		 通过 this.varName 访问内部类实例的变量
 *
 *  3.内部类中与外部类没有重命名变量，可直接访问外部变量名。
 *  	3.1如果内部类方法中定义的变量名称 与 外部类定义的名称相同，则访问的是 内部类方法定义的局部变量。
 *         也就是说：局部变量优先访问
 *
 * @author jiaolong
 * @date 2023-06-29 11:38:37
 */
public class NoStaticInnerClassUse {
    private String noRepeatVar ="外部类定义的 非重复 实例变量";
    private String prop = "外部类定义的 实例变量";

    private class InClass {
        private String prop = "内部类定义的 实例变量";

        public void info(){
            String prop = "内部类方法中定义的 局部变量";

            // 通过 外部类类名.this.varName 访问外部类实例变量
            System.out.println("外部类-内部类-内部类方法 变量名相同,调用外部类变量[DiscernVariable.this.prop]：" +
                    NoStaticInnerClassUse.this.prop);

            // 通过 this.varName 访问内部类实例的变量
            System.out.println("外部类-内部类-内部类方法 变量名相同,调用内部类变量[this.prop]：" + this.prop);
            // 直接访问局部变量
            System.out.println("外部类-内部类-内部类方法 变量名相同,调用内部类方法的局部变量[prop]：" + prop);

            System.out.println("\n变量名不重复："+noRepeatVar);
        }
    }

    public void test(){
        // 外部类不能直接访问非静态内部类的实例变量, prop
        // 如需访问内部类的实例变量，必须显式创建内部类对象
        InClass in = new InClass();
        in.info();
    }

    public static void main(String[] args){
        new NoStaticInnerClassUse().test();
    }
}
