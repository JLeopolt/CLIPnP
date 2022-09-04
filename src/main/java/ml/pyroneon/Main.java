package ml.pyroneon;

import ml.pyroneon.commands.Config;
import ml.pyroneon.commands.Network;
import ml.pyroneon.commands.Port;
import ml.pyroneon.util.Console;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The main class of the entire program, which handles user commands and filepaths. The main method within
 * this class should be the main method used by the Jar file.
 */
public class Main {

    public static CLIPnP cliPnP;

    /**
     * Gets the filepath as a String by parsing a message until it finds the filepath between 2 single quotes.
     * This works great by easily targeting the useful content, but current implementation requires the user
     * to always include the single quotes even if there are no spaces in their filepath.
     *
     * @param content The raw content message to-be-processed, which may contain more than just the filepath and it's quotes.
     * @return Returns the filepath without single quotes. Returns null if no filepath was found. (Which may occur if the user
     * forgets to put single quotes around the filepath, so error message should remind them of that)
     */
    public static String getFilepath(String content){
        Pattern pattern = Pattern.compile("'([^']*)'");
        Matcher matcher = pattern.matcher(content);
        if(matcher.find()){
            String toReturn = matcher.group();
            return toReturn.substring(1,toReturn.length()-1);
        }
        return null;
    }

    /**
     * Prints all the available commands, some information about command-line args, and each command's syntax as well as
     * some information about what each command does.
     */
    private static void printHelp(){
        Console.sendDetails("Command-Line Arguments:");
        Console.println("\t <filepath> - Opens config from a config file. All saved ports automatically opened. (Use single quotes)");
        Console.println("(i) Commands:");
        Console.println("\t stop - Gracefully stops the program. All unsaved config data will be lost. All ports will be closed.");
        Console.println("\t help - Shows command info and syntax help.");
        Console.println("\t info - Displays program and network info.");
        Console.println("""
                \t network - Interact with the network.
                \t\t network list - View network information.""");
        Console.println("""
                \t config - Interact with the config.
                \t\t config add <filepath> - Adds bindings from a file, to current config. (Use single quotes)
                \t\t config save <directory> - Saves current config to a directory (Use single quotes), as "config.json\"""");
        Console.println("""
                \t port - Interact with a port. Acceptable port range is 0-65535.
                \t\t port open <tcp, udp> <0-65535> - Opens a new port based on params.
                \t\t port close index <i> - Closes registered port and removes it from config by index. Index starts from 1.
                \t\t port close <tcp, udp> <0-65535> - Forcefully closes a port, if registered, removes it from current config.
                \t\t port query <tcp, udp> <0-65535> - Get a port's status. (Open/Closed)
                \t\t port list - Lists all currently open ports (controlled by CLIPnP).""");
    }

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
        Console.sendDetails("CLIPnP " + version + " Copyright (c) 2022 PyroNeon Software");
        Console.sendDetails("Licensed under LGPL3 - \"help\" for command help.");
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
            version = "Developer Version";
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
                // Convert array of CLArgs into a formatted Filepath surrounded by single quotes.
                String build = args[0];
                for(int i = 1; i < args.length; i++){
                    build += " "+args[i];
                }
                build = getFilepath(build);

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

            if(parsed[0].equals("help")){
                printHelp();
            }
            else if(parsed[0].equals("config")){
                Config.execute(line);
            }
            else if(parsed[0].equals("info")){
                cliPnP.printInfo(version);
            }
            else if(parsed[0].equals("port")){
                Port.execute(parsed);
            }
            else if(parsed[0].equals("network")){
                Network.execute(parsed);
            }
            else{
                Console.sendWarning("Unknown command entered. Please enter \"help\" for command help.");
            }

            Console.promptUser();
            line = keyboard.nextLine();
        }

        cliPnP.close();
        Console.sendResponse("Thank you for using CLIPnP " + version);
    }


}
