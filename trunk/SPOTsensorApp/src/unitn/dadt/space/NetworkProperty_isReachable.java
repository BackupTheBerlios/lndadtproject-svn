/*
 * Created on Sep 14, 2005
 *
 */
package unitn.dadt.space;

/*import java.io.Serializable;*/

/*import polimi.ln.neighborhoodDefs.AtomicPredicate;*/

import unitn.dadt.internals.Property;

/**
 * @author migliava
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NetworkProperty_isReachable implements Property /*, Serializable */{
    int hops;
    /**
     * @param hops
     */
    public NetworkProperty_isReachable(int hops) {
        super();
        this.hops = hops;
    }
    /* (non-Javadoc)
     * @see DADT.Action#evaluate(java.lang.Object)
     */
    
    public boolean evaluate(Object o) {
        return (hops > 0);
    }
    
    /* (non-Javadoc)
     * @see DADT.Property#getDADTClass()
     */
    public Class getDADTClass() {
        return Network.class;
    }

	public String getClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see DADT.Property#getDescriptionForLN(java.lang.Object)
	 */
	/*
	public AtomicPredicate getDescriptionForLN(Object o) {
		// TODO Auto-generated method stub
		return null;
	}
	*/

}
