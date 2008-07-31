/*
 * 18/07/08, Khasanova, Created
 * 
 */
package DADT.LNSupport;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import polimi.ln.examples.swans.SimulationReferences;
import polimi.ln.neighborhoodDefs.AtomicPredicate;
import polimi.ln.neighborhoodDefs.Neighborhood;
import polimi.ln.runtime.LogicalNeighborhoods;

///import sensorExample.Mica;

import DADT.Action;
import DADT.CompleteView;
import DADT.DADTMgr;
import DADT.InvocationData;
import DADT.ResultData;
import DADT.SelectionOperator;
import DADT.SelectionOperatorIntf;

public class SelectorAll extends SelectionOperator {
	
	/**
     * @param action
     */
    public SelectorAll(Action action) {
        
    	super(action);						// initialise with a defined action
    }
    
    /** 
     * the cast should be done by the translator. 
     * @return an Object containing a Collection <ResultData>
     */

    public Object performRemoteLN(CompleteView view, Neighborhood nodes, String DADTClassName, int simNodeId) {
        try 
        {
            
        	// get reference to the logical neighborhood 
    		LogicalNeighborhoods ln = SimulationReferences.getLN(simNodeId);		//[*]
    		SimulationReferences.getNodeInfo(simNodeId).debugPrint("Request is sent");		//[*]

    		
    		SimulationReferences.getNodeInfo(simNodeId).debugPrint("DADT class name is " + DADTClassName);		//[*]

    		
    		// PC-node sends request message into logical neighborhood (WSN)
    		ln.send(new LNSupportRequestMsg(simNodeId, 
    										view, 
    										action,
    										DADTClassName), 
    										new Neighborhood[] { nodes });
            
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


