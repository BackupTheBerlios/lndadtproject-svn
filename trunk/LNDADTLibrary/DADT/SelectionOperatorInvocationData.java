/*
 * Created on Sep 15, 2005
 *
 */
package DADT;


/**
 * This class represent the Invocation data used by selection operators: it contains
 * an action as a parameter.
 * @author migliava
 *
 */
//TODO: we could use the same approach of properties here and Serialize directly the
//operator so that we do not need any additional parameter encoding.

public class SelectionOperatorInvocationData extends InvocationData {

    protected Action action;

    /**
     * @param operator
     * @param view
     * @param action
     */
    public SelectionOperatorInvocationData(String operator, CompleteView view, Action action) {
        super(operator, view);
        this.operator = operator;
        this.sv = view.getSpaceView();
        this.dv = view.getDataView();
        this.action = action;
    }
    
    
    protected Operator getNewOperator() {
        try {
            return (Operator) Class.forName(operator).getConstructor(new Class[] {Action.class}).newInstance(new Object[] { action });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //!! added to be consistent with LN support
    /**
     * @param operator
     * @param view
     * @param action
     */
    public void updateSelectionOperatorInvocationData(String operator, CompleteView view, Action action) {
        this.operator = operator;
        this.sv = view.getSpaceView();
        this.dv = view.getDataView();
        this.action = action;
    }
}
