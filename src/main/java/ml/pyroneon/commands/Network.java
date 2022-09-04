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
        if(args[1].equals("list")){
            list();
            return;
        }
        Console.sendSyntaxError(Network.class.getName());
    }

    /**
     * Lists network info.
     */
    private static void list(){
        Main.cliPnP.printNetworkInfo();
    }
}
