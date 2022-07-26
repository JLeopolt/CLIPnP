package ml.pyroneon;

import com.simtechdata.waifupnp.UPnP;
import ml.pyroneon.util.Bind;
import ml.pyroneon.util.Protocol;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles interactions with UPnP, such as opening ports and closing them. Also handles importing and opening Config file ports.
 */
public class CLIPnP {

    private ArrayList<Bind> bindings;

    /**
     * Constructor without any config file provided. No ports are open by default.
     */
    public CLIPnP() {
        bindings = new ArrayList<>();
    }

    /**
     * Creates a new CLIPnP and opens some starter ports based on the config file provided.
     * @param config The configuration to use.
     */
    public CLIPnP(Config config) {
        bindings = new ArrayList<>();
        openAllPorts(config.getBindings());
    }

    /**
     * Checks if a Bind already exists in the current config.
     * @param bind The object to search for.
     * @return The binding object if it was found, otherwise returns null if not found.
     */
    public Bind containsBind(Bind bind){
        for(Bind b : bindings){
            // If the binding already exists within the list, return true.
            if(b.equals(bind)){
                return b;
            }
        }
        return null;
    }

    /**
     * Closes a port and removes it from current config based on it's index in the current config.
     * @param index The position to remove from current config.
     */
    public void closePortByIndex(int index){
        System.out.println((bindings.get(index)).close());
        bindings.remove(index);
    }

    /**
     * Opens a new port and registers it with the current config.
     * @param protocol The protocol to use.
     * @param port The port to use. Must be within range of 0-65535 or the constructor will send a soft error message.
     */
    public void openPort(Protocol protocol, int port){
        // If outside of range
        if(port < 0 || port > 65535){
            System.out.println("(ERROR) Invalid port number. Accepted range: 0-65535.");
            return;
        }

        // Ensure this binding doesnt already exist.
        Bind newBind = new Bind(protocol, port);
        if(containsBind(newBind) != null){
            System.out.println("(ERROR) Binding already exists.");
            return;
        }

        // If map successful, add to config.
        if(newBind.connect()){
            bindings.add(newBind);
        }
    }

    /**
     * Registers and opens each individual bind from a list of binds.
     * @param binds The list of binds to use.
     * @return Returns the number of binds which were SUCCESSFULLY added. (Not total size of bind list)
     */
    public int openAllPorts(ArrayList<Bind> binds){
        int no = 0;
        for(Bind bind : binds){
            // Ignore existing bindings.
            if(!bindings.contains(bind)){
                bind.connect();
                bindings.add(bind);
                no++;
            }
        }
        return no;
    }

    /**
     * Checks if a port is registered in current config and whether it's still open.
     * @param protocol The protocol to check.
     * @param port The port number to be checked. Can be out of range.
     * @return Returns a string informing the client whether it was registered or not, and it's open status.
     */
    public String queryPort(Protocol protocol, int port){
        Bind bind = containsBind(new Bind(protocol,port));
        if(bind != null){
            return "(i) Binding registered as "+bind;
        }
        else{
            String toReturn = "(i) Binding unregistered. Port open: ";
            if(protocol == Protocol.TCP){
                return toReturn + UPnP.isMappedTCP(port);
            }
            else{
                return toReturn + UPnP.isMappedUDP(port);
            }
        }
    }

    /**
     * Kills a port, regardless if it exists or not.
     * @param protocol The protocol to search.
     * @param port The port ot search for, can be out of range.
     */
    public void closePort(Protocol protocol, int port){
        Bind bind = containsBind(new Bind(protocol,port));
        if(bind != null){
            bindings.remove(bind);
            System.out.println(bind.close() + " and removed it from config.");
        }
        else{
            boolean success;
            if(protocol == Protocol.TCP){
                success = UPnP.closePortTCP(port);
            }
            else{
                success = UPnP.closePortUDP(port);
            }

            if(success){
                System.out.println("(i) Successfully closed port "+protocol+":"+port);
            }
            else{
                System.out.println("(ERROR) Could not close port "+protocol+":"+port);
            }
        }
    }

    /**
     * Adds all the binds from a configuration file into the current config, opening all of them.
     * @param filepath The filepath (Must be a specific .JSON file, not a dir) to open as config file.
     */
    public void addConfig(String filepath) {
        try {
            // Read the config data
            Config toAdd = Config.readFromFile(filepath);
            // Open all the ports
            int no = openAllPorts(toAdd.getBindings());
            System.out.println("(i) Successfully added ("+no+") new bindings.");
        } catch (IOException e) {
            System.out.println("(ERROR) Could not get config data from file: "+filepath);
        }
    }

    /**
     * Should be called before using other methods. The isUPnPAvailable() method blocks for a while when it's been called for
     * the first time, so it's best to call it once at the start to prevent any holdup on future interactions.
     * @return True if UPnP service is enabled for this router, false otherwise.
     */
    public static boolean isUPnPEnabled(){
        return UPnP.isUPnPAvailable();
    }

    /**
     * Saves the current configuration list of binds to a file.
     * @param dir The directory to save to, not a specific file. Saves as "config.clip"
     */
    public void save(String dir){
        new Config(bindings).saveToFile(dir);
    }

    /**
     * Prints out some software, network, and port info.
     * @param version The version of the software.
     */
    public void printInfo(String version){
        Main.printVersionInfo(version);
        System.out.println();

        // Network Info
        System.out.println("\t Network Info:");
        System.out.println("\t\t Local IP: "+UPnP.getLocalIP());
        System.out.println("\t\t Public IP: "+UPnP.getExternalIP());
        System.out.println("\t\t Default Gateway: "+UPnP.getDefaultGatewayIP());
        System.out.println();

        // Print all bindings.
        System.out.println("\t Bindings:");
        int counter = 1;
        for(Bind bind : bindings){
            System.out.println("\t\t "+counter+". "+bind.toString());
        }
        System.out.println();
    }

    /**
     * Closes all ports, without removing them from the current config. Informs the user of each port's
     * status and whether they were successfully closed or not.
     */
    public void close(){
        for(Bind bind : bindings){
            System.out.println(bind.close());
        }
    }
}
