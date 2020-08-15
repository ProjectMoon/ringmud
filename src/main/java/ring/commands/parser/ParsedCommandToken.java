package ring.commands.parser;

/**
 * Class that represents a parsed command token. It stores the start and end
 * index of the string that was parsed, the string being parsed, the text found
 * in the string, and the {@link ring.commands.parser.CommandToken} object that
 * this parsed token matched.
 * @author projectmoon
 *
 */
public class ParsedCommandToken {
	private int startIndex;
	private int endIndex;
	private CommandToken matched;
	private String token;
	private String[] parentClause;
	
	
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
		return getToken() + "(" + getStartIndex() + ", " + getEndIndex() + ")";
	}
	
	public CommandToken getMatched() {
		return matched;
	}
	
	public void setMatched(CommandToken matched) {
		this.matched = matched;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setParentClause(String[] parentClause) {
		this.parentClause = parentClause;
	}
	
	public String[] getParentClause() {
		return parentClause;
	}
}
