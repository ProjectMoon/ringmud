package ring.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ring.mobiles.Mobile;
import ring.world.WorldObject;
import ring.world.WorldObjectMetadata;

/**
 * Searches for world objects based on a presented list. This is
 * used to select things from rooms, etc.
 * @author projectmoon
 *
 */
public class WorldObjectSearch {
	private List<WorldObject> objects = new ArrayList<WorldObject>();
	
	/**
	 * Create a WorldObjectSearch with no world objects to search against.
	 */
	public WorldObjectSearch() {}
	
	/**
	 * Create a WorldObjectSearch with an initial list of objects to be searched.
	 * @param objects
	 */
	public WorldObjectSearch(Collection<? extends WorldObject> objects) {	
		this.objects.addAll(objects);
	}
	
	/**
	 * Add a list of WorldObjects to be searched when {@link #search(String)} is
	 * called.
	 * @param objects A list of {@link ring.world.WorldObject}s.
	 */
	public void addSearchList(Collection<? extends WorldObject> objects) {
		this.objects.addAll(objects);
	}
	
	/**
	 * Searches all world objects in the search lists. The resulting list of 
	 * WorldObjects is a list of relevant world objects, ranked by how closely
	 * their names match the query. The query is case-insensitive.
	 * @param name The name to search for.
	 * @return A list of world objects ranked by relevancy.
	 */
	public List<WorldObject> search(String name) {
		List<WorldObject> results = new ArrayList<WorldObject>(objects.size());
		
		filter(objects, name);
		
		results = relevanceSort(objects, name);
		return results;
	}
	
	/**
	 * The first searching step is to filter out any world objects that do
	 * not even contain our query. This prevents {@link String#compareTo(String)}
	 * from deciding that a string without the query is more relevant than a string
	 * with the query.
	 * @param list
	 * @param text
	 */
	private void filter(List<WorldObject> list, String text) {
		List<WorldObject> removals = new ArrayList<WorldObject>();
		
		for (WorldObject wo : list) {
			WorldObjectMetadata metadata = wo.getMetadata();
			if (metadata.getName().toLowerCase().indexOf(text.toLowerCase()) < 0) {
				removals.add(wo);
			}
		}
		
		list.removeAll(removals);
	}
	
	/**
	 * The second searching step is to rank all remaining world objects by relevance.
	 * This uses {@link String#compareTo(String)} to determine relevancy. It forces
	 * all compareTo results to negative values, and the closer a number is to 0, the
	 * higher it is the result set. Special weight is given to names that start with
	 * the query, as most users would expect that name to be selected first.
	 * @param list The unsorted list of WorldObjects.
	 * @param text The text to search for.
	 * @return The sorted list of WorldObjects.
	 */
	private List<WorldObject> relevanceSort(List<WorldObject> list, final String text) {
		//Ghetto local class for storing search ranking data.
		class ResultTuple {
			int rank;
			WorldObject entry;
		}
		
		//Determine relevancy rank and create a list of ResultTuples.
		List<ResultTuple> results = new ArrayList<ResultTuple>(list.size());
		for (WorldObject wo : list) {
			WorldObjectMetadata metadata = wo.getMetadata();
			int i = metadata.getName().compareToIgnoreCase(text);
			//The closer it is to 0, the more relevant it is.
			i = 0 - Math.abs(i);
			
			//Give more weight to names that start with the specified text.
			String name = metadata.getName().toLowerCase();
			String lcText = text.toLowerCase();
			
			if (name.startsWith(lcText)) {
				i += 10;
			}
			
			//Create tuple to be sorted.
			ResultTuple tuple = new ResultTuple();
			tuple.rank = i;
			tuple.entry = wo;
			results.add(tuple);
		}
		
		//The comparator resposible for sorting the list of results
		//by relevance.
		Comparator<ResultTuple> comp = new Comparator<ResultTuple>() {
			@Override
			public int compare(ResultTuple o1, ResultTuple o2) {
				return o2.rank - o1.rank;
			}
		};
		
		//Sort the tuples and return a list of WorldObjects sorted by relevance.
		Collections.sort(results, comp);
		List<WorldObject> sorted = new ArrayList<WorldObject>(list.size());
		
		for (ResultTuple t : results) {
			sorted.add(t.entry);
		}
		
		return sorted;
	}
	
	public static void main(String[] args) {
		List<Mobile> mobs = new ArrayList<Mobile>();
		Mobile m1 = new Mobile(), m2 = new Mobile(), m3 = new Mobile();
		
		m1.getBaseModel().setName("some guy");
		m2.getBaseModel().setName("another guy");
		m3.getBaseModel().setName("guy in the corner");
		mobs.add(m1); mobs.add(m2); mobs.add(m3);
		
		WorldObjectSearch search = new WorldObjectSearch(mobs);
		
		for (WorldObject mob : search.search("guy")) {
			System.out.println(mob.getMetadata().getName());
		}
	}
}
