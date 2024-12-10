package lambda.baseuse;

/**
 *  3-方法应用与泛型
 *  方法引用：与lambad表达式作用一致，实现接口功能。
 *  语法：
 *      类名::方法名
 */
public class LambdaSyntax3 {
    public static void main(String[] args) {
        // Uses a lambda expression
        JlFunction<Integer, Integer, Integer> func1 = (x, y) -> Integer.sum(x, y);
        System.out.println(func1.apply(2, 3));

        // Uses a method reference
        JlFunction<Integer, Integer, Integer> func2 = Integer::sum;
        System.out.println(func2.apply(2, 3));
    }
}

interface JlFunction<T, U, R>{
   R apply(T t, U u);
}
