/*
 * Created on Sep 29, 2005
 *
 */
package DADT.IPMulticast;

import java.io.Serializable;
import java.net.InetSocketAddress;


/**
 * @author migliava
 */
public class IPIdentifier implements Serializable {
    
    private InetSocketAddress addr;
    private long objectID; 
    /**
     * @param localSocketAddress
     */
    public IPIdentifier(InetSocketAddress localSocketAddress, long objectID) {
        addr = localSocketAddress;
        this.objectID = objectID;
    }

    protected InetSocketAddress getAddr() {
        return addr;
    }
    protected long getObjectID() {
        return objectID;
    }
    public boolean equals(Object o) {
        if (o instanceof IPIdentifier) {
            IPIdentifier id = (IPIdentifier) o;
            return (addr.equals(id.addr) && objectID==id.objectID);
        }
        else return false;
    }
    public int hashCode() {
        return addr.hashCode() + (int) (objectID % Integer.MAX_VALUE);
    }
    public String toString() {
        return addr.toString() +"::"+ objectID;
    }
}
