/*
 * Created on Aug 10, 2005
 *
 */
package DADT.IPMulticast;

import java.util.Collection;

import space.SpaceView;
import DADT.Action;
import DADT.CompleteView;
import DADT.DADTMgr;
import DADT.DIterator;
import DADT.DataView;
import DADT.Operator;
import DADT.OperatorFactory;

/**
 * @author migliava
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
//TODO add implements RegistrationListener
public class IPMulticastView extends CompleteView {


    /**
     * @param cv
     */
    public IPMulticastView(CompleteView cv) {
        super(cv);
    }

    /**
     * @param dv
     */
    public IPMulticastView(DataView dv) {
        super(dv);
    }

    /**
     * @param sv
     * @param dv
     * @param spaceInvocation
     */
    public IPMulticastView(SpaceView sv, DataView dv, boolean spaceInvocation) {
        super(sv, dv, spaceInvocation);
    }

    /**
     * @param sv
     */
    public IPMulticastView(SpaceView sv) {
        super(sv);
    }

    /**
     * @param operator
     * @return
     */
    //protected Operator getOperator(Object qualifyingExpr, String operator, Action action, Object operatorParameter) {
    public Operator getOperator(String operator, Action action, Object operatorParameter) {
        //FIXME now the action is not encoded in the operatorParameeter anymore.
        //thus we need to move actions from operatorParameters to the action parameter

        //Also in the case of iterator we should get the operator from 
        //the qualifyingExpr
        
    	
    	if (operator.equals("all")) 
    		return (Operator) new IPMAll(action); //!!return new IPMAll(action);
        
    	//!! 19-06
    	/*       
        if (operator.equals("any")) 
        	return new IPMAny(action);
        
        
        if (operator.equals("some")) 
        	return new IPMSome(action, ((SomeParameter) operatorParameter).ADTs);
        
    	
        if (operator.equals("sizeof")) 
        	return new IPMSharp();
        
      //!! 19-06
    	/* 
        if (operator.equals("is_in")) 
        	return new IPMIsIn((Collection) operatorParameter);
        */	
        	
                
        //FIXME we should probably remove the view as a parameter
        if (operator.equals("Iterator")) return new IPMIterator(this);

        throw new RuntimeException("Operator " + operator + " not supported by " + this.getClass().getName());
    }
    
}