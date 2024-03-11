package org.example;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 线程协调，根据条件执行不同的并发任务。
 * @author jiaolong
 * @date 2023-01-19 03:28:18
 */
public class CountDownLatchSample {
	public static void main(String[] args) throws InterruptedException {
		//当CountDownLatch等与0时，所有等待的线程开始工作
		CountDownLatch latch = new CountDownLatch(5);
		//latch.countDown();//每调用一次，只当的并发值-1
		//latch.await();    //当等于0时结束结束等待，开始执行任务
		
		System.out.println("1- latch.getCount():"+latch.getCount());
		for (int i = 0; i < 5; i++) {
			Thread t = new Thread(new FirstBatchWorker(latch));
			t.start();
		}
		for (int i = 0; i < 5; i++) {
			Thread t = new Thread(new SecondBatchWorker(latch));
			t.start();
		}



	}
}

class FirstBatchWorker implements Runnable {
	private CountDownLatch latch;

	public FirstBatchWorker(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		//递减countDownLatch(闩锁)的计数，如果计数达到零，则释放所有等待的线程。
		latch.countDown();
		System.out.println("First batch executed! "+latch.getCount());
	}
}

class SecondBatchWorker implements Runnable {
	private CountDownLatch latch;

	public SecondBatchWorker(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
            //使当前线程等待，直到countDownLatch(闩锁)倒计时为零
			latch.await();
			TimeUnit.SECONDS.sleep(5);
			System.out.println("Second batch executed! "+latch.getCount());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
