package jvm.know;

/**
 * 逃逸分析（Escape Analysis）是判断一个对象是否被外部方法引用或外部线程访问的分析技术，编译器会根据逃逸分析的结果对代码进行优化。
 * @author: jiaolong
 * @date: 2024/08/23 17:34
 **/
public class EscapeAnalysisTest {
    public static void main(String[] args) {
        /* VM 参数：效果是一样的
           -Xmx1000m -Xms1000m -XX:-DoEscapeAnalysis -XX:+PrintGC
           -Xmx1000m -Xms1000m -XX:+DoEscapeAnalysis -XX:+PrintGC
        */
        for (int i = 0; i < 200000 ; i++) {
            getAge();
        }
    }
    public static int getAge(){
        Student person = new Student(" 小明 ",18);
        return person.getAge();
    }
}
 class Student {
    private String name;
    private int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}