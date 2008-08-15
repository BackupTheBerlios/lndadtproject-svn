/**
 * Created on @date
 */
package unitn.dadt.LNSupport;

import java.lang.ref.Reference;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
/*
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
*/

import unitn.dadt.internals.Identificator;
import unitn.dadt.internals.IdentifiedADT;
import unitn.dadt.internals.RuntimeReference;

/**
 * @author Khasanova
 *
 */
public class LNADTIdentification implements Identificator {

	
	Hashtable fromIDtoADTs = new Hashtable() ;
	Hashtable fromADTsToIDs = new Hashtable();
    
	
    long counter = 0;
	
	/**
	 * Create LNADT Identificator, to be used by LNADTMgr on each Sensor Node
 	 */
	public LNADTIdentification() {
		super();
	}



	/**
	 * @see DADT.Identificator#getADT(java.io.Serializable)
	 */
	public Object getADT(Object adtId) {

        Object object = fromIDtoADTs.get(adtId);
 
        return ((Reference) object).get();
	}

	/**
	 * @see DADT.Identificator#getID(java.lang.Object)
	 */
	public Object getID(Object adtInstance) {
		
		RuntimeReference runtimeRef = new RuntimeReference(this);
        
        if (fromADTsToIDs.containsKey(runtimeRef)) 
        {
        	//return (Serializable) fromADTsToIDs.get(runtimeRef);
        	return (Object) fromADTsToIDs.get(runtimeRef);
        }
        
        else 
        {
            ADTIdentification ADTID = new ADTIdentification(counter ++);
            
            fromADTsToIDs.put(runtimeRef,ADTID);
            fromIDtoADTs.put(ADTID,runtimeRef);
        }
        
        //return (Serializable) fromADTsToIDs.get(runtimeRef);
        return (Object) fromADTsToIDs.get(runtimeRef);
	}

	/** (non-Javadoc)
	 * @see DADT.Identificator#getIDs(java.util.Collection)
	 */
	/* java ME doesn't support Collections
	public Collection getIDs(Collection adtCollection) {
        Collection result = new ArrayList();
        for (Iterator iter = adtCollection.iterator(); iter.hasNext();) {
            Object el = (Object) iter.next();
            result.add(getID(el));
        }
        return result;
	}
	*/
	public Vector getIDs(Vector adtCollection) {
        Vector result = new Vector();
        for (Enumeration e = adtCollection.elements(); e.hasMoreElements();) {
            Object el = (Object) e.nextElement();
            result.addElement(getID(el));
        }
        return result;
	}

	/** (non-Javadoc)
	 * @see DADT.Identificator#getIdentifiedADTs(java.util.Collection)
	 */
	/* javaME doesn't support Collections
	public Collection getIdentifiedADTs(Collection adtCollection) {
        Collection result = new ArrayList();
        for (Iterator iter = adtCollection.iterator(); iter.hasNext();) {
            Object el = (Object) iter.next();
            result.add(new IdentifiedADT(el,getID(el)));
        }
        return result;
	}
	*/
	public Vector getIdentifiedADTs(Vector adtCollection) {
        Vector result = new Vector();
        for (Enumeration e = adtCollection.elements(); e.hasMoreElements();) {
            Object el = (Object) e.nextElement();
            result.addElement(new IdentifiedADT(el,getID(el)));
        }
        return result;
	}
}
