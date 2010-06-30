package ring.commands.annotations;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import ring.commands.annotations.Form.Scope;

public class CommandForm {
	private String id;
	private String clause;
	private List<CommandToken> tokens = new ArrayList<CommandToken>();
	private Scope scope;
	
	public CommandForm(Form form) {
		parse(form);	
	}
	
	public static List<CommandForm> processForms(Form ... forms) {
		List<CommandForm> ret = new ArrayList<CommandForm>(forms.length);
		
		for (Form form : forms) {
			ret.add(new CommandForm(form));
		}
		
		return ret;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getClause() {
		return clause;
	}
	
	public void setClause(String clause) {
		this.clause = clause;
	}
	
	public Scope getScope() {
		return scope;
	}
	
	public void setScope(Scope scope) {
		this.scope = scope;
	}
	
	public List<CommandToken> getTokens() {
		return tokens;
	}
	
	public CommandToken getToken(int token) {
		return tokens.get(token);
	}
	
	public List<CommandToken> getDelimiters() {
		List<CommandToken> ret = new ArrayList<CommandToken>();
		for (CommandToken token : getTokens()) {
			if (token.isDelimiter()) {
				ret.add(token);
			}
		}
		
		return ret;
	}
	
	public List<CommandToken> getVariables() {
		List<CommandToken> ret = new ArrayList<CommandToken>();
		for (CommandToken token : getTokens()) {
			if (token.isVariable()) {
				ret.add(token);
			}
		}
		
		return ret;
	}
	
	protected List<CommandToken> getJaggedDelimiters() {
		List<CommandToken> ret = new ArrayList<CommandToken>();
		int last = -1;
		for (CommandToken token : getTokens()) {
			if (token.isDelimiter()) {
				ret.add(token);
				last++;
			}
			else {
				if (ret.size() > 0 && ret.get(last) != null) {
					ret.add(null);
					last++;
				}				
			}
		}
		
		return ret;
	}	
	
	public boolean isDelimiter(String text) {
		for (CommandToken token : getTokens()) {
			if (token.isDelimiter()) {
				if (token.getToken().equalsIgnoreCase(text)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private void parse(Form form) {
		setId(form.id());
		setClause(form.clause());
		setScope(form.scope());
		
		String[] split = form.clause().split(" ");
		int c = 0;
		int count = 0;
		
		for (String tokenString : split) {
			if (!tokenString.equals("")) {
				CommandToken token = new CommandToken();
				token.setToken(tokenString);
				
				//The parser needs to keep track of if this is at the start.
				if (count == 0) {
					token.setAtStart(true);
				}
				
				//Determine if is variable or delimiter and handle accordingly.
				if (tokenString.startsWith(":") || tokenString.startsWith("$")) {
					token.setVariable(true);
					Class<?>[] types = form.bind()[c].value();
					
					List<Class<?>> bindTypes = Arrays.asList(types);
					token.setBindTypes(bindTypes);
					c++;
				}
				else {
					token.setDelimiter(true);
				}
				
				tokens.add(token);
				count++;
			}
		}
		
		//Set atEnd property for the last command token.
		if (tokens.size() > 0) {
			tokens.get(tokens.size() - 1).setAtEnd(true);
		}
	}
	
	public String toString() {
		String ret = "";
		for (CommandToken token : getTokens()) {
			if (token.isDelimiter()) {
				ret += "D" + token + " ";
			}
			else if (token.isVariable()) {
				ret += "V" + token + " ";
			}
		}
		
		return ret.trim();
		
	}
}
