package ml.pyroneon.commands;

import ml.pyroneon.Main;
import ml.pyroneon.util.Console;

/**
 * Handles all interactions with the network, currently just for viewing information.
 */
public class Network {

    /**
     * Executes the corresponding helper method based on parameters passed in.
     * @param args Parsed list of each argument.
     */
    public static void execute(String[] args){
        if(args.length <= 1){
            help();
            return;
        }
        if(args[1].equals("list")){
            list();
            return;
        }
        Console.sendSyntaxError(Network.class.getSimpleName());
    }

    /**
     * Lists network info.
     */
    private static void list(){
        Main.cliPnP.printNetworkInfo();
    }

    /**
     * Displays some helpful information about this command.
     */
    public static void help(){
        Console.println("""
                \t network - Interact with the network.
                \t\t network list - View network information.""");
    }
}
