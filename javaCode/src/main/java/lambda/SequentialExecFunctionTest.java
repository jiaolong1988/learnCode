package lambda;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 4-定义任务执行顺序-带参数与带返回结果
 * 定义任务执行顺序.
 * @author jiaolong
 * @date 2023-03-01 03:42:16
 */
public class SequentialExecFunctionTest {

    public static void main(String[] args) {
		/*
			f ... 接收参数：1 返回参数为:3
			g ... 接收参数: 3 返回结果：6
			andThen 输入参数为：1， 返回结果为: 6
		*/
        //第一个任务接收指定参数，前一个任务的返回结果是下一个任务的输入参数。
        functionTest();

        System.out.println("===========");
		/*
			f1 n-->接收相同的参数
			f2 n-->接收相同的参数
			接收相同的参数-F2
			===========
		*/
        // consumer接收相同的参数，执行不同的任务，每个任务没有返回结果
        ConsumerTest();
    }

    /**
     * function 第一个任务接收指定参数，前一个任务的返回结果是下一个任务的输入参数。
     * @return: void
     **/
    public static void functionTest() {
        Function<Integer, Integer> f = s -> {
            System.out.println("f ... 接收参数："+s+" 返回参数为:3");
            return 3;
        };


        Function<Integer, String> g = s -> {
            String res = String.valueOf(s*2);
            System.out.println("g ... 接收参数: "+s +" 返回结果："+res);

            return res;
        };

        //注意返回的是String类型，不是Integer
        String calculatingResult = f.andThen(g).apply(1);
        System.out.println("andThen 输入参数为：1， 返回结果为: "+calculatingResult);

    }


    /**
     * consumer接收相同的参数，执行不同的任务，每个任务没有返回结果
     */
    public static void ConsumerTest() {

        //	Consumer<String> f1 = System.out::println;
        //定义两个操作内容
        Consumer<String> f1 = n->{
            System.out.println("f1 n-->" + n);
            //无法修改参数内容
            n="xxx";
        };
        Consumer<String> f2 = n -> {
            System.out.println("f2 n-->" + n);
            System.out.println(n + "-F2");
        };

        //执行完f1后再执行f2的Accept方法
        f1.andThen(f2).accept("接收相同的参数");

        System.out.println("===========");

        //连续执行F的Accept方法
        // f1.andThen(f2).andThen(f1).andThen(f2).accept("test1");
    }

}
