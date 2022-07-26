package ml.pyroneon;

import ml.pyroneon.util.Protocol;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The main class of the entire program, which handles user commands and filepaths. The main method within
 * this class should be the main method used by the Jar file.
 */
public class Main {

    /**
     * Gets the filepath as a String by parsing a message until it finds the filepath between 2 single quotes.
     * This works great by easily targeting the useful content, but current implementation requires the user
     * to always include the single quotes even if there are no spaces in their filepath.
     *
     * @param content The raw content message to-be-processed, which may contain more than just the filepath and it's quotes.
     * @return Returns the filepath without single quotes. Returns null if no filepath was found. (Which may occur if the user
     * forgets to put single quotes around the filepath, so error message should remind them of that)
     */
    private static String getFilepath(String content){
        Pattern pattern = Pattern.compile("'([^']*)'");
        Matcher matcher = pattern.matcher(content);
        if(matcher.find()){
            String toReturn = matcher.group();
            return toReturn.substring(1,toReturn.length()-1);
        }
        return null;
    }

    /**
     * Like the port() method, this is a helper for the CLI Main method.
     * It is called whenever the user issues a command starting with 'config' and acts in a similar fashion
     * to the port() method. For more clarification, check out the port() method's documentation.
     *
     * @param src The CLIPnP object to execute the command on, because this is a static method.
     * @param msg The WHOLE message (unparsed) which is necessary for the filepath to be extracted if it contains spaces.
     */
    private static void config(CLIPnP src, String msg){
        String[] parsed = msg.split(" ");
        try{
            if(parsed[1].equals("add")){
                src.addConfig(getFilepath(msg));
            }
            else if(parsed[1].equals("save")){
                src.save(getFilepath(msg));
            }
            else{
                throw new IndexOutOfBoundsException();
            }
        }catch(Exception e){
            System.out.println("(ERROR) /config Invalid syntax. See \"help\" for command help.");
        }
    }

    /**
     * This is a helper method for the command-line interface Main method. It is called whenever
     * the user issues a command starting with the word "port" , and is further processed by a
     * cascading if else ladder, finally calling the correct method with provided command-params,
     * or screaming at the user and giving them a soft error message.
     *
     * @param src The CLIPnP object to execute the command on, because this is a static method.
     * @param parsed The parsed user command which the main console already split apart.
     */
    private static void port(CLIPnP src, String[] parsed){
        try{
            if(parsed[1].equals("open")){
                // Determine protocol and port number
                if(parsed[2].equals("tcp")){
                    src.openPort(Protocol.TCP, Integer.parseInt(parsed[3]));
                    return;
                }
                if(parsed[2].equals("udp")){
                    src.openPort(Protocol.UDP, Integer.parseInt(parsed[3]));
                    return;
                }
                throw new IndexOutOfBoundsException();
            }
            else if(parsed[1].equals("close")){
                // Determine protocol and port number
                if(parsed[2].equals("tcp")){
                    src.closePort(Protocol.TCP, Integer.parseInt(parsed[3]));
                    return;
                }
                if(parsed[2].equals("udp")){
                    src.closePort(Protocol.UDP, Integer.parseInt(parsed[3]));
                    return;
                }
                if(parsed[2].equals("index")){
                    int index = Integer.parseInt(parsed[3]);
                    if(index > 0){
                        src.closePortByIndex(index-1);
                        return;
                    }
                }
                throw new IndexOutOfBoundsException();
            }
            else if(parsed[1].equals("query")){
                // Determine protocol and port number
                if(parsed[2].equals("tcp")){
                    System.out.println(src.queryPort(Protocol.TCP, Integer.parseInt(parsed[3])));
                    return;
                }
                if(parsed[2].equals("udp")){
                    System.out.println(src.queryPort(Protocol.UDP, Integer.parseInt(parsed[3])));
                    return;
                }
                throw new IndexOutOfBoundsException();
            }
        }catch(Exception e){
            System.out.println("(ERROR) /port Invalid syntax. See \"help\" for command help.");
        }
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
            System.out.println("(ERROR) UPnP service is not available on this network. You will have to port forward using conventional means through your router.");
            System.out.println("Thank you for using CLIPnP " + version);
            return;
        }

        // Check if a CLA specifies config file location.
        CLIPnP src;
        if(args.length >= 1){
            try {
                // Convert array of CLArgs into a formatted Filepath surrounded by single quotes.
                String build = args[0];
                for(int i = 1; i < args.length; i++){
                    build += " "+args[i];
                }
                build = getFilepath(build);

                src = new CLIPnP(Config.readFromFile(build));
            } catch (Exception e) {
                src = new CLIPnP();
                System.out.println("(WARN) Config file could not be read. Did you use single quotes? Proceeding with empty config.");
            }
        }
        else{
            src = new CLIPnP();
        }

        System.out.print("CLIPnP: ");
        String line = keyboard.nextLine();
        while(!line.equals("stop")){
            String[] parsed = line.split(" ");

            if(parsed[0].equals("help")){
                printHelp();
            }
            else if(parsed[0].equals("stop")){
                break;
            }
            else if(parsed[0].equals("config")){
                config(src, line);
            }
            else if(parsed[0].equals("info")){
                src.printInfo(version);
            }
            else if(parsed[0].equals("port")){
                port(src, parsed);
            }
            else{
                System.out.println("(WARN) Unknown command entered. Please enter \"help\" for command help.");
            }

            System.out.print("CLIPnP: ");
            line = keyboard.nextLine();
        }

        src.close();
        System.out.println("Thank you for using CLIPnP " + version);
    }

    /**
     * Prints all the available commands, some information about command-line args, and each command's syntax as well as
     * some information about what each command does.
     */
    private static void printHelp(){
        System.out.println("(i) Command-Line Arguments:");
        System.out.println("\t <filepath> - Opens config from a config file. All saved ports automatically opened. (Use single quotes)");
        System.out.println("(i) Commands:");
        System.out.println("\t stop - Gracefully stops the program. All unsaved config data will be lost. All ports will be closed.");
        System.out.println("\t help - Shows command info and syntax help.");
        System.out.println("\t info - Displays program and network info.");
        System.out.println("""
                \t config - Interact with the config
                \t\t config add <filepath> - Adds bindings from a file, to current config. (Use single quotes)
                \t\t config save <directory> - Saves current config to a directory (Use single quotes), as "config.json\"""");
        System.out.println("""
                \t port - Interact with a port. Acceptable port range is 0-65535.
                \t\t port open <tcp, udp> <0-65535> - Opens a new port based on params.
                \t\t port close index <i> - Closes registered port and removes it from config by index. Index starts from 1.
                \t\t port close <tcp, udp> <0-65535> - Forcefully closes a port, if registered, removes it from current config.
                \t\t port query <tcp, udp> <0-65535> - Get a port's status. (Open/Closed)""");
    }

    /**
     * Prints a text-art logo, the software version and some copyright/licensing information.
     * @param version The version of the software, obtained from manifest of Jar file.
     */
    public static void printVersionInfo(String version){
        System.out.println(
                        "   _____ _      _____ _____       _____  \n" +
                        "  / ____| |    |_   _|  __ \\     |  __ \\ \n" +
                        " | |    | |      | | | |__) | __ | |__) |\n" +
                        " | |    | |      | | |  ___/ '_ \\|  ___/ \n" +
                        " | |____| |____ _| |_| |   | | | | |     \n" +
                        "  \\_____|______|_____|_|   |_| |_|_|     \n");
        System.out.println("CLIPnP " + version + " Copyright (c) 2022 PyroNeon Software");
        System.out.println("Licensed under LGPL3 - \"help\" for command help.");
    }
}
