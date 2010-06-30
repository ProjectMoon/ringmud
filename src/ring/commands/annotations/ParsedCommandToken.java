package ring.commands.annotations;

/**
 * Helper class for parsed command tokens. The CommandParser creates
 * a ParsedCommandToken to keep track of positions in the command
 * clause.
 * @author projectmoon
 *
 */
public class ParsedCommandToken {
	private int startIndex;
	private int endIndex;
	private String token;
	private String matched;
	
	public ParsedCommandToken() {}
	public ParsedCommandToken(int start, int end) {
		startIndex = start;
		endIndex = end;
	}
	
	public int getStartIndex() {
		return startIndex;
	}
	
	public void setStartIndex(int index) {
		startIndex = index;
	}
	
	public int getEndIndex() {
		return endIndex;
	}
	
	public void setEndIndex(int index) {
		endIndex = index;
	}
	
	public String toString() {
		return "[" + startIndex + ", " + endIndex + "]";
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getMatched() {
		return matched;
	}
	
	public void setMatched(String matched) {
		this.matched = matched;
	}
}
