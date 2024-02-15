package nand2tetris.vm1;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jiaolong
 * @date 2024/01/23 17:42
 * @description: TODO
 * ջָ�����
 */
public class StackSPUtils {

    public static AtomicInteger ramNUm = new AtomicInteger(PubConst.STACK_SP_ADDRESS);
    //����
    public static void reset(){
        ramNUm.set(PubConst.STACK_SP_ADDRESS);
    }

    //��ȡ��ǰֵ
    public static int get(){
       return ramNUm.get();
    }

    //��1
    public static void addOne(){
        ramNUm.incrementAndGet();
    }

    //��1
    public static void decrementOne(){
        ramNUm.decrementAndGet();
    }


    /**
     * ����ǩ Ψһ���
     * @author jiaolong
     * @date 2024-1-25 17:43
     */
    public final static class LableIndex{
        //��ǩָ��
        private static AtomicInteger assemblerLable = new AtomicInteger(-1);
        //��ȡ�������ǩ���
        public static int getAssemblerLableIndex(){
            return assemblerLable.incrementAndGet();
        }
        //���û������ǩ���
        public static void AssemblerLablReset(){
            ramNUm.set(-1);
        }
    }

}
