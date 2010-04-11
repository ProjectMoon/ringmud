package ring.commands;

import java.util.Properties;

/**
 * Simple factory class that can reflectively create CommandIndexers.
 * @author projectmoon
 *
 */
public class IndexerFactory {
	/**
	 * Gets a CommandIndexer referenced by the implementing class, passed paramter "indexer".
	 * The properties passed in are forwarded on to the CommandIndexer.
	 * @param indexer The fully-qualified class name of the indexer to create.
	 * @param props Properties for this indexer.
	 * @return A CommandIndexer, or null if there is an error encountered creating one.
	 */
	public static CommandIndexer getIndexer(String indexer, Properties props) {
		try {
			Class<?> c = Class.forName(indexer);
			CommandIndexer cmdIndexer = (CommandIndexer)c.newInstance();
			if (props != null) {
				cmdIndexer.setProperties(props);
			}
			return cmdIndexer;
		} 
		catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static CommandIndexer getIndexer(String indexer) {
		return getIndexer(indexer, null);
	}
}
