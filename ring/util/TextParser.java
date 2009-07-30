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

public class TextParser {
    //Constants
    public static final int SCREEN_WIDTH = 80;
    private static final TreeMap<String, String> colors = new TreeMap<String, String>();
    
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
        colors.put("[WHITE]", "\033[37m");
        
        //Yellow.
        colors.put("[YELLOW]", "\033[33m");
        
        //Bold.
        colors.put("[B]", "\033[1m");
        
        //Regular.
        colors.put("[R]", "\033[22m");        
    }

    public TextParser() {}

    public static int countParameters(String str) {
        //return zero for parameters with zero parameters; fixes a bug...
        if (str.indexOf("()") != -1) {
            return 0;
        }
        char[] chars = str.toCharArray();
        int commas = 0;

        for (int c = 0; c < chars.length; c++) {
            if (chars[c] == ',') {
                commas++;
            }
        }

        commas++; //account for that last parameter

        return commas;
    }

    public static String trimNewlines(String text) {
        if (text.length() == 0) return "";
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
                res.append("\r\n");
                count = 0;
                continue;
            }
                        
            //normal data handling
            else {
                count += text.length();
            }
            
            if (count > SCREEN_WIDTH) {
                res.append("\r\n");
                count = 0;
            }
            
            res.append(text);         
        }
        
        //make sure we return to white.
        res.append(colors.get("[WHITE]"));
        return res.toString();
    }

    public static String indefiniteArticle(String text) {
        String[] tokens = text.split(" ");

        String plurality = (tokens[0].substring(tokens[0].length() - 1));

        System.out.println(plurality);

        return "meh";
    }
}
