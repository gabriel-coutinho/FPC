import java.util.Random;

public class Case1 {
	public static void main(String[] args) {
		Data data = new Data();
		Fornecedor f1 = new Fornecedor(data);
		Consumidor c1 = new Consumidor(data);
		
		Thread t1 = new Thread(f1);
		Thread t2 = new Thread(c1);
		
		t1.start();
		t2.start();
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
		public Fornecedor(Data data) {
			this.data = data;
		}
		Random rand = new Random();

		@Override
		public void run() {
			int i = 0;
			while (true) {
				try {
					data.setXpto(i++);
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}
}
