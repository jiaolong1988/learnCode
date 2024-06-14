package issue.concurrency.test;

class Account {
	private int balance = 200;
	// private final Object lock = new Object();
	
	// private final static Object lock = new Object();
	private Class<Account> lock = Account.class;

	// 转账
	void transfer(Account target, int amt) {
		synchronized (lock) {
			if (this.balance > amt) {
				this.balance -= amt;
				target.balance += amt;
			}
		}
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

}