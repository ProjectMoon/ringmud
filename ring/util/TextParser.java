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

//THIS CLASS NEEDS TO BE REWRITTEN FROM SCRATCH FOR TWO PURPOSES:
//TO NOT HAVE TO COMPLY WITH WOLFMUD'S LICENSE AGREEMENT, AND TO CLEAN THIS DAMN THING UP!
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

    public TextParser() {
    }

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
        
        /*
        int characterCount = 0;
        int wordCount = 0;
        int screenWidth = 80;
        boolean command = false;
        boolean commandProcessed = false;
        String[] inlineCommands = new String[]{"BLACK", "BLUE", "CYAN", "GREEN", "MAGENTA", "RED", "WHITE", "YELLOW", "B", "R"};

        // set a tokeniser to parse the string
        StringTokenizer tok = new StringTokenizer(data, "\n[] \t", true);

        // buffer to hold outgoing data as we build it
        StringBuffer output = new StringBuffer("\033[37m");

        // loop through each of the tokens of the string to display.
        while (tok.hasMoreTokens()) {
            data = tok.nextToken();
            // System.out.println("CURRENTLY PARSING: <" + data + ">");

            // starting or ending a command ?
            if (data.equals("[")) {
                // System.out.println("It's a command.");
                command = true;
                continue;
            }
            if (data.equals("]")) {
                // System.out.println("Ending a command.");
                command = false;
                continue;
            }

            if (data.equals("\n")) {
                // System.out.println("Reseting everything?");
                data = "\r\n";
                characterCount = 0;
                wordCount = 0;
            }

            // an inline command ?
            // System.out.println("Is it an in-line command?");
            
            commandProcessed = false;
            if (command) {
                // System.out.println("Yes! Using the crappy thing to change to the ansi code!");
                for (int x = 0; x < inlineCommands.length; x++) {
                    if (inlineCommands[x].equals(data)) {
                        switch (x) {
                            case 0:
                                data = "\033[30m";
                                break;

                            case 1:
                                data = "\033[34m";
                                break;

                            case 2:
                                data = "\033[36m";
                                break;

                            case 3:
                                data = "\033[32m";
                                break;

                            case 4:
                                data = "\033[35m";
                                break;

                            case 5:
                                data = "\033[31m";
                                break;

                            case 6:
                                data = "\033[37m";
                                break;

                            case 7:
                                data = "\033[33m";
                                break;

                            case 8:
                                data = "\033[1m";
                                break;

                            case 9:
                                data = "\033[22m";
                                break;
                        }
                        commandProcessed = true;
                        // System.out.println("Parsed the command, continuing on!");
                        break;
                    }
                }

                // if it wasn't a command display the text instead
                if (!commandProcessed) {
                    // System.out.println("It wasn't a command so we're going to display it instead.");
                    data = "[" + data + "]";
                    // System.out.println("The new data string is: " + data);
                    wordCount = data.length() + 2;
                // System.out.println("The word count is: " + wordCount);
                } else {
                    // System.out.println("We've hit an else block... wordCount = 0.");
                    wordCount = 0;
                }
            } else {
                // System.out.println("The other else block...");
                wordCount = data.length();
            //  System.out.println("The wordCount is: " + wordCount);
            }

            if (screenWidth != 0 &&
                    (characterCount == 0 || characterCount == screenWidth) &&
                    data.equals(" ")) { continue;
            }

            // System.out.println("Adding the wordCount to characterCount...");
            characterCount += wordCount;
            // System.out.println("characterCount is: " + characterCount);

            if (screenWidth != 0 && characterCount > screenWidth) {
                //  System.out.println("The line is too big, so we're indenting it now...");
                output.append("\r\n");
                characterCount = wordCount;
            //  System.out.println("We've finished indenting.");
            }

            // System.out.println("Appending <data> to <output>");
            output.append(data);
        // System.out.println("<output> so far: " + output);
        // System.out.println("----------------------------------------------");
        // System.out.println();
        }

        output.append("\033[37m");

        return output.toString();*/
    }

    public static String indefiniteArticle(String text) {
        String[] tokens = text.split(" ");

        String plurality = (tokens[0].substring(tokens[0].length() - 1));

        System.out.println(plurality);

        return "meh";
    }

    //insert method.
    //This method inserts a string into a given position of another string.
    //IT WILL INSERT IN BETWEEN THE POSITION GIVEN AND THE ONE AFTER POS (POS + 1).
    public static String insert(String theString, String stringToBeInserted, int pos) {
        String beforeString = theString.substring(0, pos + 1);
        String endString = theString.substring(pos + 1, theString.length() - 1);
        beforeString += stringToBeInserted;
        beforeString += endString;

        return beforeString;
    }

    public static void main(String[] args) {
        System.out.println("HP: 5/5 Class: Wizard Level: 1 Class Type: Arcane Caster".length());
    }
}
