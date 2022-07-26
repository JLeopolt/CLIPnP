import org.json.JSONArray;
import org.json.JSONObject;
import util.Bind;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

// Automatically tries to open a series of binds.
public class Config extends JSONObject {

    public Config(ArrayList<Bind> bindings){
        JSONArray jarray = new JSONArray(bindings);
        put("binds", jarray);
    }

    public Config(String line){
        super(line);
    }

    // Creates and returns a new Config object from a JSON file
    public static Config readFromFile(String filepath) throws IOException {
        Config toReturn = new Config(Files.readString(Path.of(filepath)));
        //System.out.println("(i) Config file used: "+filepath);
        return toReturn;
    }

    // Saves the Configuration to a JSON File.
    public void saveToFile(String directory){
        String path = directory+"/config.json";
        try (FileWriter file = new FileWriter(path)) {
            //We can write any JSONArray or JSONObject instance to the file
            this.write(file);
            System.out.println("(i) Saved config to "+path);
        } catch (IOException e) {
            System.out.println("(ERROR) Failed to save config to "+path+". Did you use single quotes?");
        }
    }

    public ArrayList<Bind> getBindings(){
        ArrayList<Bind> toReturn = new ArrayList<>();
        for(Object obj : (JSONArray)get("binds")){
            toReturn.add(new Bind(obj.toString()));
        }
        return toReturn;
    }
}
