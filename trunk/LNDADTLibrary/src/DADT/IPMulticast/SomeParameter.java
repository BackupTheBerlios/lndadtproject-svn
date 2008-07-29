/*
 * Created on Oct 3, 2005
 *
 */
package DADT.IPMulticast;

import java.util.Collection;

import DADT.Action;

/**
 * This class represents the parameter of for the Some operator
 * @author migliava
 */
//TODO for some parameter we could simply use the 
public class SomeParameter {
    //protected Action action; 
    protected Collection ADTs; 

    /**
     * Builds a new parameter of for the Some operator
     * @param action
     * @param ts
     */
    public SomeParameter(Collection ADTs) {
        super();
        //this.action = action;
        this.ADTs = ADTs;
    }
}
