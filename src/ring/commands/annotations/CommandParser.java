package ring.commands.annotations;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Template({
	@Form(bind = { @BindType({String.class}) }),
	@Form(id = "lookThing", clause = ":thing", bind = { @BindType({String.class, Class.class}) }),
	@Form(id = "lookAway", clause = ":thing away", bind = { @BindType({String.class, Class.class}) }),
	@Form(id = "lookAt", clause = "at :thing in $box", bind = { @BindType({String.class}), @BindType({Class.class}) }),
})
public class CommandParser {
	private String commandName;
	private Template template;
	private List<CommandForm> forms;
	
	//Later a command.
	public CommandParser(Template template) {
		commandName = "look";
		this.template = template;
		forms = CommandForm.processForms(template.value());
	}
	
	//The magic method...
	public ParsedCommand parse(String command) {
		//Split on " "
		//Check first word to see if it's the right command
		//Go backwards through the command string and look for the first ocurrance of a delimiter.
		//Once we find it, take everything between it and the previous delimiter and bind it to a variable (if necessary)
		//Repeat for until out of delimiters.
		//If we can't find a delimiter, then the command doesn't match so we return false or null or something.		
		
		String[] split = command.split(" ");
		String clause = command.substring(command.indexOf(" ") + 1);
		if (!split[0].equalsIgnoreCase(commandName)) {
			return null;
		}
		
		CommandForm form = findForm(clause);
		//System.out.println("command matches: " + form.getId());
		
		return null;
	}
	
	private CommandForm findForm(String clause) {
		for (CommandForm form : forms) {
			System.out.println("testing: " + form + ":");
			List<ParsedCommandToken> tokens = testForm(form, clause);
			
			String[] split = clause.split(" ");
			if (tokens != null) {
				for (ParsedCommandToken token : tokens) {
					System.out.print("    ");
					System.out.println(token.getToken());
				}
			}
			else {
				System.out.println("    no match");
			}
		}
		
		return null;
	}
	
	private List<ParsedCommandToken> testForm(CommandForm form, String clause) {
		List<ParsedCommandToken> parsed = new ArrayList<ParsedCommandToken>();
		String prevDelim = null;
		ParsedCommandToken currToken;
		ParsedCommandToken prevToken = null;
		
		List<CommandToken> delims = form.getDelimiters();
		String[] split = clause.split(" ");
		int c = 0;
		boolean errors = false;
		
		if (delims.size() == 0) errors = true;
		
		for (CommandToken delim : delims) {
			String currDelim = delim.getToken();
				
			currToken = new ParsedCommandToken();
			currToken.setStartIndex(c);
			parsed.add(currToken);
			
			boolean found = false;
 			for (; c < split.length; c++) {
 				if (delim.isAtStart() && delim.isDelimiter() && c == 0) {
 					if (!delim.getToken().equals(split[c])) {
 						errors = true;
 						break;
 					}
 				}
 				
				if (!split[c].equals(currDelim)) {
					//Make sure we don't slip up on delims and arguments that get mixed.
					if (prevDelim != null && split[c].equals(prevDelim)) {
						prevToken.setEndIndex(c);
						currToken.setStartIndex(c + 1);
					}					
				}
				else {
					currToken.setEndIndex(c);
					found = true;
					break;
				}
			}
 			
 			if (!found) {
				errors = true;
 			}
 			
 			if (errors) {
 				break;
 			}
		
			prevDelim = currDelim;
			prevToken = currToken;
		}
		
		if (!errors) {
			//convert remainder of tokens to ParsedCommandToken
			ParsedCommandToken lastToken = new ParsedCommandToken(c, split.length);
			parsed.add(lastToken);
			
			for (ParsedCommandToken token : parsed) {
				craftParsedToken(token, split);
			}
			
			if (parsed.size() > 0) {
				return parsed;
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
		 
	}
	
	private void craftParsedToken(ParsedCommandToken token, String[] clause) {
		String text = "";
		for (int c = token.getStartIndex(); c < token.getEndIndex(); c++) {
			text += clause[c] + " ";
		}
		
		token.setToken(text.trim());
	}
	
	public static void main(String[] args) {
		String command = "look thing away";
		/*for (String arg : args) {
			command += arg + " ";
		}*/
		command = command.trim();
		Template t = CommandParser.class.getAnnotation(Template.class);
		
		CommandParser parser = new CommandParser(t);
		parser.parse(command);
	}
}

class ParsedCommandToken {
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
