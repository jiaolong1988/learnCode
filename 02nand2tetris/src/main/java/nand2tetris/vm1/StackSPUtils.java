package nand2tetris.vm1;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jiaolong
 * @date 2024/01/23 17:42
 * @description: TODO
 * 栈指针操作
 */
public class StackSPUtils {

    public static AtomicInteger ramNUm = new AtomicInteger(PubConst.STACK_SP_ADDRESS);
    //重置
    public static void reset(){
        ramNUm.set(PubConst.STACK_SP_ADDRESS);
    }

    //获取当前值
    public static int get(){
       return ramNUm.get();
    }

    //加1
    public static void addOne(){
        ramNUm.incrementAndGet();
    }

    //减1
    public static void decrementOne(){
        ramNUm.decrementAndGet();
    }


    /**
     * 汇编标签 唯一序号
     * @author jiaolong
     * @date 2024-1-25 17:43
     */
    public final static class LableIndex{
        //标签指针
        private static AtomicInteger assemblerLable = new AtomicInteger(-1);
        //获取汇编代码标签序号
        public static int getAssemblerLableIndex(){
            return assemblerLable.incrementAndGet();
        }
        //重置汇编代码标签序号
        public static void AssemblerLablReset(){
            ramNUm.set(-1);
        }
    }

}
