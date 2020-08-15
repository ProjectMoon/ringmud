package ring.util;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Hex;

public class UserUtilities {
	//This is not instantiable!
	private UserUtilities() {}
	
	public static String sha1Hash(String text) {
		try {
		MessageDigest md = null;
		md = MessageDigest.getInstance("SHA-1");
		md.update(text.getBytes("UTF-8"));
		byte[] raw = md.digest();
		return new String(Hex.encodeHex(raw));
		}
		catch (Exception e) {
			throw new EncryptionException(e);
		}
	}
	
	/**
	 * Softer exception to catch stuff in the sha1Hash method. Yes, this is a bad practice.
	 * Kids, don't try this at home.
	 * @author projectmoon
	 *
	 */
	@SuppressWarnings("serial")
	public static class EncryptionException extends RuntimeException {
		public EncryptionException(String msg) {
			super(msg);
		}
		
		public EncryptionException(Throwable cause) {
			super(cause);
		}
		
		public EncryptionException(String msg, Throwable cause) {
			super(msg, cause);
		}
	}
}
