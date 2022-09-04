package ml.pyroneon.commands;

import ml.pyroneon.Main;
import ml.pyroneon.util.Console;
import ml.pyroneon.util.Protocol;

/**
 * Handles user commands regarding port opening,closing,querying,etc.
 */
public class Port {

    /**
     * Calls the corresponding helper method based on parameters passed in.
     * @param args Parsed list of each argument.
     */
    public static void execute(String[] args){
        if(args[1].equals("open")){
            open(args);
        }
        else if(args[1].equals("close")){
            close(args);
        }
        else if(args[1].equals("query")){
            query(args);
        }
        else if(args[1].equals("list")){
            list();
        }
        else{
            Console.sendSyntaxError(Port.class.getName());
        }
    }

    /**
     * Opens a port.
     * @param args Command arguments.
     */
    private static void open(String[] args){
        try{
            // Determine protocol and port number
            if(args[2].equals("tcp")){
                Main.cliPnP.openPort(Protocol.TCP, Integer.parseInt(args[3]));
            }
            else if(args[2].equals("udp")){
                Main.cliPnP.openPort(Protocol.UDP, Integer.parseInt(args[3]));
            }
        }
        catch(IndexOutOfBoundsException e){
            Console.sendError("Value out of bounds. Triple-check your syntax, see \"help\" for command help.");
        }
    }

    /**
     * Closes a port.
     * @param args Command arguments.
     */
    private static void close(String[] args){
        try{
            // Determine protocol and port number
            if(args[2].equals("tcp")){
                Main.cliPnP.closePort(Protocol.TCP, Integer.parseInt(args[3]));
            }
            else if(args[2].equals("udp")){
                Main.cliPnP.closePort(Protocol.UDP, Integer.parseInt(args[3]));
            }
            else if(args[2].equals("index")){
                int index = Integer.parseInt(args[3]);
                if(index > 0){
                    Main.cliPnP.closePortByIndex(index-1);
                }
            }
        }
        catch(IndexOutOfBoundsException e){
            Console.sendError("Value out of bounds. Triple-check your syntax, see \"help\" for command help.");
        }
    }

    /**
     * Checks a port's open/close status, and it's registration status.
     * @param args Command arguments.
     */
    private static void query(String[] args){
        try {
            // Determine protocol and port number
            if(args[2].equals("tcp")){
                Console.sendResponse(Main.cliPnP.queryPort(Protocol.TCP, Integer.parseInt(args[3])));
            }
            else if(args[2].equals("udp")){
                Console.sendResponse(Main.cliPnP.queryPort(Protocol.UDP, Integer.parseInt(args[3])));
            }
        }
        catch(IndexOutOfBoundsException e){
            Console.sendError("Value out of bounds. Triple-check your syntax, see \"help\" for command help.");
        }
    }

    /**
     * Lists open/active ports.
     */
    private static void list(){
        Main.cliPnP.printBindings();
    }
}
