package thread.define;

import java.util.concurrent.*;

public class ThirdThread {
	public static void main(String[] args){
		// 先使用Lambda表达式创建Callable<Integer>对象
		// 使用FutureTask来包装Callable对象
		FutureTask<Integer> task = new FutureTask<Integer>((Callable<Integer>)() -> {
			int i = 0;
			for ( ; i < 100 ; i++ ) {
				System.out.println(Thread.currentThread().getName() + " 的循环变量i的值：" + i);
				TimeUnit.MILLISECONDS.sleep(100);
			}
			// call()方法可以有返回值
			return i;
		});

		//t1的状态：TIMED_WAITING
		Thread t1 = new Thread(task);
		t1.start();
		System.out.println("启动一个线程：t1");

		//t2的状态：TERMINATED,启动的时候就终止了。
		Thread t2 = new Thread(task);
		t2.start();
		System.out.println("启动一个线程：t2");

		for(int i = 0 ; i < 100 ; i++) {
			System.out.println(i+"_t1的状态："+t1.getState());
			System.out.println(i+"_t2的状态："+t2.getState());
		}

		try {
			// 获取线程返回值
			System.out.println("==>线程返回值：" + task.get());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}

