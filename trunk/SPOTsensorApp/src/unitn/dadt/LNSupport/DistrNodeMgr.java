/*
 * 01/07/08, Khasanova, Created
 */
package unitn.dadt.LNSupport;


/*
import polimi.ln.neighborhoodDefs.AtomicPredicate;
import polimi.ln.neighborhoodDefs.ConjunctiveNeighborhood;
import polimi.ln.neighborhoodDefs.Neighborhood;
*/

import java.util.Vector;

import polimi.ln.neighborhoodDefs.ConjunctiveNeighborhood;
import polimi.ln.neighborhoodDefs.Neighborhood;
import polimi.ln.neighborhoodDefs.Predicate;
import polimi.ln.nodeAttributes.Node;
import polimi.ln.runtime.LNDeliver;
import polimi.ln.runtime.LogicalNeighborhoods;
import unitn.dadt.internals.Action;
import unitn.dadt.internals.DataView;
import unitn.dadt.internals.ExpressionTree;
import unitn.dadt.internals.Operator;
import unitn.dadt.internals.Property;
import unitn.dadt.LNSupport.LNCompleteView;



/**
 * Distributed Node manager that provides link between DADT layer and LN communication layer on the distributed application. 
 * @author G.Khasanova
 */
public class DistrNodeMgr {

	
	private Operator selector;
	public LogicalNeighborhoods ln;
    public final static int lnClientNodeId = 0;
	
	
	/**
 	 * @param predicates 
 	 * @param dataview 
 	 * @param pcNodeId
 	 * @param DADTaction 
 	 */
	
 	public void performDADTRequest(String selectorDescr, ExpressionTree expTree, String DADTaction, String DADTClassName) {
 		
 		
		// define DADT dataview over given ExpressionTree						
		LNCompleteView lnView = new LNCompleteView(new DataView(expTree));
		
		// define LN predicates over DADT dataview
		Predicate[] LNpredicates = defineLNPredicates(expTree);
		
		
		// specify a selector
 		selector = lnView.getOperator(selectorDescr, DADTaction, null);
 		
 		// perform a remote execution of DADT action over nodes in the constructed LN
 		selector.performRemoteLN(lnView, (Neighborhood)(new ConjunctiveNeighborhood (LNpredicates)), DADTClassName, ln, lnClientNodeId);

 	}
 	
 	
    /**
	 * @param expTree
	 * @param masterProperty 
	 * @return
	 */
 	
	public static Predicate[] defineLNPredicates(ExpressionTree expTree){
		
		Predicate[] LNpredicates =  (Predicate[]) expTree.traverseExpTree(new Vector());
		
		return LNpredicates;  
	}
	
}
