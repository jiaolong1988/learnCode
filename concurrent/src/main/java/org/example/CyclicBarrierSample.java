package org.example;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 用于线程间的批量协调，也就是说一批一批的执行任务
 * 相等于批量执行相同的任务
 * @author jiaolong
 * @date 2023-01-19 03:56:21
 */
public class CyclicBarrierSample {
	public static void main(String[] args) throws Exception {
		//障碍触发前,调用await的线程数量
		int parties = 5;
		CyclicBarrier barrier = new CyclicBarrier(parties, new Runnable() {
			@Override
			public void run() {
				System.out.println("\n任务执行前的环境检查。。");
			}
		});
		
		for (int i = 0; i < 5; i++) {
			Thread t = new Thread(new CyclicWorker(barrier));
			t.start();
		}
	}

	static class CyclicWorker implements Runnable {
		private CyclicBarrier barrier;

		public CyclicWorker(CyclicBarrier barrier) {
			this.barrier = barrier;
		}

		@Override
		public void run() {
			try {
				for (int i = 0; i < 3; i++) {
					/**
					 * 1.await()方法阻塞线程。
					 * 2.当调用await()的数量 等于 当CyclicBarrier指定的parties数量时，开始批量执行任务
					 * 3.await()返回的是 当前线程的到达索引，既getParty() - 1 ，零表示最后一个到达，开始执行任务返回索引值.
					  **/				 
					int arrivalIndex =  barrier.await();
					//开始执行具体任务
					System.out.println("--->"+Thread.currentThread().getName()+" 开始执行具体任务"+i+"  序号："+arrivalIndex);
					System.out.println("			..."+Thread.currentThread().getName());
				}
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}