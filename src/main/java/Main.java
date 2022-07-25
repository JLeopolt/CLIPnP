/*
    The main class which handles CLI interface.
 */

import util.Protocol;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static void config(CLIPnP src, String[] parsed){
        try{
            if(parsed[1].equals("add")){
                src.addConfig(parsed[2]);
            }
            else if(parsed[1].equals("save")){
                src.save(parsed[2]);
            }
            else{
                throw new IndexOutOfBoundsException();
            }
        }catch(Exception e){
            System.out.println("(ERROR) /config Invalid syntax. See \"help\" for command help.");
        }
    }

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

    public static void main(String[] args){
        Scanner keyboard = new Scanner(System.in);

        String version = Main.class.getPackage().getImplementationVersion();
        if(version == null){
            version = "Unknown Version";
        }

        printVersionInfo(version);

        if(!CLIPnP.isUPnPEnabled()){
            System.out.println("(ERROR) UPnP service is not available on this network. You will have to port forward using conventional means through your router.");
            System.out.println("Thank you for using CLIPnP " + version);
            return;
        }

        // Check if a CLA specifies config file location.
        CLIPnP src;
        if(args.length > 1){
            try {
                src = new CLIPnP(Config.readFromFile(args[0]));
            } catch (IOException e) {
                src = new CLIPnP();
                System.out.println("(WARN) Config file could not be read. Proceeding with blank config.");
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
                config(src, parsed);
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

    private static void printHelp(){
        System.out.println("(i) Command-Line Arguments:");
        System.out.println("\t <filepath> - Opens config from a config file. All saved ports automatically opened.");
        System.out.println("(i) Commands:");
        System.out.println("\t stop - Gracefully stops the program. All unsaved config data will be lost. All ports will be closed.");
        System.out.println("\t help - Shows command info and syntax help.");
        System.out.println("\t info - Displays program and network info.");
        System.out.println("""
                \t config - Interact with the config
                \t\t config add <filepath> - Adds bindings from a file, to current config.
                \t\t config save <directory> - Saves current config to a directory, as 'config.json'""");
        System.out.println("""
                \t port - Interact with a port. Acceptable port range is 0-65535.
                \t\t port open <tcp, udp> <0-65535> - Opens a new port based on params.
                \t\t port close <index> - Closes registered port and removes it from config by index. Index starts from 1.
                \t\t port close <tcp, udp> <0-65535> - Forcefully closes a port, if registered, removes it from current config.
                \t\t port query <tcp, udp> <0-65535> - Get a port's status. (Open/Closed)""");
    }

    public static void printVersionInfo(String version){
        System.out.println(
                        "   _____ _      _____ _____       _____  \n" +
                        "  / ____| |    |_   _|  __ \\     |  __ \\ \n" +
                        " | |    | |      | | | |__) | __ | |__) |\n" +
                        " | |    | |      | | |  ___/ '_ \\|  ___/ \n" +
                        " | |____| |____ _| |_| |   | | | | |     \n" +
                        "  \\_____|______|_____|_|   |_| |_|_|     ");
        System.out.println("CLIPnP " + version + " Copyright (c) 2022 PyroNeon Software");
        System.out.println("Licensed under LGPL3 - \"help\" for command help.");
    }
}
