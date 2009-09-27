package ring.commands.nc;

import java.util.Properties;

public class IndexerFactory {
	public static CommandIndexer getIndexer(String indexer, Properties props) {
		try {
			Class<?> c = Class.forName(indexer);
			CommandIndexer cmdIndexer = (CommandIndexer)c.newInstance();
			cmdIndexer.setProperties(props);
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
}
