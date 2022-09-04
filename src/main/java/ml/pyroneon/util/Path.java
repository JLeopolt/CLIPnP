package ml.pyroneon.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resolves a file/directory path from an input command.
 */
public class Path {

    /**
     * Creates a save filepath from a dir/file path.
     * @param msg The command.
     * @return The filepath. If only a directory is provided, assigns it a default name.
     */
    public static String getSaveFilepath(String msg) throws IllegalArgumentException{
        // If path could not be found, returns an error.
        String path = getPathInQuotes(msg);
        if(path == null){
            Console.sendError("Could not find specified file/dir. Did you spell everything right?");
            throw new IllegalArgumentException();
        }

        // If the path is a file, returns its path.
        File file = new File(path);
        if(file.isFile()){
            return file.getAbsolutePath();
        }
        if(file.isDirectory()){
            // Returns a unique default file name.
            return generateDefaultFilePath(file.getAbsolutePath());
        }

        // If the path given was something other than a dir or file, throw error.
        Console.sendError("Could not find specified file/dir. Did you spell everything right?");
        throw new IllegalArgumentException();
    }

    /**
     * Gets a filepath from an input command.
     * @param msg The command.
     * @return The filepath. If only a directory is provided, returns an error.
     */
    public static String getFilepath(String msg) throws IllegalArgumentException{
        // If path could not be found, returns an error.
        String path = getPathInQuotes(msg);
        if(path == null){
            Console.sendError("Could not find specified file. Did you spell everything right?");
            throw new IllegalArgumentException();
        }

        // If the path is a file, returns its path.
        File file = new File(path);
        if(file.isFile()){
            return file.getAbsolutePath();
        }
        Console.sendError("Could not find specified file. Did you spell everything right?");
        throw new IllegalArgumentException();
    }

    /**
     * Gets a directory path from an input command.
     * @param msg The command.
     * @return The directory path. If a filepath is provided, ignores it, and returns just the directory path.
     */
    public static String getDirectory(String msg) throws IllegalArgumentException{
        // If path could not be found, returns an error.
        String path = getPathInQuotes(msg);
        if(path == null){
            Console.sendError("Could not find specified directory. Did you spell everything right?");
            throw new IllegalArgumentException();
        }

        File dir = new File(path);
        if(dir.isDirectory()){
            return dir.getAbsolutePath();
        }
        // If the file is not a directory, return its parent's directory.
        return dir.getParent();
    }

    /**
     * Gets the filepath as a String by parsing a message until it finds the filepath between 2 single quotes.
     * This works great by easily targeting the useful content, but current implementation requires the user
     * to always include the single quotes even if there are no spaces in their filepath.
     *
     * @param content The raw content message to-be-processed, which may contain more than just the filepath and it's quotes.
     * @return Returns the filepath without single quotes. Returns null if no filepath was found. (Which may occur if the user
     * forgets to put single quotes around the filepath, so error message should remind them of that)
     */
    public static String getPathInQuotes(String content){
        Pattern pattern = Pattern.compile("'([^']*)'");
        Matcher matcher = pattern.matcher(content);
        if(matcher.find()){
            String toReturn = matcher.group();
            return toReturn.substring(1,toReturn.length()-1);
        }
        return null;
    }

    /**
     * Generates a unique filename for the config file.
     * @param directory The directory path to use.
     * @return The new file path, including its custom name and the directory path.
     */
    private static String generateDefaultFilePath(String directory) throws IllegalArgumentException{
        final String prefix = "config";

        // Check if path is valid.
        File dir = new File(directory);
        if(!dir.isDirectory()){
            Console.sendError("Could not find specified directory. Did you spell everything right?");
            throw new IllegalArgumentException();
        }

        // Searches for a free file name until one is found.
        for(int i = 0; true; i++){
            // Omit the counter if it's 0.
            String namespace = prefix;
            if(i != 0){
                namespace += i;
            }

            // Determine if that file exists already. If not, save it, otherwise, continue searching.
            File toSave = new File(directory+"/"+namespace+".clip");
            if(!toSave.exists()){
                return toSave.getPath();
            }
        }
    }
}
