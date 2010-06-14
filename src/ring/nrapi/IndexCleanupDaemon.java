package ring.nrapi;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Collection;

import org.xmldb.api.base.XMLDBException;

import ring.daemons.Daemon;
import ring.nrapi.business.BusinessObject;
import ring.persistence.XQuery;

public class IndexCleanupDaemon implements Daemon {
	private ReferenceQueue<BusinessObject> refQueue;
	private boolean halt;
	private ObjectIndex index;
	
	public IndexCleanupDaemon(ObjectIndex index) {
		this.index = index;
	}

	@Override
	public void start() throws IOException {
		setupShutdownHook();
		
		while (!halt) {
			try {
				Reference<? extends BusinessObject> boRef = refQueue.remove();
				BusinessObject bo = boRef.get();
				remove(bo);
			} 
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (XMLDBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	private void setupShutdownHook() {
		Runnable purge = new Runnable() {
			@Override
			public void run() {
				try {
					InputStream query = this.getClass().getResourceAsStream("/ring/nrapi/index_purge.xq");
					XQuery xq = new XQuery(query);
					xq.executeUpdate();
					System.out.println("Object Index purged.");
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (XMLDBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}	
		};
		
		Runtime.getRuntime().addShutdownHook(new Thread(purge));
	}

	private void remove(BusinessObject bo) throws IOException, XMLDBException {
		if (bo == null) {
			return;
		}
		
		synchronized (index) {
			BusinessObject bo2 = index.removeKey(bo.getUuid());
			if (bo2 == null) {
				//uh?
			}
			
			//Remove from database index document
			InputStream query = this.getClass().getResourceAsStream("/ring/nrapi/index_delete.xq");
			XQuery xq = new XQuery(query);
			xq.declareVariable("uuid", bo.getUuid());
			xq.executeUpdate();
		}
	}
	
	@Override
	public void stop() throws IOException {
		halt = true;
	}
	
	@Override
	public void halt() throws IOException {
		halt = true;		
	}
	
	public ReferenceQueue<BusinessObject> getReferenceQueue() {
		return refQueue;
	}
	
	public void setReferenceQueue(ReferenceQueue<BusinessObject> queue) {
		refQueue = queue;
	}
	
	public ObjectIndex getObjectIndex() {
		return index;
	}
	
	public void setObjectIndex(ObjectIndex index) {
		this.index = index;
	}
}
