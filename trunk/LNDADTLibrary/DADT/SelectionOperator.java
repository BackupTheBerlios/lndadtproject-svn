/*
 * 14/09/05, Migliava, Created
 * 31/07/08, Khasanova, modified to use DistrNodeMgr
 *
 */
package DADT;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

/**
 * @author migliava
 */
public abstract class SelectionOperator implements Operator {

    protected Action action;
    
    /**
     * Selects an action
     */
    public SelectionOperator(Action a) {
        this.action = a;
    }

    /**
     * This utility function is used to invoke the action of the operator to the
     * adt instances passed as a parameter and return the ResultData[] of the invocation
     * 
     * It is required that the selection operator sends the resulting array
     * to the initiator using network-specific protocols.
     * 
     * @param ADTs
     * @return
     */
   
    protected Vector evaluateAction(Vector ADTs) {
        Vector resultList = new Vector();
        for (Enumeration e = ADTs.elements(); e.hasMoreElements();) {
            Object el = (Object) e.nextElement();
            resultList.addElement(/*new ResultData(*/action.evaluate(el)/*,DADTMgr.mgr.getAdtIdentificator().getID(el))*/);
        }
        return resultList;
    }
    
}
