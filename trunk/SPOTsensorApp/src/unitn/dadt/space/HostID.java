/*
 * Created on Aug 9, 2005
 *
 */
package unitn.dadt.space;

import java.util.Random;

/**
 * @author migliava
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HostID {
    
    int id;
    
    private HostID(int id) { this.id = id; };
    
    public static HostID getHostID() { return new HostID((new Random().nextInt())); }
    
    public boolean equals(Object h) {
        if (h instanceof HostID)
            return ((HostID)h).id == this.id;
        else
            return false;
    }
    
    public int hashCode() {
        return id;
    }
    
}
