package ring.daemons;

import java.io.IOException;

/**
 * Launch a daemon as a thread.
 * @author projectmoon
 *
 */
public class DaemonThread {
	private final Daemon daemon;
	private Thread daemonThread;
	
	public DaemonThread(Daemon daemon) {
		this.daemon = daemon;
	}
	
	public void start() {
		Runnable runDaemon = new Runnable() {
			@Override
			public void run() {
				try {
					daemon.start();
				} 
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		};
		
		daemonThread = new Thread(runDaemon);
		daemonThread.start();
	}
	
	public Thread getThread() {
		return daemonThread;
	}
	
	public Daemon getDaemon() {
		return daemon;
	}
	
	public void join() throws IOException, InterruptedException {
		daemon.stop();
		daemonThread.join();
	}
}
