package net.pyroneon.util;

import com.simtechdata.waifupnp.UPnP;
import org.json.JSONObject;

/**
 * This is a data structure class, which is basically just a JSONObject with some fancy methods to keep things organized and compartmentalized.
 */
public class Bind extends JSONObject {

    /**
     * Creates a Bind object.
     * @param np The protocol to use.
     * @param port The port to use. Is assumed to be within range when called.
     */
    public Bind(Protocol np, int port) {
        put("protocol", np);
        put("port", port);
    }

    /**
     * Builds a Bind object from a serial JSON object as String.
     * @param line The serialized JSONObject, read from a file.
     */
    public Bind(String line){
        super(line);
    }

    /**
     * @return Returns the protocol to be used.
     */
    public Protocol getProtocol(){
        return Protocol.valueOf(get("protocol").toString());
    }

    /**
     * @return Returns the port to be used.
     */
    public int getPort(){
        return getInt("port");
    }

    /**
     * Attempts to open a port with the Protocol and Port held by this object.
     * @return True if successful, and False otherwise. Will also print more specific information to console before returning.
     */
    public boolean connect(){
        boolean success;
        if(getProtocol() == Protocol.TCP){
            success = UPnP.openPortTCP(getPort());
        }
        else{
            success = UPnP.openPortUDP(getPort());
        }

        if(success){
            Console.sendResponse("Successfully opened "+getProtocol()+" port: "+getPort());
            return true;
        }
        Console.sendError("Failed to open "+getProtocol()+" port: "+getPort());
        return false;
    }

    /**
     * Attempts to close the UPnP port based on Protocol and Port held by this object. Will not deregister the port from the config, however.
     * @return A success/failure message, to be printed out to the console.
     */
    public String close(){
        boolean success;
        if(getProtocol() == Protocol.TCP){
            success = UPnP.closePortTCP(getPort());
        }
        else{
            success = UPnP.closePortUDP(getPort());
        }

        if(success){
            return Console.getResponse("Successfully closed "+getProtocol()+" port: "+getPort());
        }
        return Console.getError("Failed to close "+getProtocol()+" port: "+getPort());
    }

    /**
     * @param other Another Bind object.
     * @return True if they map to the same port/protocol, false otherwise.
     */
    public boolean equals(Bind other){
        return other.getProtocol() == getProtocol() && other.getPort() == getPort();
    }

    /**
     * @return Useful information about the port and protocol this object holds.
     */
    public String toString(){
        boolean isMapped;
        if(getProtocol() == Protocol.TCP){
            isMapped = UPnP.isMappedTCP(getPort());
        }
        else{
            isMapped = UPnP.isMappedUDP(getPort());
        }
        return getProtocol() + ": " + getPort() + " Open: "+ isMapped;
    }
}
