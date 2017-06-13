import java.util.Random;

public class Case1 {
	public static void main(String[] args) throws InterruptedException {
		Data data = new Data();
		Fornecedor f1 = new Fornecedor(data);
		Consumidor c1 = new Consumidor(data);
		
		Thread t1 = new Thread(f1);
		Thread t2 = new Thread(c1);
		
		t1.start();
		t2.start();
		
		t1.join();
		t2.join();
	}

	static class Data {
		private int xpto;
		private boolean bufferLimpo = true;
		
		public synchronized int getXpto() throws InterruptedException {
			while(bufferLimpo) {
				wait();
			}
			int x = xpto;
			bufferLimpo = true;
			notifyAll();
			return x;
		}

		public synchronized void setXpto(int xpto) throws InterruptedException {
			while(!bufferLimpo) {
				wait();
			}
			this.xpto = xpto;
			System.out.println("mudei");
			bufferLimpo = false;
			notifyAll();
		}
	}

	static class Consumidor implements Runnable {
		private Data data;
		
		public Consumidor(Data data) {
			this.data = data;
		}

		@Override
		public void run() {
			while (true) {
				try {
					System.out.println(data.getXpto());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	static class Fornecedor implements Runnable {
		private Data data;
		private Random rand;
		public Fornecedor(Data data) {
			this.data = data;
			rand = new Random();
		}

		@Override
		public void run() {
			while (true) {
				try {
					data.setXpto(rand.nextInt(11));
					Thread.sleep(101);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}
}
