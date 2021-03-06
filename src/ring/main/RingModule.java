package ring.main;

/**
 * High-level interface that all modules of the RingMUD system implement.
 * Allows the system to start and stop the given module. 
 * @author jeff
 *
 */
public interface RingModule {
	public void execute(String[] args);
	public boolean usesDatabase();
}
