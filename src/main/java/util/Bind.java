package util;

import com.simtechdata.waifupnp.UPnP;
import org.json.JSONObject;

public class Bind extends JSONObject {

    public Bind(Protocol np, int port) throws IllegalArgumentException{
        put("protocol", np);
        put("port", port);
    }

    public Bind(String line){
        super(line);
    }

    public Protocol getProtocol(){
        return Protocol.valueOf(get("protocol").toString());
    }

    public int getPort(){
        return getInt("port");
    }

    public boolean connect(){
        boolean success;
        if(getProtocol() == Protocol.TCP){
            success = UPnP.openPortTCP(getPort());
        }
        else{
            success = UPnP.openPortUDP(getPort());
        }

        if(success){
            System.out.println("(i) Successfully opened "+getProtocol()+" port: "+getPort());
            return true;
        }
        System.out.println("(ERROR) Failed to open "+getProtocol()+" port: "+getPort());
        return false;
    }

    public String close(){
        boolean success;
        if(getProtocol() == Protocol.TCP){
            success = UPnP.closePortTCP(getPort());
        }
        else{
            success = UPnP.closePortUDP(getPort());
        }

        if(success){
            return "(i) Successfully closed "+getProtocol()+" port: "+getPort();
        }
        return "(ERROR) Failed to close "+getProtocol()+" port: "+getPort();
    }

    // Check if this Bind matches another.
    public boolean equals(Bind other){
        return other.getProtocol() == getProtocol() && other.getPort() == getPort();
    }

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
