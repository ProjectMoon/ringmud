package ring.resources.providers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import ring.jox.BeanParser;
import ring.resources.beans.PortalBean;
import ring.resources.beans.RoomBean;
import ring.resources.beans.RoomBeanSet;
import ring.system.MUDBootException;
import ring.system.MUDConfig;
import ring.system.XMLFileNameFilter;

public class RoomProvider implements RingBeanProvider<RoomBean> {
	private static final Logger log = Logger.getLogger(RoomProvider.class.getName());
	
	/**
	 * Loads RoomBeans and forwards PortalBeans on to the PortalProvider.
	 */
	public Map<String, RoomBean> loadBeans() {
		HashMap<String, RoomBean> beans = new HashMap<String, RoomBean>();
		RoomBeanSet beanSet = getUniverse();
		
		for (RoomBean bean : beanSet.getRoom()) {
			beans.put(bean.getID(), bean);
			
			for (PortalBean portalBean : bean.getExit()) {
				portalBean.setID(bean.getID() + "." + portalBean.getDirection());
			}
		}
		
		return beans;
	}
	
	/**
	 * Helper method to get large RoomSet of all RoomBeans.
	 * @return
	 */
    private RoomBeanSet getUniverse() {
        String[] roomSetFiles = MUDConfig.getRoomSetFiles();
        RoomBeanSet universe = new RoomBeanSet();
        universe.setName("Universe");
        for (String filePath : roomSetFiles) {
        	RoomBeanSet set = constructFromFiles(filePath);
        	log.info("Processed globe " + set);
        	universe.copyFrom(set);
        }
        
        return universe;
    }
	
    /**
     * Helper method that gets a set of RoomBeans from all xml files in a directory containing zone files.
     * @param directory
     * @return
     */
	private RoomBeanSet constructFromFiles(String directory) {
    	File dir = new File(directory);
    	RoomBeanSet globe = new RoomBeanSet();
    	globe.setName(directory);
    	if (dir.isDirectory() == false) {
    		throw new MUDBootException("Data files should only be specified by directory, not absolute files.");
    	}
    	else {
    		File[] dataFiles = dir.listFiles(new XMLFileNameFilter());
    		
    		for (File dataFile : dataFiles) {
    			log.info("Processing RoomSet " + dataFile);
    			try {
					FileInputStream stream = new FileInputStream(dataFile);
					BeanParser<RoomBeanSet> roomParser = new BeanParser<RoomBeanSet>();
	    			RoomBeanSet set = roomParser.parse(stream, RoomBeanSet.class);
	    			globe.copyFrom(set);
				} 
    			catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
    		}
    		
    		return globe;
    	}
    }

}
