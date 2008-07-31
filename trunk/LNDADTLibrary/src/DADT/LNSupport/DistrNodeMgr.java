/*
 * 01/07/08, Khasanova, Created
 * 30/07/08, Khasanova, Modified in order to use SelectionOperator
 */
package DADT.LNSupport;


import java.util.ArrayList;

import polimi.ln.neighborhoodDefs.AtomicPredicate;
import polimi.ln.neighborhoodDefs.ConjunctiveNeighborhood;
import polimi.ln.neighborhoodDefs.Neighborhood;

import DADT.Action;
import DADT.ExpressionTree;
import DADT.Operator;
import DADT.Property;

/**
 * Distributed Node manager that provides link between DADT layer and LN communication layer on the distributed application. 
 * @author Khasanova
 */
public class DistrNodeMgr {
    
	private Operator selector;
    /**
 	 * @param selectorDescr 
     * @param predicates 
     * @param view 
 	 * @param pcNodeId
 	 * @param action 
     * @param DADTClassName 
 	 */
 	public void requestData(String selectorDescr, AtomicPredicate[] predicates, LNCompleteView DADTview, int pcNodeId, Action action, String DADTClassName) {
 		
 		selector = DADTview.getOperator(selectorDescr, action, null);
 		
 		selector.performRemoteLN(DADTview, (Neighborhood)(new ConjunctiveNeighborhood (predicates)), DADTClassName, pcNodeId);

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
