package net.pyroneon.commands;

import net.pyroneon.util.Console;

/**
 * Offers the client detailed support with command syntax and functionality.
 */
public class Help {

    public static void execute(String[] args){
        // If no args, prints a broad help section.
        if(args.length <= 1){
            printHelp();
            return;
        }
        if(args[1].equals("cla")){
            cla();
            return;
        }
        Console.sendSyntaxError(Help.class.getSimpleName());
    }

    /**
     * Prints all the available commands, some information about command-line args, and each command's syntax as well as
     * some information about what each command does.
     */
    private static void printHelp(){
        // CLA Help
        cla();

        // Misc. Command Help
        Console.sendDetails("Commands:");
        Console.println("\t stop - Gracefully stops the program. All unsaved config data will be lost. All ports will be closed.");
        Console.println("\t info - Displays program and network info.");

        // Main Command Help
        help();
        Network.help();
        Config.help();
        Port.help();
    }

    /**
     * Displays some helpful information about this command.
     */
    private static void help(){
        Console.println("""
                \t help - Shows command info and syntax help.
                \t\t help cla - Information about setting up Command-Line-Arguments.""");
    }

    /**
     * Information about setting up Command-Line-Arguments.
     */
    private static void cla(){
        Console.sendDetails("Command-Line Arguments:");
        Console.println("\t <filepath> - Opens saved bindings from a config file. Always surround the path with single quotes, E.g. 'C:/Users/yourname/myconfig.clip'.");
    }
}
