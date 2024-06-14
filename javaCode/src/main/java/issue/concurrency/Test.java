package issue.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//class Student{
//	private String studentName="studentName";
//}

public class Test {
	private String testName="testName";

	 
	public static void main(String[] args) throws Exception {
		
//	        Class clazz = User.class;
//	        User u = new User();
//	        u.setName("jl");
//
//	        
//	        Field ff = clazz.getDeclaredField("name");	        
//            System.out.println(ff.isAccessible());//这里的结果是false
//            
//            ff.setAccessible(true);            
//	        System.out.println(ff.get(u));
	        

//		
//		//获取自身对象
//		Class<Test> tc = Test.class;
//		Field tcField = tc.getDeclaredField("testName");
//		System.out.println(tcField.isAccessible());
//		tcField.setAccessible(true);
//		
//		Test tn = new Test();
//		System.out.println("====>"+tcField.get(tn));
//		
		
	    //反射name字段是否可以访问
//	    Class<?> studentClass = Student.class;	    
//	    Student student = new Student();
		
//		Field studentClassField = studentClass.getDeclaredField("studentName");
//		System.out.println(studentClassField.isAccessible());
//		studentClassField.setAccessible(true);
//		System.out.println("====>"+studentClassField.get(student));
		
//		for (Field field : studentClass.getDeclaredFields()) {
//			System.out.println(field.isAccessible());// 这里的结果是false
//			field.setAccessible(true);
//			
//			System.out.println("字段名称："+field.getName());
//			System.out.println("获取对象字段的值："+field.get(student));
//		}
		
		
//		AtomicInteger ai = new AtomicInteger(60);
//		System.out.println(ai.compareAndSet(60, 2022)+"--- \t"+ai.get() );
//
//		System.out.println("\t"+ai.get() );
//		System.out.println(ai.compareAndSet(5, 2022)+"nnn\t"+ai.get() );
//		
	
		//锁
//		Lock lock = new ReentrantLock();
//		lock.lock();
//		//信号量
//		Semaphore s = new Semaphore(0);	
//		//System.out.println(s.tryAcquire());
//		s.acquireUninterruptibly();
//		System.out.println("ddddddddddddddd");
//		s.release();
	
		
		//锁实现
		AbstractQueuedSynchronizer aqs;
		
		ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
		
		//数量向下⻔闩
		CountDownLatch countDownLatch = new CountDownLatch(2);
		//回环屏障
		CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
		
		//线程池创建
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		
		CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
			 try {
			 Thread.sleep(3*1000);
			 } catch (InterruptedException e) {
			 throw new IllegalStateException(e);
			 }
			 System.out.println("运⾏在⼀个单独的线程当中-没有返回值");
			});
		
		future.get();
	
		
		CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
			 try {
				 Thread.sleep(3*1000);
			 } catch (InterruptedException e) {
			 throw new IllegalStateException(e);
			 }
			 System.out.println("运⾏在⼀个单独的线程当中");
			 return "我有返回值";
			 });
		 System.out.println(future1.get());
	}

}
