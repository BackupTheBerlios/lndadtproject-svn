/**
 * Created on @date
 */
package DADT.LNSupport;

import java.io.Serializable;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import DADT.Identificator;
import DADT.IdentifiedADT;
import DADT.internals.RuntimeReference;

/**
 * @author Khasanova
 *
 */
public class LNADTIdentification implements Identificator {

	
	///---- re-use
    //We need to use maps as it should be fast in both ways (and also the code is nicer ;-)
    //we need to go fromIDtoADTs whenever we use an operator that address the single instance
    //we need to go fromADTsToIDs whenever we return values from invocation (as IDs are contained in ResultData)
    
	Map<ADTIdentification, RuntimeReference> fromIDtoADTs = new HashMap<ADTIdentification, RuntimeReference>() ;
    Map<RuntimeReference, ADTIdentification> fromADTsToIDs = new HashMap<RuntimeReference, ADTIdentification> ();
    ///----
    
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
	public Object getADT(Serializable adtId) {

        Object object = fromIDtoADTs.get(adtId);
 
        return ((Reference) object).get();
	}

	/**
	 * @see DADT.Identificator#getID(java.lang.Object)
	 */
	public Serializable getID(Object adtInstance) {
		
		RuntimeReference runtimeRef = new RuntimeReference(this);
        
        if (fromADTsToIDs.containsKey(runtimeRef)) 
        {
        	return (Serializable) fromADTsToIDs.get(runtimeRef);
        }
        
        else 
        {
            ADTIdentification ADTID = new ADTIdentification(counter ++);
            
            fromADTsToIDs.put(runtimeRef,ADTID);
            fromIDtoADTs.put(ADTID,runtimeRef);
        }
        
        return (Serializable) fromADTsToIDs.get(runtimeRef);
	}

	/** (non-Javadoc)
	 * @see DADT.Identificator#getIDs(java.util.Collection)
	 */
	public Collection getIDs(Collection adtCollection) {
        Collection result = new ArrayList();
        for (Iterator iter = adtCollection.iterator(); iter.hasNext();) {
            Object el = (Object) iter.next();
            result.add(getID(el));
        }
        return result;
	}

	/** (non-Javadoc)
	 * @see DADT.Identificator#getIdentifiedADTs(java.util.Collection)
	 */
	public Collection getIdentifiedADTs(Collection adtCollection) {
        Collection result = new ArrayList();
        for (Iterator iter = adtCollection.iterator(); iter.hasNext();) {
            Object el = (Object) iter.next();
            result.add(new IdentifiedADT(el,getID(el)));
        }
        return result;
	}

}
