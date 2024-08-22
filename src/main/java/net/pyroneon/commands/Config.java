package net.pyroneon.commands;

import net.pyroneon.Main;
import net.pyroneon.util.Console;
import net.pyroneon.util.Path;

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
        if(parsed.length <= 1){
            help();
            return;
        }
        if(parsed[1].equals("add")) {
            add(msg);
            return;
        }
        else if (parsed[1].equals("save")) {
            save(msg);
            return;
        }

        Console.sendSyntaxError(Config.class.getSimpleName());
    }

    /**
     * Adds a new config file.
     * @param msg Takes the entire unparsed message as parameter.
     */
    private static void add(String msg){
        Main.cliPnP.addConfig(Path.getFilepath(msg));
    }

    /**
     * Saves the config to a file.
     * @param msg Takes the entire unparsed message as parameter.
     */
    private static void save(String msg){
        Main.cliPnP.save(Path.getSaveFilepath(msg));
    }

    /**
     * Displays some helpful information about this command.
     */
    public static void help(){
        Console.println("""
                \t config - Interact with the config.
                \t\t config add <filepath> - Adds bindings from a file, to current config. (Use single quotes)
                \t\t config save <directory> - Saves current config to a directory (Use single quotes), as "config.json\"""");
    }
}
