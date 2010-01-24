package ring.nrapi.magic;

/**
 * Interface that allows implementation of a specific, type-safe metadata
 * class for Spells. The interface contains only a single method, transform().
 * The spell metadata implementation is passed to the current MagicSystem,
 * which is responsible for casting to the appropriate metadata implementation
 * and handling errors properly. 
 * @author projectmoon
 *
 */
public interface SpellMetadataImplementation {
	/**
	 * Transform the String-based values into types useful
	 * to this implementation.
	 * @param metadata
	 */
	public void transform(SpellMetadata metadata);
}
