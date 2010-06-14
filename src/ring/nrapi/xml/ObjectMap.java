package ring.nrapi.xml;

public class ObjectMap {	
	public static void main(String[] args) {
		RefQueueThread t = new RefQueueThread(null);
		synchronized(t) {
			t.addStuffToQueue();
		}
	}
	
}

class RefQueueThread implements Runnable {
	private boolean stuffInQueue;
	private ObjectMap test;
	
	public RefQueueThread(ObjectMap test) {
		this.test = test;
	}
	
	@Override
	public synchronized void run() {
		while (true) {
			while (!stuffInQueue) {
				try {
					wait();	
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			removeStuffFromQueue();
		}
		
	}
	
	public synchronized void addStuffToQueue() {
		System.out.println("adding stuff");
	}
	
	public synchronized void removeStuffFromQueue() {
		System.out.println("Removing stuff from queue");
		
		try {
			synchronized (test) {
				test.wait();
			}
				System.out.println("Deleting from DB");
				test.notify();			
		}
		catch (Exception e) {}
		//catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
		
		stuffInQueue = false;
	}
}

