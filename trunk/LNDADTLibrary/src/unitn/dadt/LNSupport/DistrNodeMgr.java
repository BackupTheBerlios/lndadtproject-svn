/*
 * Created on July 01, 2008
 * @author Khasanova
 */
package unitn.dadt.LNSupport;


import java.util.ArrayList;

import polimi.ln.examples.swans.SimulationReferences;
import polimi.ln.neighborhoodDefs.AtomicPredicate;
import polimi.ln.neighborhoodDefs.ConjunctiveNeighborhood;
import polimi.ln.neighborhoodDefs.Neighborhood;

import polimi.ln.runtime.LogicalNeighborhoods;


import DADT.Action;
import DADT.DataView;
import DADT.ExpressionTree;
import DADT.Property;
import DADT.LNSupport.*;



/**
 * Distributed Node manager that provides link between DADT layer and LN communication layer on the distributed application. 
 * @author G.Khasanova
 */
public class DistrNodeMgr {
    
    //static Logger l = Logger.getLogger("InfoLogger"); 			// debug tool
	
 	/**
 	 * @param predicates 
 	 * @param dataview 
 	 * @param pcNodeId
 	 * @param action 
 	 */

 	public void sendRequest(AtomicPredicate[] predicates, DataView dataview, int pcNodeId, Action action, String DADTClassName) {

    
 		
		// define a neighborhood of nodes based on the LN predicates 
 		
		Neighborhood nodes = new ConjunctiveNeighborhood (predicates);
		
		// get reference to the logical neighborhood 
		LogicalNeighborhoods ln = SimulationReferences.getLN(pcNodeId);		//[*]
		SimulationReferences.getNodeInfo(pcNodeId).debugPrint("Request is sent");		//[*]

		
		SimulationReferences.getNodeInfo(pcNodeId).debugPrint("DADT class name is " + DADTClassName);		//[*]

		
		// PC-node sends request message into logical neighborhood (WSN)
		ln.send(new LNSupportRequestMsg(pcNodeId, 
										dataview, 
										action,
										DADTClassName), // create a message which contains action to be executed
										new Neighborhood[] { nodes });
		
		
 	}
 	
	
    /**
	 * @param expTree
	 * @param masterProperty 
	 * @return
	 */
 	
	public static AtomicPredicate[] definePredicates(ExpressionTree expTree, Property masterProperty){
		
		ArrayList predicates =  expTree.traverseExpTree(new ArrayList(), null, masterProperty.getClassName());
		return (AtomicPredicate[]) predicates.toArray(new AtomicPredicate[predicates.size()]);  

	}
	
}
