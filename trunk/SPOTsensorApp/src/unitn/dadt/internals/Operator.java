/*
 * 11/11/05, Migliava, Created
 * 30/07/08, Khasanova, Modified to be used with LN
 *
 */
package unitn.dadt.internals;

import polimi.ln.neighborhoodDefs.Neighborhood;
import polimi.ln.runtime.LogicalNeighborhoods;


public interface Operator {

    /**
     * @param view
     * @return
     */
	public Object performRemote(CompleteView view);
	
    public Object performRemoteLN(CompleteView view, Neighborhood nodes, String DADTClassName, LogicalNeighborhoods ln, int senderId);
    
    public void performLocal(InvocationData message);

}