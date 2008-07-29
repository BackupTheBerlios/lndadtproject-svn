/*
 * Created on Aug 9, 2005
 *
 */
package unitn.dadt.space;

/**
 * @author migliava
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NetworkProperty_broadcast {
    
    /* (non-Javadoc)
     * @see DADT.Action#evaluate(java.lang.Object)
     */
    
    public boolean evaluate(Object o) {
        return true;
    }
    
    /* (non-Javadoc)
     * @see DADT.Property#getDADTClass()
     */
    public Class getDADTClass() {
        return Network.class;
    }
}