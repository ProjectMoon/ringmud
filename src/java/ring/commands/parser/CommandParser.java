package ring.commands.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.python.core.PyException;
import org.python.core.PyObject;
import org.python.core.PyTuple;
import org.python.core.PyType;

import ring.commands.Command;
import ring.commands.CommandArguments;
import ring.commands.CommandSender;
import ring.commands.annotations.Form;
import ring.commands.annotations.Template;

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
	
	public CommandParser(Command command) throws CommandParsingException {
		commandName = command.getCommandName();
		this.command = command;
		Template cmdTemplate = command.getClass().getAnnotation(Template.class);
		
		if (cmdTemplate != null) {
			initialize(cmdTemplate);
		}
		else {
			throw new IllegalArgumentException("The Command object does not have a defined command Template!");
		}
	}
	
	public CommandParser(PyObject pyCommand) throws CommandParsingException {
		if (isInstanceOfCommand(pyCommand)) {
			try {
				Template cmdTemplate = (Template)pyCommand.__getattr__("__template__").__tojava__(Template.class);
				
				if (cmdTemplate != null) {
					initialize(cmdTemplate);
				}
				else {
					throw new IllegalArgumentException("The Command object does not have a defined command Template!");
				}				
			}
			catch (PyException e) {
				throw new IllegalArgumentException("The Command object does not have a defined command Template!");
			}
		}
		else {
			throw new IllegalArgumentException("The passed Python must be a class object derived from ring.commands.parser.Command");
		}
	}
	
	/**
	 * Delegate to this method for actual object initialization.
	 * @param template
	 * @throws CommandParsingException
	 */
	private void initialize(Template template) throws CommandParsingException {
		//Initial error checking.
		HashSet<String> idCheck = new HashSet<String>();
		for (Form form : template.value()) {
			if (!idCheck.add(form.id())) {
				throw new CommandParsingException("Supplied command template has one or more duplicate command form IDs.");
			}
		}
		forms = CommandForm.processForms(template.value());	
	}
	
	private boolean isInstanceOfCommand(PyObject pyObj) {
		if (pyObj instanceof PyType) {
			PyTuple mro = (PyTuple)pyObj.__getattr__("__mro__");
		
			for (Object pyType : mro) {
				if (pyType == Command.class) {
					return true;
				}
			}
		}
		
		return false;
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
	public CommandArguments parse(CommandSender sender, String command) throws CommandParsingException {
		String[] split = command.split(" ");
		String clause = "";
		
		//Do we even have a clause?
		if (split.length > 1) {
			clause = command.substring(command.indexOf(" ") + 1);
		}
		else {
			clause = "";
		}
		
		//Figure out if the root command is actually correct.
		if (!split[0].equalsIgnoreCase(commandName)) {
			return null;
		}
		
		//Next find the correct command form.
		CPTuple tuple = parseClause(clause);

		if (tuple != null && tuple.form != null) {
			CommandArguments cmd = new CommandArguments();
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
	 * agrees with the supplied clause. The method actually delegates to three
	 * separate methods: one for handling no-token CommandForms, one for handling
	 * a single token CommandForm, and another for handling a multiple token CommandForm.
	 * @param form
	 * @param clause
	 * @return A list of parsed command tokens if the command matches, null otherwise.
	 */
	private List<ParsedCommandToken> parseAndTestForm(CommandForm form, String clause) {
		//Special case for 1 token forms.
		if (form.getTokenLength() == 1) {
			return parseSingleTokenForm(form, clause);
		}
		else if (form.getTokenLength() > 1) {
			return parseMultiTokenForm(form, clause);
		}
		else {
			return parseNoTokenForm(form, clause);
		}
	}
	
	/**
	 * Parse a no-token command form. This is the simplest CommandForm to deal with.
	 * @param form
	 * @param clause
	 * @return A list of parsed command tokens if the command matches, null otherwise.
	 */
	private List<ParsedCommandToken> parseNoTokenForm(CommandForm form, String clause) {
		String[] split = clause.split(" ");
		if (split.length == 1 && split[0].equals("")) {
			return new ArrayList<ParsedCommandToken>(0);
		}
		else {
			return null;
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
			currToken.setParentClause(split);
			currToken.setStartIndex(c);
			parsed.add(currToken);
			
			boolean found = false;
 			for (; c < split.length; c++) {
 				//Error check if we start with a delimiter.
 				if (delim.isAtStart() && delim.isDelimiter() && c == 0) {
 					if (!delim.getToken().equals(split[c])) {
 						errors = true;
 						break;
 					}
 				}
 				
 				//Find the start and end indices of the matched token.
				if (!split[c].equals(currDelim)) {
					//Make sure we don't slip up on delims and arguments that get mixed.
					//We must rewind and fix the previous token if we find this case.
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
			
			try {
				for (int c = 0; c < variables.size(); c++) {
					parsed.get(c).setMatched(variables.get(c));
				}

				return parsed;
			}
			catch (IndexOutOfBoundsException e) {
				return null;
			}
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
