package ring.compiler;

public class ValidationError {
	private int line;
	private String error;
	
	public ValidationError(int line, String error) {
		this.line = line;
		this.error = error;
	}
	
	public int getLine() {
		return line;
	}
	
	public String getError() {
		return error;
	}
}
