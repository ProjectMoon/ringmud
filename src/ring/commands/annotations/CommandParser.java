package ring.commands.annotations;

import java.util.ArrayList;
import java.util.List;

import ring.commands.WorldObjectSearch;
import ring.movement.Room;

/**
 * The command parser receives a command template and parses commands given to it.
 * It will match command input to a form of the command template. If no form matches,
 * the parser will return null form its parse method. If more than one form matches,
 * it will return the first form found.
 * @author projectmoon
 *
 */

public class CommandParser {
	/**
	 * Helper class to return from the findForm method.
	 *
	 */
	private class CPTuple {
		public CommandForm form;
		public List<ParsedCommandToken> tokenList;
	}
	
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
	public ParsedCommand parse(CommandSender sender, String command) {
		String[] split = command.split(" ");
		String clause = command.substring(command.indexOf(" ") + 1);
		
		//First figure out if the root command is actually correct.
		if (!split[0].equalsIgnoreCase(commandName)) {
			return null;
		}
		
		//Next find the correct command form.
		CPTuple tuple = findForm(clause);

		if (tuple.form != null) {
			ParsedCommand cmd = new ParsedCommand();
			cmd.setFormID(tuple.form.getId());
			cmd.setCommand(commandName);
			cmd.setScope(tuple.form.getScope());
			cmd.setCascadeType(tuple.form.getCascadeType());
			
			cmd.initialize(sender, tuple.tokenList);
			return cmd;
		}
		else {
			return null;
		}
	}
	
	private CPTuple findForm(String clause) {
		for (CommandForm form : forms) {
			List<ParsedCommandToken> tokens = testForm(form, clause);
			
			if (tokens != null) {
				CPTuple tuple = new CPTuple();
				tuple.form = form;
				tuple.tokenList = tokens;
				return tuple;
			}
		}
		
		return null;
	}
	
	/**
	 * Important method. This determines if a command form matches the command
	 * sent to the parser. Could probably use some refactoring.
	 * @param form
	 * @param clause
	 * @return A list of parsed command tokens if the command matches, null otherwise.
	 */
	private List<ParsedCommandToken> testForm(CommandForm form, String clause) {
		String[] split = clause.split(" ");
		
		//Special case for 1 token forms.
		if (form.getTokenLength() == 1) {
			CommandToken token = form.getToken(0);
			if (token.isDelimiter() && split[0].equals(token.getToken()) && split.length == 1) {
				return new ArrayList<ParsedCommandToken>(0);
			}
			else if (token.isVariable()) {
				List<ParsedCommandToken> parsed = new ArrayList<ParsedCommandToken>(1);
				ParsedCommandToken parsedToken = new ParsedCommandToken();
				parsedToken.setStartIndex(0);
				parsedToken.setEndIndex(split.length);
				parsedToken.setToken(clause);
				parsedToken.setMatched(token);
				parsed.add(parsedToken);
				return parsed;
			}
		}
		
		//The real monster.
		List<ParsedCommandToken> parsed = new ArrayList<ParsedCommandToken>();
		String prevDelim = null;
		ParsedCommandToken currToken;
		ParsedCommandToken prevToken = null;
				
		List<CommandToken> delims = form.getDelimiters();
		
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
			//Forms that end in a variable need c advanced one more space or they will
			//get the last delimiter too.
			if (form.getToken(form.getTokenLength() - 1).isVariable()) {
				c++;
			}
			
			ParsedCommandToken lastToken = new ParsedCommandToken(c, split.length);
			parsed.add(lastToken);
			
			parsed = finishParsing(form, parsed, split);
			
			return parsed;
		}
		else {
			return null;
		}
		 
	}
	
	/**
	 * Final parsing step to craft the variable names, remove blank tokens, and
	 * match the parsed token to the variable names they map to.
	 * @param form
	 * @param parsed
	 * @param split
	 * @return
	 */
	private List<ParsedCommandToken> finishParsing(CommandForm form, List<ParsedCommandToken> parsed, String[] split) {		
		//Craft the tokens.
		for (ParsedCommandToken token : parsed) {
			craftParsedToken(token, split);
		}
		
		//Remove all blanks.
		List<ParsedCommandToken> blanks = new ArrayList<ParsedCommandToken>();
		for (ParsedCommandToken token : parsed) {
			if (token.getToken().trim().equals("")) {
				blanks.add(token);
			}
		}
		parsed.removeAll(blanks);
		
		if (parsed.size() <= 0) {
			return null;
		}
		else {
			//Set the matched tokens.
			//Length and order of variable list should that of parsed list.
			List<CommandToken> variables = form.getVariables();
			
			for (int c = 0; c < variables.size(); c++) {
				parsed.get(c).setMatched(variables.get(c));
			}

			return parsed;	
		}
	}
	
	private void craftParsedToken(ParsedCommandToken token, String[] clause) {
		String text = "";
		for (int c = token.getStartIndex(); c < token.getEndIndex(); c++) {
			text += clause[c] + " ";
		}
		
		token.setToken(text.trim());
	}
}
