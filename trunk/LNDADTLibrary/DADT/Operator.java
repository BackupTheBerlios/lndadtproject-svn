/*
 * Created on Nov 11, 2005
 *
 */
package DADT;

import polimi.ln.neighborhoodDefs.Neighborhood;

public interface Operator {

    /**
     * @param view
     * @return
     */
    public Object performRemote(CompleteView view);
    public Object performRemoteLN(CompleteView view, Neighborhood nodes, String DADTClassName, /*temp*/ int simNodeId);
    
    
    public void performLocal(InvocationData message);

}