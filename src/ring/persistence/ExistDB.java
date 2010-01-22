package ring.persistence;

import java.io.PrintStream;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XQueryService;

import ring.system.MUDConfig;

/**
 * Class used to access the eXist XND. Provides a cleaner method of querying the database.
 * @author projectmoon
 *
 */
public class ExistDB {
	
	//Constants mapping to various strings necessary for XML:DB API.
	//URI mappings
	private static final String DB_URI = MUDConfig.getDatabaseURI();
	
	//User mappings
	private static final String DB_USER = MUDConfig.getDatabaseUser();
	private static final String DB_PASSWORD = MUDConfig.getDatabasePassword();
	
	//Collection mappings
	private static final String ROOT_COLLECTION = "db";
	
	//Service mappings
	private static final int SVCNAME = 0;
	private static final int SVCVER = 1;
	private static final String[] XQUERY_SERVICE = { "XQueryService", "1.0" };
	private static final String[] COLLECTION_MGMT_SERVICE = { "CollectionManagementService", "1.0" };
	
	//Service caches: Not necessary right now
	//private static Map<Collection, XQueryService> xqServiceCache = new WeakHashMap<Collection, XQueryService>();
	
	//Other stuff
	//private static boolean shutdownHookExists;
	//private static boolean shutdown = false;
	
	static {
		//System.setProperty("exist.initdb", "true");
		//System.setProperty("exist.home", "/etc/ringmud/");
		
		try {
			Class<?> cl = Class.forName("org.exist.xmldb.DatabaseImpl");
			Database xmlDB = (Database)cl.newInstance();
			xmlDB.setProperty("create-database", "true");
			DatabaseManager.registerDatabase(xmlDB);
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (XMLDBException e) {
			System.err.println("XMLDB Error:" + e.getMessage());
			System.err.println("Is the XML database running?");
			System.exit(1);
		} 
		catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			System.err.println("lol broke" + e);
			System.exit(1);
		}
	
		//Hook doesn't exist until the constructor is first called.
		//shutdownHookExists = false;
	}
	
	public ExistDB() {
		/*
		//Add a shutdown hook for the root collection only, if it's not already there
		if (!shutdownHookExists) {
			try {
				setupShutdownHook(getRootCollection());
			} catch (XMLDBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
	}
	
	/*
	public void shutdown() {
		System.out.print("Shutting down eXist... ");
		try {
			DatabaseInstanceManager manager = (DatabaseInstanceManager)getRootCollection().getService("DatabaseInstanceManager", "1.0"); 
			manager.shutdown();
		}
		catch (XMLDBException e) {
			System.err.println("There was an error shutting down eXist:");
			e.printStackTrace();
		}
		
		shutdown = true;
		System.out.println("Done.");
	}
	*/
	
	/*
	//This will come back later when support for embedded DBs is reintroduced.
	private void setupShutdownHook(final Collection col) {
		Runnable hook = new Runnable() {
			@Override
			public void run() {
				//This only runs in the event of an abnormal shutdown.
				if (!shutdown) {
					shutdown();
				}
			}
		};
		
		Runtime.getRuntime().addShutdownHook(new Thread(hook));
		shutdownHookExists = true;
	}
	*/
	
	public XQueryService getXQueryService(Collection col) throws XMLDBException {
		return (XQueryService)col.getService(XQUERY_SERVICE[SVCNAME], XQUERY_SERVICE[SVCVER]);
	}
	
	public Collection getRootCollection() throws XMLDBException {
		Collection root = (Collection)DatabaseManager.getCollection(craftCollectionURI(ROOT_COLLECTION), DB_USER, DB_PASSWORD);
		return root;
	}
	
	public Collection getCollection(String name) throws XMLDBException {
		String colName = ROOT_COLLECTION + "/" + name;
		Collection col = (Collection)DatabaseManager.getCollection(craftCollectionURI(colName), DB_USER, DB_PASSWORD);
		
		if (col == null) {
			throw new XMLDBException(-1, "Collection " + colName + " is null. Is the database running?");
		}
		
		return col;
	}
	
	private static String craftCollectionURI(String name) {
		String ret = DB_URI;
		
		if (!DB_URI.endsWith("/")) {
			ret += "/";
		}
		
		return ret + name;
	}
	
	public void removeAllResources() {
		try {
			Collection root = getRootCollection();
			CollectionManagementService service = (CollectionManagementService)root.getService(
					COLLECTION_MGMT_SERVICE[SVCNAME], COLLECTION_MGMT_SERVICE[SVCVER]);
			
			//Drop all collections.
			service.removeCollection(ExistDBStore.STATIC_COLLECTION);
			service.removeCollection(ExistDBStore.GAME_COLLECTION);
			service.removeCollection(ExistDBStore.PLAYERS_COLLECTION);
		}
		catch (XMLDBException e) {
			System.err.println("DB Warning: was unable to remove all collections");
		}
	}
	
	public XMLResource querySingleResource(String xquery) throws XMLDBException {
		Collection col = getRootCollection();
		System.out.println("Query: " + xquery);
		XQueryService service = getXQueryService(col);
		if (service != null) {
			ResourceSet res = service.query(xquery);
			System.out.println("Resource size: " + res.getSize());
			ResourceIterator i = res.getIterator();
			
			while (i.hasMoreResources()) {
				Resource r = i.nextResource();
				if (r instanceof XMLResource) {
					return (XMLResource)r;
				}
			}
		}
		
		//Nothing found, return null.
		return null;
	}
	
	public void testQuery(String xquery) throws XMLDBException {
		Collection col = getRootCollection();
		System.out.println("Query: " + xquery);
		XQueryService service = getXQueryService(col);
		if (service != null) {
			ResourceSet res = service.query(xquery);
			System.out.println("Resource size: " + res.getSize());
			ResourceIterator i = res.getIterator();
			
			while (i.hasMoreResources()) {
				System.out.println("------------------");
				System.out.println(i.nextResource().getContent());
				System.out.println("------------------");
			}
		}
	}

	public void addRootNode(String rootNode) throws XMLDBException {
		Collection col = getRootCollection();
		XMLResource res = (XMLResource)col.createResource(null, "XMLResource");
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		xml += "<" + rootNode + "></" + rootNode + ">";
		res.setContent(xml);
		col.storeResource(res);
	}
	
	public void addRootNode(Collection col, String rootNode) throws XMLDBException {
		XMLResource res = (XMLResource)col.createResource("root", "XMLResource");
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		xml += "<" + rootNode + "></" + rootNode + ">";
		res.setContent(xml);
		col.storeResource(res);
	}
	
	/**
	 * Creates a new database by setting up collections. Doesn't check
	 * if one already exists... yet.
	 */
	public void createRingDatabase() throws XMLDBException {
		Collection root = getRootCollection();
		CollectionManagementService service = (CollectionManagementService)root.getService(
				COLLECTION_MGMT_SERVICE[SVCNAME], COLLECTION_MGMT_SERVICE[SVCVER]);
		
		//Static content: loaded during server boot
		Collection staticCol = service.createCollection(ExistDBStore.STATIC_COLLECTION);
		addRootNode(staticCol, "ring");
		
		//Game collection: Stores world state
		Collection gameCol = service.createCollection(ExistDBStore.GAME_COLLECTION);
		addRootNode(gameCol, "ring");
		
		//Players collection: Stores player information. 
		Collection playersCol = service.createCollection(ExistDBStore.PLAYERS_COLLECTION);
		addRootNode(playersCol, "ring");
	}
	
	public void listResources(PrintStream out) {
		try {
			Collection col = getCollection(ExistDBStore.STATIC_COLLECTION);
			
			out.println("Static Collection:");
			String[] res = col.listResources();
			for (String r : res) {
				out.println(r);
			}
			
			col = getCollection(ExistDBStore.GAME_COLLECTION);
			
			out.println("Game Collection:");
			res = col.listResources();
			for (String r : res) {
				out.println(r);
			}
		}
		catch (XMLDBException e) {
			e.printStackTrace();
		}
		
		
	}
}
