/*
 * 18/07/08, Khasanova, Created
 * 
 */
package unitn.dadt.LNSupport;

import java.util.Vector;

import polimi.ln.neighborhoodDefs.Neighborhood;
import polimi.ln.nodeAttributes.Node;
import polimi.ln.runtime.LogicalNeighborhoods;

/*
import polimi.ln.examples.swans.SimulationReferences;
import polimi.ln.neighborhoodDefs.AtomicPredicate;
import polimi.ln.neighborhoodDefs.Neighborhood;
import polimi.ln.runtime.LogicalNeighborhoods;
*/


import unitn.dadt.internals.Action;
import unitn.dadt.internals.CompleteView;
import unitn.dadt.internals.InvocationData;
import unitn.dadt.internals.SelectionOperator;

public class SelectorAll extends SelectionOperator {
	
	String actionId;
	/**
     * @param action
	 * @return 
     */
	
    public SelectorAll(Action action) {
        super(action);
    }

	
    public SelectorAll(String actionId) {
    	super();						// Initialize with a defined action
    	this.actionId = actionId;
    	
    }
    
    /** 
     * the cast should be done by the translator. 
     * @return an Object containing a Collection <ResultData>
     */

    
    public Object performRemoteLN(CompleteView view, Neighborhood nodes, String DADTClassName, LogicalNeighborhoods ln, int senderId) {
        try 
        {
        	// create request message
        	LNSupportRequestMsg reqMsg = new LNSupportRequestMsg(senderId, view, actionId, DADTClassName);
					
        	// send message over logical neighbourhood
    		ln.send(reqMsg.toByteArray(), new Neighborhood[] { nodes });
            
            return null;
            
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
    }


	public Vector performActionLocally(Vector ADTs) {
		return super.evaluateAction(ADTs);
	}

	public void performLocal(InvocationData message) {
		// TODO Auto-generated method stub
		
	}

	public Object performRemote(CompleteView view) {
		// TODO Auto-generated method stub
		return null;
	}


}


