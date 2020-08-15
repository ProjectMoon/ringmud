package ring.events;

import ring.deployer.DeployedMUDFactory;
import ring.nrapi.business.BusinessObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Loads events at MUD startup.
 * @author projectmoon
 *
 */
public class EventLoader {
	/**
	 * Quick business object class that wraps around the XML fragments
	 * returned by the eventloader.xq file. We need to do this so we can
	 * make use of eXist's <code>util:document-name</code> function in order
	 * to get the document names and the codebehind attribute. Otherwise, we
	 * would have to operate off eXist's IDs (which can be wrong for our purposes),
	 * or return more XML than we need.
	 * @author projectmoon
	 *
	 */
	public static class CodebehindEntry extends BusinessObject {
		private String docName;
		private String codebehind;
		
		public void setDocumentName(String docName) {
			this.docName = docName;
		}
		
		public String getDocumentName() {
			return docName;
		}
		public void setCodebehind(String codebehind) {
			this.codebehind = codebehind;
		}
		
		
		public String getCodebehind() {
			return codebehind;
		}
	}
	
	/**
	 * Load and bind events.
	 * @throws IOException
	 */
	public void loadEvents() {
		EventDispatcher.initialize();
		throw new UnsupportedOperationException("Implement loadEvents");
	}
	
	private void load(CodebehindEntry entry) {
		try {
			String pythonFile = DeployedMUDFactory.currentMUD().getLocation() + System.getProperty("file.separator") + entry.getCodebehind();
			InputStream pyStream = new FileInputStream(pythonFile);
			EventDispatcher.initializeEvents(entry.getDocumentName(), pyStream);
		} 
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
