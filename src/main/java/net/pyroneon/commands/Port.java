package net.pyroneon.commands;

import net.pyroneon.Main;
import net.pyroneon.util.Console;
import net.pyroneon.util.Protocol;

/**
 * Handles user commands regarding port opening,closing,querying,etc.
 */
public class Port {

    /**
     * Calls the corresponding helper method based on parameters passed in.
     * @param args Parsed list of each argument.
     */
    public static void execute(String[] args){
        if(args.length <= 1){
            help();
            return;
        }
        if(args[1].equals("open")){
            open(args);
            return;
        }
        else if(args[1].equals("close")){
            close(args);
            return;
        }
        else if(args[1].equals("query")){
            query(args);
            return;
        }
        else if(args[1].equals("list")){
            list();
            return;
        }
        Console.sendSyntaxError(Port.class.getSimpleName());
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
                return;
            }
            else if(args[2].equals("udp")){
                Main.cliPnP.closePort(Protocol.UDP, Integer.parseInt(args[3]));
                return;
            }
            else if(args[2].equals("index")){
                int index = Integer.parseInt(args[3]);
                if(index > 0){
                    Main.cliPnP.closePortByIndex(index-1);
                }
                return;
            }
            Console.sendSyntaxError(Port.class.getSimpleName());
        }
        catch(IndexOutOfBoundsException e){
            Console.sendSyntaxError(Port.class.getSimpleName());
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

    /**
     * Displays some helpful information about this command.
     */
    public static void help(){
        Console.println("""
                \t port - Interact with a port. Acceptable port range is 0-65535.
                \t\t port open <tcp, udp> <0-65535> - Opens a new port based on params.
                \t\t port close index <i> - Closes registered port and removes it from config by index. Index starts from 1.
                \t\t port close <tcp, udp> <0-65535> - Forcefully closes a port, if registered, removes it from current config.
                \t\t port query <tcp, udp> <0-65535> - Get a port's status. (Open/Closed)
                \t\t port list - Lists all currently open ports (controlled by CLIPnP).""");
    }
}
