/*
 * 31/07/08, Khasanova, Created
 *
 */
package unitn.dadt.LNSupport;


import unitn.dadt.space.SpaceView;
import unitn.dadt.internals.Action;
import unitn.dadt.internals.CompleteView;

import unitn.dadt.internals.DataView;
import unitn.dadt.internals.Operator;


public class LNCompleteView extends CompleteView {


    /**
     * Create complete view
     * @param cv
     */
    public LNCompleteView(CompleteView cv) {
        super(cv);
    }

    /**
     * Create complete view containing only DADT DataView
     * @param dv
     */
    public LNCompleteView(DataView dv) {
        super(dv);
    }

    /**
     * Create complete view containing DataView and SpaceView
     * @param sv
     * @param dv
     * @param spaceInvocation
     */
    public LNCompleteView(SpaceView sv, DataView dv, boolean spaceInvocation) {
        super(sv, dv, spaceInvocation);
    }

    /**
     * Create complete view containing only DADT SpaceView
     * @param sv
     */
    public LNCompleteView(SpaceView sv) {
        super(sv);
    }

	
	/**
	 * Create a selection Operator depending on the parameter operatorDescr
	 * (non-Javadoc)
	 * @see DADT.CompleteView#getOperator(java.lang.String, DADT.Action, java.lang.Object)
	 */
	public Operator getOperator(String operatorDescr, Action action, Object operatorParameter) {
		if (operatorDescr == "all")
 		{	
 			return new SelectorAll(action);
 		}
 		else if (operatorDescr == "any")
 		{
 			// return new SelectorAny(action); 
 		}
		System.out.println("Operator " + operatorDescr + " is not supported by the prototype");
		return null;
	}
}