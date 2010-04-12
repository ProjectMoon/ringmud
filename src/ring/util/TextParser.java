package ring.util;

/**
 * <p>Title: RingMUD Codebase</p>
 * <p>Description: RingMUD is a java codebase for a MUD with a working similar to DikuMUD</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: RaiSoft/Thermetics</p>
 * @author Jeff Hair
 * @version 1.0
 */
import java.util.*;

//TODO refactor this into a non static class. will allow for better code
//in parseOutgoingData
public class TextParser {
    //Constants
    public static final int SCREEN_WIDTH = 80;
    private static final Map<String, String> colors = new HashMap<String, String>();
    
    static {
        //Black.
        colors.put("[BLACK]", "\033[30m");
        
        //Blue.
        colors.put("[BLUE]", "\033[34m");
        
        //Cyan.
        colors.put("[CYAN]", "\033[36m");
        
        //Green.
        colors.put("[GREEN]", "\033[32m");
        
        //Magenta.
        colors.put("[MAGENTA]", "\033[35m");
        
        //Red.
        colors.put("[RED]", "\033[31m");
        
        //White.
        colors.put("[WHITE]", "\033[39m");
        
        //Yellow.
        colors.put("[YELLOW]", "\033[33m");
        
        //Bold.
        colors.put("[B]", "\033[1m");
        
        //Regular.
        colors.put("[R]", "\033[0m");        
    }

    public TextParser() {}

    public static String trimNewlines(String text) {
    	//Don't need to make any changes to an empty string.
        if (text.length() == 0) return text;
        
        char[] textChars = text.toCharArray();
        int start = 0;
        int end = text.length() - 1;
        
        //first remove \ns from the beginning.
        while(textChars[start] == '\n')
            start++;
        
        //now the end.
        while(textChars[end] == '\n')
            end--;
        
        //we now have the start and end of the string where there are no newlines.
        return text.substring(start, end + 1);
    }
    
    public static String parseOutgoingData(String data, boolean screenWidthParsing) {
    	if (screenWidthParsing) {
    		return parseOutgoingData(data);
		}
		else {
			return parseOutgoingDataNoSWP(data);
		}
	}
	
	//Explicitly does not insert newlines.
	private static String parseOutgoingDataNoSWP(String data) {
      StringTokenizer st = new StringTokenizer(data, "\n[], \t", true);
        StringBuilder res = new StringBuilder();
        int count = 0;
        
        while (st.hasMoreTokens()) {
            String text = st.nextToken();
           
            //handle possibility of inline commands
            if (text.equals("[")) {
                text += st.nextToken() + st.nextToken();
                String replaceValue = colors.get(text);
                
                //if there was a replacement match found, use that.
                //otherwise it's not a command.
                if (replaceValue != null)
                    text = replaceValue;
                else
                    count += text.length();               
            }
            
            //handle \ns directly.
            else if (text.equals("\n")) {
                res.append("\r\n");
                count = 0;
                continue;
            }
                        
            //normal data handling
            else {
                count += text.length();
            }
                        
            //finally we can append normally, if we reach this part.
            res.append(text);         
        }
        
        //make sure we return to default.
        res.append(colors.get("[R]"));
        
        return res.toString();
	}
    
    //This method is used to replace the color tags with the correct ANSI codes.
    public static String parseOutgoingData(String data) {
        StringTokenizer st = new StringTokenizer(data, "\n[], \t", true);
        StringBuilder res = new StringBuilder();
        int count = 0;
        
        while (st.hasMoreTokens()) {
            String text = st.nextToken();
           
            //handle possibility of inline commands
            if (text.equals("[")) {
                text += st.nextToken() + st.nextToken();
                String replaceValue = colors.get(text);
                
                //if there was a replacement match found, use that.
                //otherwise it's not a command.
                if (replaceValue != null)
                    text = replaceValue;
                else
                    count += text.length();               
            }
            
            //handle \ns directly.
            else if (text.equals("\n")) {
                res.append("\n");
                count = 0;
                continue;
            }
                        
            //normal data handling
            else {
                count += text.length();
            }
            
            if (count >= SCREEN_WIDTH) {
            	res.append(text);
            	int offset = count - SCREEN_WIDTH;
            	int maxIndex = res.length() - 1;
            	char c = res.charAt(maxIndex - offset);
            	
            	//rewind until we find a character that is whitespace
            	while (Character.isWhitespace(c) == false) {
            		offset++;
            		c = res.charAt(maxIndex - offset);
				}
				
				//Leave whitespace on the line above by moving offset back one
				if (offset != 0) {
	           		offset--;
	           	}
            	
            	//Now we should have a safe insert.
            	res.insert(maxIndex - offset, "\r\n");
                count = offset;
                continue;
            }
            
            //finally we can append normally, if we reach this part.
            res.append(text);         
        }
        
        //make sure we return to default.
        res.append(colors.get("[R]"));
                
        //It is FAR simpler to remove spaces on the beginning/end of lines this way
        //instead of in the parsing loop.
        //This funky character class is actually the \s class with the \n removed.
        //This ensures newlines in the suffix from communicators are left alone.
        //See: http://java.sun.com/docs/books/tutorial/essential/regex/pre_char_classes.html
        String ret = res.toString();
        ret =  ret.replaceAll("\n[ \\t\\x0B\\f\\r]+", "\n");
        return ret;
    }
    
    /**
     * Removes all formatting from the given String. Useful for
     * sending data to players who have ANSI turned off, as well
     * as other internal uses.
     * @param displayName
     * @return
     */
	public static String stripFormatting(String text) {
		for (String color : colors.keySet()) {
			//These two lines turn the color string
			//into a regex.
			color = color.replace("[", "\\[");
			color = color.replace("]", "\\]");
			
			//Now we can destroy all colors via regex power!
			text = text.replaceAll(color, "");
		}
		return text;
	}
	
	public static void main(String[] args) {
		System.out.println(stripFormatting("[WHITE]blah[CYAN]"));
	}
}
