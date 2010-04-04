package ring.persistence;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.python.util.PythonInterpreter;
import org.w3c.dom.Node;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

import ring.deployer.DeployedMUD;
import ring.deployer.DeployedMUDFactory;
import ring.main.RingModule;
import ring.movement.Room;

public class XQueryModule implements RingModule {

	@Override
	public void execute(String[] args) {
		//Discover the mud we are to query against
		DeployedMUD mud = DeployedMUDFactory.getMUD(args[0]);
		
		if (mud != null) {
			ExistDB.setRootURI(mud.getName());
		}
		String filename = args[1];
		File xqueryFile = new File(filename);
		
		String xquery = "";
		String line = "";
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(xqueryFile));
			
			while ((line = reader.readLine()) != null) {
				xquery += line + "\n";
			}
			
			System.out.println("Executing: " + xquery);
			XQuery xq = new XQuery(xquery);
			//xq.setLoadpoint(Loadpoint.STATIC);
			ResourceList results = xq.execute();
			
			System.out.println("result size: " + results.size());
			
			for (Resource res : results) {
				System.out.println(res.getId() + ":");
				System.out.println(res.getContent());
			}
			
			results.close();
			
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean usesDatabase() {
		return true;
	}

}
