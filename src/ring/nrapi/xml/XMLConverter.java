package ring.nrapi.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.python.core.PyObject;
import org.python.core.PyType;
import org.python.util.PythonInterpreter;

import ring.persistence.Persistable;

/**
 * A tool for converting Jython-based Persistable objects to XML that the engine can understand.
 * This stores a list of Persistables, and when requested, calls the toXML() method on each one.
 * This tool is mainly to be used by world builders who do not wish to code data in XML by hand.
 * Instead, the compiler will use this to construct the XML for them. The only downside is that
 * game objects must be added to this object manually.
 * @author projectmoon
 *
 */
public class XMLConverter {
	private static List<Persistable> persistables = new ArrayList<Persistable>();
	
	/**
	 * Adds a Persistable to the XML converter for later use.
	 * @param p
	 */
	public static void add(Persistable p) {
		persistables.add(p);
	}
	
	/**
	 * Adds a list of Persistables for later use.
	 * @param ps
	 */
	public static void add(List<Persistable> ps) {
		persistables.addAll(ps);
	}
	
	/**
	 * Adds a python dictionary or list containing only Persistables to the list of
	 * objects to be converted to XML. This method performs basic type checking to
	 * ensure that the PyObject received is either a dict or a list. If the passed object
	 * is neither a list nor a dict, the method will assume that the object being added
	 * is an individual Persistable and will try to convert it to that. If all of these
	 * fail, an IllegalArgumentException will be thrown.
	 * <br/><br/>
	 * When adding a list or dict, it will iterate over all values without regards to their
	 * actual data type, so it is still possible to get a ClassCastException.
	 * @param p
	 */
	public static void add(PyObject p) {
		if (p.getType().getName().equals("dict")) {
			Map<?, Persistable> map = (Map<?, Persistable>)p.__tojava__(Map.class);
			persistables.addAll(map.values());
		}
		else if (p.getType().getName().equals("list")) {
			List<Persistable> list = (List<Persistable>) p.__tojava__(List.class);
			persistables.addAll(list);
		}
		else {
			try {
				Persistable persistable = (Persistable)p.__tojava__(Persistable.class);
				persistables.add(persistable);
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Type \"" + p.getType().getName() + "\" is invalid in this context.");
			}
		}
	}
	
	/**
	 * Adds a Map containing Persistables to the XML Converter for later use.
	 * The keys may be of any type, as the XMLConverter is only concerned with the values.
	 * @param map
	 */
	public static void add(Map<?, Persistable> map) {
		persistables.addAll(map.values());
	}
	
	/**
	 * Converts all stored persistables to XML and returns their XML strings in a List.
	 * @return The converted XML.
	 */
	public static List<String> getXML() {
		List<String> xml = new ArrayList<String>(persistables.size());
		
		for (Persistable p : persistables) {
			xml.add(p.toXML());
		}
		
		return xml;
	}
	
	public static List<Persistable> getPersistables() {
		return persistables;
	}
	
	/**
	 * Clears the list of stored persistables.
	 */
	public static void clear() {
		persistables.clear();
	}
	
	public static void main(String[] args) {
		PythonInterpreter interp = new PythonInterpreter();
		interp.exec("from ring.persistable import Persistable");
		interp.exec("m = { 'sdf':'blarp' }");
		PyObject po = interp.get("m");
		XMLConverter.add(po);
	}
}
