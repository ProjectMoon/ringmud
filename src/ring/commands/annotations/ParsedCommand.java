package ring.commands.annotations;

import java.util.List;

public class ParsedCommand {
	private String formID;
	private String command;
	private List<Object> arguments;
	
	public String getFormID() {
		return formID;
	}
	
	public void setFormID(String id) {
		formID = id;
	}
	
	public String getCommand() {
		return command;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public List<Object> getArguments() {
		return arguments;
	}
	
	public void setArguments(List<Object> args) {
		arguments = args;
	}
	
	public Object getArgument(int index) {
		return arguments.get(index);
	}
}
