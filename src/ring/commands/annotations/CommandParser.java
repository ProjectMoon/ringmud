package ring.commands.annotations;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Template({
	@Form(bind = { @BindType({String.class}) }),
	@Form(id = "lookThing", clause = ":thing", bind = { @BindType({String.class, Class.class}) }),
	@Form(id = "lookAway", clause = "away :thing", bind = { @BindType({String.class, Class.class}) }),
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
			System.out.print("testing: " + form + "...");
			System.out.println(testForm(form, clause));
		}
		
		return null;
	}
	
	private boolean testForm(CommandForm form, String clause) {
		String[] split = clause.split(" ");
		/*
		int index = 0;
		int matches = 0;
		
		int delimIndex = 0;
		for (CommandToken token : form.getTokens()) {			
			if (token.isDelimiter()) {
				boolean found = false;
				for (int c = index; c < split.length; c++) {
					if (token.getToken().equalsIgnoreCase(split[c]) && c >= delimIndex) {
						found = true;
						matches++;
						index = c;
						break;
					}
				}
							
				if (!found) {
					break;
				}
			}
			
			delimIndex++;
		}
		return matches;
		*/
		
		List<String> clauseDelims = new ArrayList<String>();
		int last = -1;
		for (String token : split) {
			if (form.isDelimiter(token)) {
				clauseDelims.add(token);
				last++;
			}
			else {
				if (clauseDelims.size() > 0) {
					if (clauseDelims.get(last) != null) {
						clauseDelims.add(null);
						last++;
					}
				}
				else {
					clauseDelims.add(null);
					last++;
				}
			}
		}
		
		List<CommandToken> formDelims = form.getJaggedDelimiters();
		
		System.out.println(formDelims);
		System.out.println(clauseDelims);
	
		return formDelims.toString().equals(clauseDelims.toString());
	}
	
	public static void main(String[] args) {
		String command = "";
		for (String arg : args) {
			command += arg + " ";
		}
		command = command.trim();
		Template t = CommandParser.class.getAnnotation(Template.class);
		
		CommandParser parser = new CommandParser(t);
		parser.parse(command);
	}
}
