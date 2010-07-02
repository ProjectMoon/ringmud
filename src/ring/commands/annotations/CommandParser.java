package ring.commands.annotations;

import java.util.ArrayList;
import java.util.List;

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
	 * Lame helper class to return from the findForm method.
	 *
	 */
	private class CPTuple {
		public CommandForm form;
		public List<ParsedCommandToken> tokenList;
	}
	
	private String commandName;
	private Command command;
	private List<CommandForm> forms;
	
	//Later a command.
	public CommandParser(Command command) {
		commandName = command.getCommandName();
		this.command = command;
		Template cmdTemplate = command.getClass().getAnnotation(Template.class);
		
		if (cmdTemplate == null) {
			throw new IllegalArgumentException("The Command object does not have a defined command Template!");
		}
		else {
			forms = CommandForm.processForms(cmdTemplate.value());
		}
	}
	
	public Command getCommand() {
		return command;
	}
	
	public List<CommandForm> getForms() {
		return forms;
	}
	
	public String getCommandName() {
		return commandName;
	}
	
	/**
	 * Parse the supplied command using the supplied CommandSender
	 * as the "origin point" for the command. This method will return
	 * a ParsedCommand object if the command parsing is successful.
	 * Otherwise, it will return null. Null is only returned if the
	 * command to be sent does not conform to the any of the forms
	 * of the Command object stored in this parser. 
	 * @param sender
	 * @param command
	 * @return A ParsedCommand if parsing was successful, or null if it was unsuccessful.
	 */
	public ParsedCommand parse(CommandSender sender, String command) {
		String[] split = command.split(" ");
		String clause = command.substring(command.indexOf(" ") + 1);
		
		//First figure out if the root command is actually correct.
		if (!split[0].equalsIgnoreCase(commandName)) {
			return null;
		}
		
		//Next find the correct command form.
		CPTuple tuple = parseClause(clause);

		if (tuple.form != null) {
			ParsedCommand cmd = new ParsedCommand();
			cmd.setFormID(tuple.form.getId());
			cmd.setCommand(commandName);
			cmd.setScope(tuple.form.getScope());
			cmd.setCascadeType(tuple.form.getCascadeType());
			
			//Delegate to ParsedCommand#initialize for object translation.
			cmd.initialize(sender, tuple.tokenList);
			return cmd;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Parses the supplied command clause by searching for a suitable
	 * CommandForm and parsing it according to the grammar defined in said
	 * suitable CommandForm. The method returns a "tuple" object consisting 
	 * of both the CommandForm and the list of parsed command tokens. If no
	 * suitable command form could be found, the method returns null.
	 * @param clause
	 * @return A tuple containing the CommandForm and parsed tokens, or null.
	 */
	private CPTuple parseClause(String clause) {
		for (CommandForm form : forms) {
			List<ParsedCommandToken> tokens = parseAndTestForm(form, clause);
			
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
	 * This method parses and tests a CommandForm object to see if it lexically
	 * agrees with the supplied clause. The method actually delegates to two
	 * separate methods: one for handling a single token CommandForm, and another
	 * for handling a multiple token CommandFomr.
	 * @param form
	 * @param clause
	 * @return A list of parsed command tokens if the command matches, null otherwise.
	 */
	private List<ParsedCommandToken> parseAndTestForm(CommandForm form, String clause) {	
		//Special case for 1 token forms.
		if (form.getTokenLength() == 1) {
			return parseSingleTokenForm(form, clause);
		}
		else {
			return parseMultiTokenForm(form, clause);
		}

	}
	
	/**
	 * Method that tests to see if the supplied clause agrees with the supplied single-token
	 * CommandForm.
	 * @param form
	 * @param clause
	 * @return A list of parsed command tokens if the command matches, null otherwise.
	 */
	private List<ParsedCommandToken> parseSingleTokenForm(CommandForm form, String clause) {
		String[] split = clause.split(" ");
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
		else {
			return null;
		}
	}
	
	/**
	 * Method that parses the supplied multiple token CommandForm to see if the
	 * supplied clause agrees with it. This method is rather complex, and also
	 * delegates to a finishing method in a vain attempt to be readable.
	 * @param form
	 * @param clause
	 * @return A list of parsed command tokens if the clause matches, null otherwise.
	 */
	private List<ParsedCommandToken> parseMultiTokenForm(CommandForm form, String clause) {
		String[] split = clause.split(" ");
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
			
			//TODO This is greedy and doesn't take delims on the end into account
			//Thus producing a bug where the shorter verison of a command will work
			//and not the correct one.
			ParsedCommandToken lastToken = new ParsedCommandToken(c, split.length);
			parsed.add(lastToken);
			
			parsed = finishMultiTokenParsing(form, parsed, split);
			
			return parsed;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Final parsing step to craft the variable names, remove blank tokens, and
	 * match the parsed token to the variable names they map to during multitoken
	 * command form parsing.
	 * @param form
	 * @param parsed
	 * @param split
	 * @return The polished list of parsed command tokens, should the list have anything in it. Null otherwise.
	 */
	private List<ParsedCommandToken> finishMultiTokenParsing(CommandForm form, List<ParsedCommandToken> parsed, String[] split) {		
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
	
	/**
	 * Helper method that sticks the extracted text of the parsed command token into
	 * its token property.
	 * @param token
	 * @param clause
	 */
	private void craftParsedToken(ParsedCommandToken token, String[] clause) {
		String text = "";
		for (int c = token.getStartIndex(); c < token.getEndIndex(); c++) {
			text += clause[c] + " ";
		}
		
		token.setToken(text.trim());
	}
}
