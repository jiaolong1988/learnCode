package issue.concurrency1;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class LongRunningAction implements Runnable {
    private String threadName;
    private Phaser ph;

    LongRunningAction(String threadName, Phaser ph) {
        this.threadName = threadName;
        this.ph = ph;
        ph.register();
    }

    @Override
    public void run() {
        ph.arriveAndAwaitAdvance();
        System.out.println("run:"+threadName);
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ph.arriveAndDeregister();
    }
    
    public static void main(String[] args) {
    	ExecutorService executorService = Executors.newCachedThreadPool();  	
    	Phaser ph = new Phaser(1);  	
    	System.out.println("0 is :"+ph.getPhase());
    	
    	executorService.submit(new LongRunningAction("thread-1", ph));
    	executorService.submit(new LongRunningAction("thread-2", ph));
    	executorService.submit(new LongRunningAction("thread-3", ph));
    	ph.arriveAndAwaitAdvance();
    	System.out.println("1 is :"+ph.getPhase());
    	
    	executorService.submit(new LongRunningAction("thread-4", ph));
    	executorService.submit(new LongRunningAction("thread-5", ph));
    	ph.arriveAndAwaitAdvance();
    	 
    	System.out.println("2 is :"+ ph.getPhase());

    	ph.arriveAndDeregister();
    	
    	
	}
}