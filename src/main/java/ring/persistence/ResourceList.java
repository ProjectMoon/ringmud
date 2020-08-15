package ring.persistence;

import java.util.Iterator;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;

/**
 * Class that encapsulates a ResourceSet and gives it a more friendly API.
 * This ResourceList can be iterated over with a foreach loop, unlike a ResourceSet.
 * Also provides a close() method to close the parent collection of this ResourceList. 
 * @author projectmoon
 *
 */
public class ResourceList implements Iterable<Resource> {
	private Collection parentCollection;
	private ResourceSet resources;
	private ResourceIterator iter;
	
	public ResourceList(Collection parentCollection, ResourceSet resources) throws XMLDBException {
		this.parentCollection = parentCollection;
		this.resources = resources;
		iter = resources.getIterator();
	}
	
	@Override
	public Iterator<Resource> iterator() {
		return new IteratorImpl(iter);
	}
	
	public int size() throws XMLDBException {
		return (int) resources.getSize();
	}
	
	public boolean hasMoreResources() throws XMLDBException {
		return iter.hasMoreResources();
	}
	
	public Resource nextResource() throws XMLDBException {
		return iter.nextResource();
	}
	
	public void close() throws XMLDBException {
		parentCollection.close();
	}
}

class IteratorImpl implements Iterator<Resource> {
	private ResourceIterator iter;
	
	public IteratorImpl(ResourceIterator iter) {
		this.iter = iter;
	}
	
	@Override
	public boolean hasNext() {
		try {
			return iter.hasMoreResources();
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public Resource next() {
		try {
			return iter.nextResource();
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("this iterator does not support removal");		
	}
	
}
