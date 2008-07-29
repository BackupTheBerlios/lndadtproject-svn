/*
 * Created on Sep 29, 2005
 *
 */
package unitn.dadt.internals;

//import java.io.Serializable;
//import java.util.Collection;
import java.util.Vector;

/**
 * This is the common inteface implemented by Identificators. An Identificator is a component which 
 * assolves two function, first is capable of building globally unique identifiers for objects, 
 * then is also resposible to keep weak references to identified object so that when given back the 
 * identifier it is able to trace back to the corresponding object instance.
 * 
 * These identifiers are used to trace back results of invocations on multiple ADTs so that when
 * results are returned the programmer knows how to select the instance that has returned a given
 * value, and use its identifier with an operator that support invocation on a specific instance (eg some)  
 * 
 * Subclasses of Identificator are network dependent and must be able to provide Identifiers that works 
 * in accordance with the operators in that particular network plugin.
 * 
 * This functionalities are separate from BindingRegistry for two reasons. First of all the semantic of
 * the Identificator is a little more abstract in that for example there is no strong correspondence 
 * beween the export status and the presence in the table (the correspondence is only weak i.e. can
 * (actually should, according to the model) be tested manually by operators)
 * 
 * ****this is not a real danger but an important issue to be discussed the earliest the identifier
 * is now composed of two parts: the spaceADTpart and the dataADT part. The space part is useful to reach the
 * site on which data resides.... DOES IT MAKES SENSE FROM THE PAPER DUAL SPACE/DATA POINT OF VIEW or should
 * we have a single mechanism that works for both data and space? In the
 * paper we said identifiers 
 * 
 * Does it makes sense from the 
 * 
 * @author migliava
 */
public interface Identificator {

//    /**
//     * This will construct an identificator which will use addr as the
//     * identifier of the site and compose that with an object identifier to
//     * compose the global unique identifier.
//     * @param addr
//     */
    /**
     * This method will get the identifier of a given ADT. The first time it is called with 
     * the same instance it generates the identifier and stores a weak reference into the table, when it is
     * called again with the same instance it simply returns the previous identifier.
     */
	/*
    public Serializable getID(Object adtInstance); // no Serializable objects in J2ME
    */
	public Object getID(Object adtInstance);
	
    /**
     * Returns the ADT corresponing to a given Identifier
     */
	/*
    public Object getADT(Serializable adtIdentifier); // no Serializable objects in J2ME
	*/
	public Object getADT(Object adtIdentifier);
	
    /**
     * Will receive a collection of ADTs, and returns a collection of IdentifiedADTs
     * which are composed of ADTs instance together with the corresponding 
     * identifier
     */
    /*javaME doesn't suppot collections
     * public Collection getIdentifiedADTs(Collection adtCollection);
     */public Vector getIdentifiedADTs(Vector adtCollection);
    
     /**
     * Will receive a collection of ADTs, and returns a collection of identifiers 
     */
     /*javaME doesn't support Collections
    public Collection getIDs(Collection adtCollection);
    */
    public Vector getIDs(Vector adtCollection);
}	
