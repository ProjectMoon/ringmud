package ring.commands.annotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ring.commands.WorldObjectSearch;
import ring.movement.Room;
import ring.world.WorldObject;

public class ParsedCommand {
	private String formID;
	private String command;
	private List<Object> arguments;
	private Scope cascadeType;
	private Scope scope;
	
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
	
	public void setArguments(Object ... args) {
		arguments.addAll(Arrays.asList(args));
	}
	
	public Object getArgument(int index) {
		return arguments.get(index);
	}

	public void setCascadeType(Scope cascadeType) {
		this.cascadeType = cascadeType;
	}

	public Scope getCascadeType() {
		return cascadeType;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public Scope getScope() {
		return scope;
	}
	
	protected void initialize(CommandSender sender, List<ParsedCommandToken> tokens) {
		if (tokens == null || tokens.size() == 0) {
			setArguments(sender.getContext().getLocation());
			return;
		}
		else {
			System.out.println("Cascade type: " + this.getCascadeType());
			if (this.getCascadeType() == Scope.RIGHT_CASCADING) {
				initializeRightCascade(sender, tokens);
			}
			else if (this.getCascadeType() == Scope.LEFT_CASCADING) {
				initializeLeftCascade(sender, tokens);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void initializeRightCascade(CommandSender sender, List<ParsedCommandToken> tokens) {
		List<Object> arguments = new ArrayList<Object>(tokens.size());
		
		ParsedCommandToken first = tokens.get(0);
		System.out.println("first: " + first + ". scope = " + first.getMatched().getScope());
		Scope scope = first.getMatched().getScope();
		
		//Retrieve initial WO dataset from first token based on its scope and bind types.
		//Filter that list via WorldObjectSearch and bind the first result to the argument
		//Begin looping over rest of variables:
		//	Use previous WO's produceSearchList(Class ... cs) method based on bind types for the current PT
		//	Filter WO list based on parsed token and set the argument.
		
		List<WorldObject> rootObjs = null;
		if (scope == Scope.ROOM) {
			Room location = sender.getContext().getLocation();
			rootObjs = location.produceSearchList(first.getMatched().getBindTypes());
			System.out.println("bind types: " + first.getMatched().getBindTypes());
			System.out.println("root list: " + rootObjs);
			WorldObject rootArg = search(first.getToken(), rootObjs);
			arguments.add(rootArg);
			
			WorldObject previousArg = rootArg;
			
			for (int c = 1; c < tokens.size(); c++) {
				ParsedCommandToken token = tokens.get(c);
				List<Class<?>> bindTypes = token.getMatched().getBindTypes();
				List<WorldObject> objs = previousArg.produceSearchList(bindTypes);
				WorldObject arg = search(token.getToken(), objs);
				arguments.add(arg);
				previousArg = arg;
			}
			
			setArguments(arguments);
		}
		else if (scope == Scope.MOBILE){
			//search for mobs in current room.
		}
	}
	
	private void initializeLeftCascade(CommandSender sender, List<ParsedCommandToken> tokens) {
		
	}
	
	/**
	 * This method delegates to {@link ring.commands.WorldObjectSearch} in order
	 * to search collections of world objects from generic data sources. It returns
	 * the most relevant world object found amongst all presented collections. The
	 * text searched for is case-insensitive.
	 * @param name The name to search for.
	 * @param worldObjectLists {@link java.util.Collection}s of {@link WorldObject}s. 
	 * @return The most relevant world object, or null if nothing was found.
	 */
	private WorldObject search(String name, Collection<? extends WorldObject> ... worldObjectLists) {
		WorldObjectSearch search = new WorldObjectSearch();
		
		for (Collection<? extends WorldObject> list : worldObjectLists) {
			search.addSearchList(list);
		}
		
		List<WorldObject> results = search.search(name);
		
		if (results.size() > 0) {
			return results.get(0);
		}
		else {
			return null;
		}
	}
}
