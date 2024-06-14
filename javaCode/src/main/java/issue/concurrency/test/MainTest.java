package issue.concurrency.test;


import java.util.HashSet;
import java.util.Set;

public class MainTest {

	public static void main(String[] args) throws Exception {
		Set<String> result = new HashSet<>();
		
		Account a = new Account();
		Account b = new Account();
		Account c = new Account();
		
		for(int i=0;i<100000;i++) {
			
			Thread t1 = new Thread(new TestThread(a,b));
			Thread t2 = new Thread(new TestThread(b,c));
			t1.start();
			t2.start();

			t1.join();
			t2.join();
			
			if(b.getBalance()!=200) {
				String value = "a: "+a.getBalance()+"  b: "+b.getBalance()+" c: "+c.getBalance();
				result.add(value);
				
			}else {
				a.setBalance(200);
				b.setBalance(200);
				c.setBalance(200);
			}
			
		}
		
		System.out.println(result);

	}
	static class TestThread implements Runnable {
		Account thisAccount;
		Account targetAccount;
		
		TestThread(Account thisAccount1, Account targetAccount1){
			this.thisAccount = thisAccount1;
			this.targetAccount = targetAccount1;
		}
		
		@Override
		public void run() {
			
			try {
				int random= (int)((double)Math.random()*10);
				Thread.sleep(random);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			thisAccount.transfer(targetAccount, 100);
		}
		
	}
}
