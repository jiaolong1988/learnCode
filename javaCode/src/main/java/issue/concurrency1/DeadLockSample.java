package issue.concurrency1;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 死锁线程案例及使用Java提供的标准管理API，定位死锁位置
 * @author jiaolong
 * @date 2023-01-17 02:56:56
 */
public class DeadLockSample extends Thread {
	
	private String first;
	private String second;

	public DeadLockSample(String name, String first, String second) {
		super(name);
		this.first = first;
		this.second = second;
	}

	public void run() {
		synchronized (first) {
			System.out.println(this.getName() + " obtained: - first " + first);
			try {
				Thread.sleep(1000L);
				synchronized (second) {
					System.out.println(this.getName() + " obtained: - second " + second);
				}
			} catch (InterruptedException e) {
				// Do nothing
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		//1.扫描线程是否死锁
		ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
		Runnable dlCheck = new Runnable() {
			@Override
			public void run() {
				long[] threadIds = mbean.findDeadlockedThreads();
				if (threadIds != null) {
					ThreadInfo[] threadInfos = mbean.getThreadInfo(threadIds);
					System.out.println("Detected deadlock threads:");
					for (ThreadInfo threadInfo : threadInfos) {
						System.out.println(threadInfo.getThreadName());
					}
				}
			}
		};
		//稍等5秒，然后每 10 秒进行一次死锁扫描
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);		
		scheduler.scheduleAtFixedRate(dlCheck, 5L, 10L, TimeUnit.SECONDS);
		
		
		//2.执行死锁线程案例
		String lockA = "lockA";
		String lockB = "lockB";
		DeadLockSample t1 = new DeadLockSample("Thread1", lockA, lockB);
		DeadLockSample t2 = new DeadLockSample("Thread2", lockB, lockA);
		t1.start();
		t2.start();
		t1.join();
		t2.join();

		/**
		 * 执行结果
			Thread1 obtained: - first lockA
			Thread2 obtained: - first lockB
			Detected deadlock threads:
			Thread2
			Thread1
			Detected deadlock threads:
			Thread2
			Thread1
		 */
	}
}