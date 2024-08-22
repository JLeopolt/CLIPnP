package net.pyroneon;

import net.pyroneon.commands.Config;
import net.pyroneon.commands.Help;
import net.pyroneon.commands.Network;
import net.pyroneon.commands.Port;
import net.pyroneon.util.Console;
import net.pyroneon.util.Path;

import java.util.Scanner;

/**
 * The main class of the entire program, which handles user commands and filepaths. The main method within
 * this class should be the main method used by the Jar file.
 */
public class Main {

    public static CLIPnP cliPnP;

    /**
     * Prints a text-art logo, the software version and some copyright/licensing information.
     * @param version The version of the software, obtained from manifest of Jar file.
     */
    public static void printVersionInfo(String version){
        Console.println(
                        "   _____ _      _____ _____       _____  \n" +
                        "  / ____| |    |_   _|  __ \\     |  __ \\ \n" +
                        " | |    | |      | | | |__) | __ | |__) |\n" +
                        " | |    | |      | | |  ___/ '_ \\|  ___/ \n" +
                        " | |____| |____ _| |_| |   | | | | |     \n" +
                        "  \\_____|______|_____|_|   |_| |_|_|     \n");
        Console.sendDetails("CLIPnP " + version + " Copyright (c) 2024 PyroNeon Software");
        Console.sendDetails("Licensed under LGPL3 - \"help\" for command help.");
        Console.sendWarning("This is a BETA version. Contact us at PyroNeon.net if you experience an issue.");
    }

    /**
     The main method for the entire program. Handles the console side of the interface, by first getting the Implementation Version from the Jar's
     Manifest. Next, it calls the isUPnPEnabled() method from WaifUPnP to determine if UPnP is actually supported by the client's router. If not, then
     the program terminates, otherwise, the program will proceed to check for a config file provided as a command-line argument (CLA) which if found, will
     be applied and sent to the CLIPnP class on initialization. Finally, the program will enter it's main phase; where it constantly prompts the user for
     input as a command-line interface. The user's commands are parsed and appropriate responses are issued accordingly. The workload of the commands is
     split up into their own separate helper methods.
     */
    public static void main(String[] args){
        Scanner keyboard = new Scanner(System.in);

        String version = Main.class.getPackage().getImplementationVersion();
        if(version == null){
            version = "IDE-version";
        }

        printVersionInfo(version);

        if(!CLIPnP.isUPnPEnabled()){
            Console.sendError("UPnP service is not available on this network. You will have to port forward using conventional means through your router.");
            Console.sendResponse("Thank you for using CLIPnP " + version);
            return;
        }

        // Check if a CLA specifies config file location.
        if(args.length >= 1){
            try {
                // Turns the command-line-args into a single string.
                String build = args[0];
                for(int i = 1; i < args.length; i++){
                    build += " "+args[i];
                }

                // The command-line-arg filepath should be in quotes.
                build = Path.getPathInQuotes(build);

                cliPnP = new CLIPnP(Configuration.readFromFile(build));
            } catch (Exception e) {
                cliPnP = new CLIPnP();
                Console.sendWarning("Config file could not be read. Did you use single quotes? Proceeding with empty config.");
            }
        }
        else{
            cliPnP = new CLIPnP();
        }

        Console.promptUser();
        String line = keyboard.nextLine();
        while(!line.equals("stop")){
            String[] parsed = line.split(" ");
            switch (parsed[0]) {
                case "help", "?" -> Help.execute(parsed);
                case "config" -> Config.execute(line);
                case "info" -> cliPnP.printInfo(version);
                case "port" -> Port.execute(parsed);
                case "network" -> Network.execute(parsed);
                default -> Console.sendWarning("Unknown command entered. Please enter \"help\" for command help.");
            }
            Console.promptUser();
            line = keyboard.nextLine();
        }

        cliPnP.close();
        Console.sendResponse("Thank you for using CLIPnP " + version);
    }


}
