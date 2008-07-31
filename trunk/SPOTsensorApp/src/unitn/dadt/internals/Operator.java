/*
 * 11/11/05, Migliava, Created
 * 30/07/08, Khasanova, Modified to be used with LN
 *
 */
package unitn.dadt.internals;

/*
import polimi.ln.neighborhoodDefs.Neighborhood;
*/
public interface Operator {

    /**
     * @param view
     * @return
     */
    public Object performRemote(CompleteView view);
    
    //public Object performRemoteLN(CompleteView view, Neighborhood nodes, String DADTClassName, /*temp*/ int simNodeId);
    
    
    public void performLocal(InvocationData message);

}