/*
 * Created on Sep 16, 2005
 *
 */
package unitn.dadt.internals;

/*import polimi.ln.neighborhoodDefs.AtomicPredicate;*/


/**
 * @author migliava
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface Property {
    public boolean evaluate(Object o);
    public Class getDADTClass();
    
    //*public AtomicPredicate getDescriptionForLN(Object o); //this method is used to support communication over LN 
    public String getClassName(); 				  
}