/*
 * Created on Aug 11, 2005
 *
 */
package unitn.dadt.internals;

/*import java.io.Serializable;*/

import unitn.dadt.space.SpaceView;

/**
 * @author migliava
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InvocationData /* implements Serializable */{
    protected String operator;
    protected DataView dv;
    protected SpaceView sv;
    
    public DataView getDv() {
        return dv;
    }
    /**
     * This method must be redefined in subclasses which uses some 
     * parameter in the operator constructor
     * @return
     */
    //TODO How we construct the Operator on the remote side?
    //1 we shoud distinguish between local and remote construction
    //2 we need to get the appropriate constructor on the remote side
    //3 we need a way to send the parameters on the remote side
    //The actual mechanism based on overriding getNewOperator in the corresponding Message 
    //is dangerous and error prone as often when implementing a new operator
    //with non-default constructor the programmer (me) forgets to ovverride this method
    //plus we need to define a new message even when it is not necessary.
    //serialization of the operator could solve this problems.
    //Is using a package constructor for the remote (the "Local" side) Operator 
    //useful?
    protected Operator getNewOperator() {
        try {
            return (Operator) Class.forName(operator).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param operator
     * @param view
     * @param action
     */
    public InvocationData(String operator, CompleteView view) {
        super();
        this.operator = operator;
        this.sv = view.getSpaceView();
        this.dv = view.getDataView();
    }

    /**
     * 
     */
    public void runOperator() {
        getNewOperator().performLocal(this);
    }
    public SpaceView getSv() {
        return sv;
    }
    
    public boolean useOperatorMessage(String operatorName) {
    	if ((operator.indexOf(operatorName) != -1 ))
    	{ 
    		return true;
    	}
    	else 
    	{
    		return false;
    	}	
    }
}
