/*
 * 01/07/08, Khasanova, Created
 */
package unitn.dadt.LNSupport;


/*
import polimi.ln.neighborhoodDefs.AtomicPredicate;
import polimi.ln.neighborhoodDefs.ConjunctiveNeighborhood;
import polimi.ln.neighborhoodDefs.Neighborhood;
*/

import unitn.dadt.internals.Action;
import unitn.dadt.internals.Operator;
import unitn.dadt.LNSupport.LNCompleteView;



/**
 * Distributed Node manager that provides link between DADT layer and LN communication layer on the distributed application. 
 * @author G.Khasanova
 */
public class DistrNodeMgr {
    
	private Operator selector;
	
	/**
 	 * @param predicates 
 	 * @param dataview 
 	 * @param pcNodeId
 	 * @param action 
 	 */
	/*
 	public void requestData(String selectorDescr, AtomicPredicate[] predicates, LNCompleteView DADTview, int pcNodeId, Action action, String DADTClassName) {
 		
 		selector = DADTview.getOperator(selectorDescr, action, null);
 		
 		selector.performRemoteLN(DADTview, (Neighborhood)(new ConjunctiveNeighborhood (predicates)), DADTClassName, pcNodeId);

 	}
 	*/
 	
	
    /**
	 * @param expTree
	 * @param masterProperty 
	 * @return
	 */
 	/*
	public static AtomicPredicate[] definePredicates(ExpressionTree expTree, Property masterProperty){
		
		ArrayList predicates =  expTree.traverseExpTree(new ArrayList(), null, masterProperty.getClassName());
		return (AtomicPredicate[]) predicates.toArray(new AtomicPredicate[predicates.size()]);  

	}
	*/
}
