package ring.commands.parser;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import ring.commands.annotations.Form;
import ring.commands.annotations.Scope;

/**
 * The object representation of a command form. This class transforms
 * the information in the Form annotation into something useful.
 * @author projectmoon
 *
 */
public class CommandForm {
	private String id;
	private String clause;
	private List<CommandToken> tokens = new ArrayList<CommandToken>();
	private Scope scope;
	private Scope cascadeType;
	
	public CommandForm(Form form) {
		try {
			parse(form);
		}
		catch (FormParsingException e) {
			throw new CommandException("There was a problem parsing a CommandForm.", e);
		}
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
	
	public int getTokenLength() {
		return tokens.size();
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
	
	public CommandToken getFirstVariable() {
		for (CommandToken token : tokens) {
			if (token.isVariable()) {
				return token;
			}
		}
		return null;
	}
	
	public CommandToken getLastVariable() {
		for (int c = tokens.size() - 1; c >= 0; c--) {
			CommandToken token = tokens.get(c);
			if (token.isVariable()) {
				return token;
			}
		}
		
		return null;
	}
	
	public boolean hasVariables() {
		for (CommandToken token : tokens) {
			if (token.isVariable()) {
				return true;			
			}
		}
		
		return false;
	}
	
	public String toString() {
		return getId();
	}
	
	/**
	 * Transform the given Form into a useful CommandForm object with
	 * friendly properties and methods.
	 * @param form
	 */
	private void parse(Form form) throws FormParsingException {
		setId(form.id());
		setClause(form.clause());
		setScope(form.scope());
		
		
		String[] split = form.clause().split(" ");
		int c = 0;
		int count = 0;
		boolean foundScoped = false;
		
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
					try {
						Class<?>[] types = form.bind()[c].value();
						List<Class<?>> bindTypes = Arrays.asList(types);
						token.setBindTypes(bindTypes);
					}
					catch (ArrayIndexOutOfBoundsException e) {
						throw new FormParsingException("Not all variables have bind types declared on form \"" + this + "\".");
					}
					
					//Handle scoped variable specifically. Cascade detection is at the end of parsing.
					if (tokenString.startsWith("$")) {
						if (foundScoped) {
							throw new FormParsingException("There can only be one scoped variable in a command form.");
						}
						else {
							token.setScoped(true);
							token.setScope(form.scope());
							foundScoped = true;
						}
					}
					else {
						token.setScoped(false);
					}
					
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
			
			if (tokens.get(tokens.size() - 1).isDelimiter()) {
				throw new FormParsingException("Command form \"" + this + "\" cannot end with a delimiter.");
			}
		}
		
		//A bit more error checking
		if (this.hasVariables() && !foundScoped) {
			throw new FormParsingException("Variable form \"" + this + "\" must have a scoped variable in the start or end position.");
		}
				
		//Figure out right vs left cascade.
		detectCascade();
	}
	
	/**
	 * Detect and set which cascading mode to use for forms with variables in them.
	 * The cascading mode determines how translation results are filtered through 
	 * the command chain.
	 */
	private void detectCascade() throws FormParsingException {
		if (this.hasVariables()) {
			CommandToken firstVariable = this.getFirstVariable();
			CommandToken lastVariable = this.getLastVariable();
			
			//Verification.
			if (firstVariable.isScoped() && lastVariable.isScoped() && (firstVariable != lastVariable)) {
				throw new FormParsingException("Cascade conflict. Only the first or last variable may be scoped, not both.");
			}
			
			if (!firstVariable.isScoped() && !lastVariable.isScoped()) {
				throw new FormParsingException("Variable form \"" + this + "\" does not have a scope variable in the start or end position.");
			}
			
			//Now we can cascade.
			if (firstVariable.isScoped()) {
				ltrCascade();
			}
			else if (lastVariable.isScoped()) {
				rtlCascade();
			}
		}
	}
	
	/**
	 * Create a left-to-right cascade.
	 */
	private void ltrCascade() {
		CommandToken first = this.getFirstVariable();
		for (CommandToken variable : this.getVariables()) {
			if (variable != first) {
				variable.setScope(Scope.LTR_CASCADING);
			}
		}
		
		setCascadeType(Scope.LTR_CASCADING);
	}
	
	/**
	 * Create a right-to-left cascade.
	 */
	private void rtlCascade() {
		CommandToken last = this.getLastVariable();
		for (CommandToken variable : this.getVariables()) {
			if (variable != last) {
				variable.setScope(Scope.RTL_CASCADING);
			}
		}
		
		setCascadeType(Scope.RTL_CASCADING);
	}

	public void setCascadeType(Scope cascadeType) {
		if (cascadeType != Scope.LTR_CASCADING && cascadeType != Scope.RTL_CASCADING) {
			throw new IllegalArgumentException("Invalid scope for cascade type. Must use RTL or LTR.");
		}
		
		this.cascadeType = cascadeType;
	}

	public Scope getCascadeType() {
		return cascadeType;
	}
}
