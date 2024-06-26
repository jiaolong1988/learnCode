package thread;

/**
 * 守护线程（Daemon Thread）主要作用是为其他线程提供服务，并在后台默默运行.(辅助其他线程进行工作)
 * 重要特性：当 JVM 中所有的非守护线程（用户线程）都结束时，无论守护线程是否还在运行，JVM 都会自动退出。
 * 			这意味着守护线程不应该执行关键任务，因为它们可能会在任何时候被终止.
 *
 *
 * @author jiaolong
 * @date 2024-6-14 17:16
 */
public class DaemonThread extends Thread {
	// 定义后台线程的线程执行体与普通线程没有任何区别
	public void run() {
		for (int i = 0; i < 1000 ; i++ ){
			System.out.println(getName() + "  " + i);
		}
	}
	public static void main(String[] args) {
		DaemonThread t = new DaemonThread();
		// 将此线程设置成后台线程
		t.setDaemon(true);
		// 启动后台线程
		t.start();
		for (int i = 0 ; i < 10 ; i++ ){
			System.out.println(Thread.currentThread().getName() + " ---------> " + i);
		}

		// -----程序执行到此处，前台线程（main线程）结束------
		// 守护线程也随之结束
	}
}
