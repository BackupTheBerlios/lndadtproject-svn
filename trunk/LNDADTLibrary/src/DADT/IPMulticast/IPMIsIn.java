/*
 * Created on Oct 3, 2005
 *
 */
package DADT.IPMulticast;

import java.util.Collection;

import DADT.CompleteView;
import DADT.Operator;
import DADT.InvocationData;


//FIXME implement and test this class
/**
 * @author migliava
 */
public class IPMIsIn implements Operator {

    /**
     * @param collection
     */
    public IPMIsIn(Collection set) {

    }

    /* (non-Javadoc)
     * @see DADT.Operator#performRemote(DADT.CompleteView)
     */
    public Object performRemote(CompleteView view) {
        return null;
    }

    /* (non-Javadoc)
     * @see DADT.Operator#performLocal(DADT.InvocationData)
     */
    public void performLocal(InvocationData message) {

    }
}
