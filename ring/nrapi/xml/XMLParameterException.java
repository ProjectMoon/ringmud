package ring.nrapi.xml;

/**
 * Exception class that is thrown when there is a problem
 * parsing the parameters from EffectCreators, spell metadata,
 * or other classes that rely on unsafe type conversions from
 * XML text.
 * @author projectmoon
 *
 */
public class XMLParameterException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public XMLParameterException(String msg) {
		super(msg);
	}
	
	public XMLParameterException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
