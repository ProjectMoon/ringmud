package ring.scripting;

import bsh.*;
import java.io.*;

public abstract class Script {
	/*
	This is the abstract base class for running BeanShell script files (.bsh) in RingMUD. It extends a few subclasses, those
	currently being EventScript and FactoryScript. EventScript handles MUDEvent objects for the event-driven	programming aspect 
	of the MUD. For example, a Mobile can fire a Mobile_On_Move event and an EventScript will be created and run to deal with it.
	
	The FactoryScript class is mostly used at start-up to load factory objects. It will run the script, get the object from it, and
	put it in the factory classes.
	*/
	
	//Instance variables:
	protected Interpreter script; //the interpreter for this script.
	protected boolean error; //is there an error in this script? subclasses should find ways to set this variable
	
	//abstract methods that must be implemented by extending classes
	public abstract void runScript();
	
	
	//some useful public/protected methods that subclasses can use for error checking and stuff
	protected final boolean checkFilename(String filename) {
		//current allowed file extensions: .bsh, .conf
		return ((filename.matches(".+\\.bsh")) || (filename.matches(".+\\.conf")));
	}

	public final boolean exitedSuccessfully() {
		return error;
	}
}
