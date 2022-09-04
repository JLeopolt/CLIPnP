package ml.pyroneon.util;

/**
 * Used to maintain consistent formatting for the Console output of command-line execution.
 */
public class Console {

    /**
     * Sends an error message.
     * @param context Message details.
     */
    public static void sendError(String context){
        System.out.println(getError(context));
    }

    /**
     * Generates an Error message.
     * @param context Message details.
     * @return The completed message.
     */
    public static String getError(String context){
        return "(ERROR) "+context;
    }

    /**
     * Sends a warning message.
     * @param context Message details.
     */
    public static void sendWarning(String context){
        System.out.println(getWarning(context));
    }

    /**
     * Generates a Warning message.
     * @param context Message details.
     * @return The completed message.
     */
    public static String getWarning(String context){
        return "(WARN) "+context;
    }

    /**
     * Sends a command response message.
     * @param context Message details.
     */
    public static void sendResponse(String context){
        System.out.println(getResponse(context));
    }

    /**
     * Generates a response message.
     * @param context Message details.
     * @return The completed message.
     */
    public static String getResponse(String context){
        return "(i) "+context;
    }

    /**
     * Sends a status/config informative message.
     * @param context Message details.
     */
    public static void sendDetails(String context){
        System.out.println(getDetails(context));
    }

    /**
     * Generates a status/config informative message.
     * @param context Message details.
     * @return The completed message.
     */
    public static String getDetails(String context){
        return "(INFO) "+context;
    }

    /**
     * Prompts the user for input. Preceded with a colon, printed on same line..
     */
    public static void promptUser(){
        System.out.print("CLIPnP: ");
    }

    /**
     * Currently just a wrapper for console's println() method.
     * @param content Message details.
     */
    public static void println(String content){
        System.out.println(content);
    }

    /**
     * Sends a Syntax Error.
     * @param classname Optional. Specifies the class sending the error.
     */
    public static void sendSyntaxError(String classname){
        // If no classname specified, ignore it.
        String prefix = "";
        if(!classname.isEmpty()){
            prefix = "["+classname+"] ";
        }

        System.out.println(getError( prefix + "Invalid syntax. See \"help\" for command help."));
    }
}
