package ring.commands.nc;

/**
 * Class representing the result of a command. It encapsulates data to send back
 * to the CommandSender (if any), as well as the success or failure status of
 * the command.
 * 
 * @author projectmoon
 * 
 */
public class CommandResult {
	//Text to be return on success.
	private String text;
	
	//text to be returned on fail.
	private String failText;
	
	//did the command "fail" or "succeed" to be executed?
	private boolean successful;
	
	//Whether or not to even return data.
	private boolean returnData;

	public CommandResult() {
		text = "";
		failText = "[R][GREEN]You can't do that.[R][WHITE]";
		successful = false;
	}

	public void clearText() {
		text = "";
	}

	public void setFailText(String text) {
		failText = text;
	}

	public void addText(String txt) {
		text += txt;
	}

	public void setText(String txt) {
		text = txt;
	}

	public void setSuccessful(boolean b) {
		successful = b;
	}

	public String getText() {
		if (successful)
			return text;
		else
			return failText; 
	}
	
	/**
	 * Returns whether or not to return data. A CommandSender
	 * should not send data back to its client if this is set
	 * to false. It may be false for a number of reasons; though
	 * usually it is because data sending gets handled within the command
	 * in a different way than normal (the movement system, for example).
	 * @return
	 */
	public boolean getReturnData() {
		return returnData;
	}
	
	/**
	 * Sets whether or not to return data from this CommandResult. Protected because
	 * only the Command package needs to worry about this.
	 * @param returnData
	 */
	protected void setReturnData(boolean returnData) {
		this.returnData = returnData;
	}

}
