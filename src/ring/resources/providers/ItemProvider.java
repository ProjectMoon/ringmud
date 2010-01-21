package ring.resources.providers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import ring.jox.BeanParser;
import ring.resources.beans.ItemBean;
import ring.resources.beans.ItemBeanSet;
import ring.system.MUDBootException;
import ring.system.MUDConfig;
import ring.system.XMLFileNameFilter;

public class ItemProvider implements RingBeanProvider<ItemBean> {

	private static final Logger log = Logger.getLogger(ItemProvider.class.getName());

	public Map<String, ItemBean> loadBeans() {
		HashMap <String, ItemBean> beans = new HashMap<String, ItemBean>();
		ItemBeanSet beanSet = getAllItems();
		
		for (ItemBean bean : beanSet.getItem()) {
			beans.put(bean.getID(), bean);
		}
		
		return beans;
	}
	
	/**
	 * Helper method to get large RoomSet of all RoomBeans.
	 * @return
	 */
    private ItemBeanSet getAllItems() {
        String[] itemSetFiles = MUDConfig.getItemFiles();
        ItemBeanSet itemBeans = new ItemBeanSet();
        
        for (String filePath : itemSetFiles) {
        	ItemBeanSet set = constructFromFiles(filePath);
        	log.info("Processed item set " + set);
        	if (set != null) {
        		itemBeans.copyFrom(set);
			}
        }
        
        return itemBeans;
    }
	
    /**
     * Helper method that gets a set of RoomBeans from all xml files in a directory containing zone files.
     * @param directory
     * @return
     */
	private ItemBeanSet constructFromFiles(String directory) {
    	File dir = new File(directory);
    	ItemBeanSet set = new ItemBeanSet();
    	
    	if (dir.exists()) {
			if (dir.isDirectory() == false) {
				throw new MUDBootException("Data files should only be specified by directory, not absolute files.");
			}
			else {
				File[] dataFiles = dir.listFiles(new XMLFileNameFilter());
				
				for (File dataFile : dataFiles) {
					log.fine("Processing ItemSet " + dataFile);
					try {
						FileInputStream stream = new FileInputStream(dataFile);
						BeanParser<ItemBeanSet> roomParser = new BeanParser<ItemBeanSet>();
						ItemBeanSet fileSet = roomParser.parse(stream, ItemBeanSet.class);
						set.copyFrom(fileSet);
					} 
					catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}			
				}
				
				return set;
			}
		}
		else {
			System.err.println("Directory does not exist: " + dir);
			return null;
		}
    }	

}
