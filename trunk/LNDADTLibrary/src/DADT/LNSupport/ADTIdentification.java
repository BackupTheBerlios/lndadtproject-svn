/*
 * Created on July 01, 2008
 *
 */
package DADT.LNSupport;

import java.io.Serializable;
import java.net.InetSocketAddress;


/**
 * @author Khasanova
 */
public class ADTIdentification implements Serializable {
    
    private long objectID; 

    /**
     * @param objectID
     */
    public ADTIdentification(long objectID) {
        
        this.objectID = objectID;
    }

    protected long getObjectID() {
        return objectID;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        
    	if (o instanceof ADTIdentification) 
        {
            ADTIdentification id = (ADTIdentification) o;
            return  (objectID==id.objectID);
        }
        else
        	return false;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (int) (objectID % Integer.MAX_VALUE);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return "ADT instance ID = "+ objectID;
    }
}
