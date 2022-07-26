package ml.pyroneon;

import org.json.JSONArray;
import org.json.JSONObject;
import ml.pyroneon.util.Bind;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Used to track binds, mainly to save them in file form and read them back later.
 */
public class Config extends JSONObject {

    /**
     * Creates a brand-new config file, used for saving.
     * @param bindings The configuration state, all the binds that should be saved.
     */
    public Config(ArrayList<Bind> bindings){
        JSONArray jarray = new JSONArray(bindings);
        put("binds", jarray);
    }

    /**
     * @param line A serial JSON string.
     */
    public Config(String line){
        super(line);
    }

    /**
     * Creates and returns a new Config object from a filepath.
     * @param filepath Should point to a .JSON object
     * @return Returns a new Config object filled with all binds from the provided file.
     * @throws IOException In case the file can't be opened or isn't legible.
     */
    public static Config readFromFile(String filepath) throws IOException {
        return new Config(Files.readString(Path.of(filepath)));
    }

    /**
     * Saves the Configuration to a JSON File.
     * @param directory A filepath to a directory where the JSON file should be saved. The file will be named 'config.clip'
     */
    public void saveToFile(String directory){
        String path = directory+"/config.clip";
        try (FileWriter file = new FileWriter(path)) {
            //We can write any JSONArray or JSONObject instance to the file
            this.write(file);
            System.out.println("(i) Saved config to "+path);
        } catch (IOException e) {
            System.out.println("(ERROR) Failed to save config to "+path+". Did you use single quotes?");
        }
    }

    /**
     * @return A list of each bind included in the configuration.
     */
    public ArrayList<Bind> getBindings(){
        ArrayList<Bind> toReturn = new ArrayList<>();
        for(Object obj : (JSONArray)get("binds")){
            toReturn.add(new Bind(obj.toString()));
        }
        return toReturn;
    }
}
