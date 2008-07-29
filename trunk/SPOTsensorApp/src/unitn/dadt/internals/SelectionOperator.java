/*
 * Created on Sep 14, 2005
 *
 */
package unitn.dadt.internals;

/*import java.util.Collection;*/
import java.util.Enumeration;
/*
import java.util.Iterator;
import java.util.LinkedList;
*/
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

//    abstract protected ResultData[] performRemote(CompleteView view);
//    abstract protected void performLocal(SelectionOperatorInvocationData message);
   
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
    /* j2ME doesn't support Collections
     * protected ResultData[] performActionLocally(Collection ADTs) {
        LinkedList<ResultData> resultList = new LinkedList<ResultData>();
        Iterator iter = ADTs.iterator();
        while (iter.hasNext()) {
            Object el = (Object) iter.next();
            resultList.add(new ResultData(action.evaluate(el),DADTMgr.mgr.getAdtIdentificator().getID(el)));
        }
        return resultList;
    }
    */
    protected Vector performActionLocally(Vector ADTs) {
        Vector resultList = new Vector();
        
        for (Enumeration e = ADTs.elements(); e.hasMoreElements();)
        {
        	ResultData res = (ResultData)action.evaluate(e.nextElement());
        	resultList.addElement(new ResultData(res.getData(),
        										(String)DADTMgr.mgr.getAdtIdentificator().getID(e.nextElement())));
        }	
        return resultList;
        /*
        Iterator iter = ADTs.iterator();
        while (iter.hasNext()) {
            Object el = (Object) iter.next();
            resultList.add(new ResultData(action.evaluate(el),DADTMgr.mgr.getAdtIdentificator().getID(el)));
        }
        return resultList;
        */
    }
    
}
