import com.simtechdata.waifupnp.UPnP;
import util.Bind;
import util.Protocol;

import java.io.IOException;
import java.util.ArrayList;

public class CLIPnP {

    private ArrayList<Bind> bindings;

    public CLIPnP() {
        bindings = new ArrayList<>();
    }

    public CLIPnP(Config config) {
        bindings = new ArrayList<>();
        openAllPorts(config.getBindings());
    }

    // Checks if a binding is already present
    public Bind containsBind(Bind bind){
        for(Bind b : bindings){
            // If the binding already exists within the list, return true.
            if(b.equals(bind)){
                return b;
            }
        }
        return null;
    }

    public void closePortByIndex(int index){
        System.out.println((bindings.get(index)).close());
        bindings.remove(index);
    }

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

    // Opens all bindings from a list, and adds all of them to the bindings list
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

    // Gets registration status on a port and it's open status
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

    // Kills a port, regardless if it exists or not.
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

    // Must be called before starting.
    public static boolean isUPnPEnabled(){
        return UPnP.isUPnPAvailable();
    }

    // Saves current configuration a file
    public void save(String dir){
        new Config(bindings).saveToFile(dir);
    }

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

    // Close all ports and tell the user.
    public void close(){
        for(Bind bind : bindings){
            System.out.println(bind.close());
        }
    }
}
