package lambda;

import java.util.function.Consumer;

/**
 * 1-函数接口定义案例
 * https://mp.weixin.qq.com/s/3V_GtJjna5b8wy5GcWnQ0g
 * <p>
 * Function接口 消灭if...else
 *
 * @author jiaolong
 */
public class FunctionIfReplace {
    public static void main(String[] args) {

        // 1.处理抛出异常的if
        VUtils.isTure(false).throwMessage("11111111111");

        // 2.处理if分支操作
        VUtils.isTureOrFalse(false).trueOrFalseHandle(
        //第一个参数
        () -> {
            System.out.println("true , statr...");
        } ,
        //第二个参数
        () -> {
            System.out.println("false, end...");
        });

        // 3.如果存在值执行消费操作，否则执行基于空的操作
        VUtils.isBlankOrNoBlank("有字符").presentOrElseHandle(
            //第一个参数
            System.out::println
            ,
            //第二个参数
            () -> {
                System.out.println("空字符串");
            }
        );

    }
}

@FunctionalInterface
interface ThrowExceptionFunction {
    /**
     * 抛出异常信息
     *
     * @param message 异常信息
     * @return void
     **/
    void throwMessage(String message);
}

@FunctionalInterface
interface BranchHandle {
    /**
     * 分支操作
     *
     * @param trueHandle  为true时要进行的操作
     * @param falseHandle 为false时要进行的操作
     * @return void
     **/
    void trueOrFalseHandle(Runnable trueHandle, Runnable falseHandle);
}

interface PresentOrElseHandler<T extends Object> {

    /**
     * 值不为空时执行消费操作 值为空时执行其他的操作
     *
     * @param action      值不为空时，执行的消费操作
     * @param emptyAction 值为空时，执行的操作
     * @return void
     **/
    void presentOrElseHandle(Consumer<? super T> action, Runnable emptyAction);

}

class VUtils {

    public static ThrowExceptionFunction isTure(boolean b) {

        ThrowExceptionFunction tef = (errorMessage) -> {
            if (b) {
                throw new RuntimeException(errorMessage);
            }
        };

        return tef;
    }

    public static BranchHandle isTureOrFalse(boolean b) {
        return (trueHandle, falseHandle) -> {
            if (b) {
                trueHandle.run();
            } else {
                falseHandle.run();
            }
        };
    }

    public static PresentOrElseHandler<?> isBlankOrNoBlank(String str) {

        return (consumer, runnable) -> {
            if (str == null || str.length() == 0) {
                runnable.run();
            } else {
                consumer.accept(str);
            }
        };
    }

}
