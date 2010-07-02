package ring.commands.annotations;

import java.util.List;
import java.util.ArrayList;

public class CommandToken {
	private boolean isDelimiter;
	private boolean isVariable;
	private boolean isAtStart;
	private boolean isAtEnd;
	
	private String token;
	
	//Variable specific
	private List<Class<?>> bindTypes = new ArrayList<Class<?>>();
	private boolean isScoped;
	private Scope scope;
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public boolean isDelimiter() {
		return isDelimiter;
	}
	
	public void setDelimiter(boolean delimiter) {
		isDelimiter = delimiter;
		isVariable = !isDelimiter;
	}
	
	public boolean isVariable() {
		return isVariable;
	}
	
	public void setVariable(boolean variable) {
		isVariable = variable;
		isDelimiter = !isVariable;
	}
	
	public List<Class<?>> getBindTypes() {
		if (isDelimiter()) {
			throw new IllegalArgumentException("This CommandToken is a delimiter, not a variable.");
		}
		else { 
			return bindTypes;
		}
	}
	
	public void setBindTypes(List<Class<?>> bindTypes) {
		if (isDelimiter()) {
			throw new IllegalArgumentException("This CommandToken is a delimiter, not a variable.");
		}
		else { 
			this.bindTypes = bindTypes;
		}		
	}
	
	public boolean isAtStart() {
		return isAtStart;
	}
	
	public void setAtStart(boolean beginning) {
		isAtStart = beginning;
		isAtEnd = !isAtStart;
	}
	
	public boolean isAtEnd() {
		return isAtEnd;
	}
	
	public void setAtEnd(boolean end) {
		isAtEnd = end;
		isAtStart = !isAtEnd;
	}
	
	public String toString() {
		return getToken();
	}

	public void setScoped(boolean isScoped) {
		this.isScoped = isScoped;
	}

	public boolean isScoped() {
		return isScoped;
	}

	public void setScope(Scope scope) {
		if (!isScoped()) {
			if (scope != Scope.RTL_CASCADING && scope != Scope.LTR_CASCADING) {
				throw new IllegalArgumentException("Non-scoped variables can only use cascading scopes.");
			}
		}
		this.scope = scope;
	}

	public Scope getScope() {
		if (!isScoped()) {
			if (scope != Scope.RTL_CASCADING && scope != Scope.LTR_CASCADING) {
				throw new IllegalArgumentException("Non-scoped variables can only use cascading scopes.");
			}
		}
		return scope;
	}
}
