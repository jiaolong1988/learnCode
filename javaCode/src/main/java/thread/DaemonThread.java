package thread;

/**
 * �ػ��̣߳�Daemon Thread����Ҫ������Ϊ�����߳��ṩ���񣬲��ں�̨ĬĬ����.(���������߳̽��й���)
 * ��Ҫ���ԣ��� JVM �����еķ��ػ��̣߳��û��̣߳�������ʱ�������ػ��߳��Ƿ������У�JVM �����Զ��˳���
 * 			����ζ���ػ��̲߳�Ӧ��ִ�йؼ�������Ϊ���ǿ��ܻ����κ�ʱ����ֹ.
 *
 *
 * @author jiaolong
 * @date 2024-6-14 17:16
 */
public class DaemonThread extends Thread
{
	// �����̨�̵߳��߳�ִ��������ͨ�߳�û���κ�����
	public void run()
	{
		for (int i = 0; i < 1000 ; i++ )
		{
			System.out.println(getName() + "  " + i);
		}
	}
	public static void main(String[] args)
	{
		DaemonThread t = new DaemonThread();
		// �����߳����óɺ�̨�߳�
		t.setDaemon(true);
		// ������̨�߳�
		t.start();
		for (int i = 0 ; i < 10 ; i++ )
		{
			System.out.println(Thread.currentThread().getName() + " ---------> " + i);
		}
		// -----����ִ�е��˴���ǰ̨�̣߳�main�̣߳�����------
		// ��̨�߳�ҲӦ����֮����
	}
}
