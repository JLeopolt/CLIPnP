package ml.pyroneon.commands;

import ml.pyroneon.Main;
import ml.pyroneon.util.Console;

/**
 * Handles all user interactions with the configuration settings.
 */
public class Config {

    /**
     * Executes the corresponding helper method based on parameters passed in.
     * @param msg Takes the entire unparsed message as parameter.
     */
    public static void execute(String msg){
        String[] parsed = msg.split(" ");

        if(parsed[1].equals("add")){
            add(msg);
            return;
        }
        else if(parsed[1].equals("save")){
            save(msg);
            return;
        }

        Console.sendSyntaxError(Config.class.getName());
    }

    /**
     * Adds a new config file.
     * @param msg Takes the entire unparsed message as parameter.
     */
    private static void add(String msg){
        Main.cliPnP.addConfig(Main.getFilepath(msg));
    }

    /**
     * Saves the config to a file.
     * @param msg Takes the entire unparsed message as parameter.
     */
    private static void save(String msg){
        Main.cliPnP.save(Main.getFilepath(msg));
    }
}
