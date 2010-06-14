package ring.nrapi;

import java.io.IOException;
import java.lang.ref.ReferenceQueue;

import ring.daemons.DaemonThread;
import ring.nrapi.business.BusinessObject;

/**
 * Class that contains methods for starting and stopping the Object Index System.
 * @author projectmoon
 *
 */
public class ObjectIndexSystem {
	private static ObjectIndex index;
	private static DaemonThread daemonThread;
	private static IndexCleanupDaemon cleanupDaemon;
	private static boolean started;
	
	public static void start() {
		ReferenceQueue<BusinessObject> refQueue = new ReferenceQueue<BusinessObject>();
		
		index = new ObjectIndex();
		index.setReferenceQueue(refQueue);
		
		cleanupDaemon = new IndexCleanupDaemon(index);
		cleanupDaemon.setReferenceQueue(refQueue);
		
		daemonThread = new DaemonThread(cleanupDaemon);
		daemonThread.start();
		
		started = true;
	}
	
	public static void stop() throws InterruptedException, IOException {
		daemonThread.join();
	}
	
	public static boolean isStarted() {
		return started;
	}
	
	public static ObjectIndex getObjectIndex() {
		return index;
	}
	
	public static ObjectSearch newSearch() {
		ObjectSearch search = new ObjectSearch();
		search.setObjectIndex(index);
		return search;
	}
}
