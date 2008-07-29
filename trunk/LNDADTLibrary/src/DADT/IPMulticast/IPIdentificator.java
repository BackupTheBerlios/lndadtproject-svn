/*
 * Created on Sep 29, 2005
 *
 */
package DADT.IPMulticast;

import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.WeakHashMap;

import DADT.Identificator;
import DADT.IdentifiedADT;
import DADT.internals.RuntimeReference;

/**
 * This class implements identificators using a random number generator for the data ADT instance and the IP address
 * to identify the spaceADT on which the dataADT resides. 
 * In this case the identifier is sufficient to route the request directly toward the adtInstance 
 * (in according for example to the IPMIterator and the IPMSome
 * operators, however this is not a requirement of the Identificator interface.
 *  
 * @author migliava
 */
public class IPIdentificator implements Identificator {

    //We need to use maps as it should be fast in both ways (and also the code is nicer ;-)
    //we need to go fromIDtoADTs whenever we use an operator that address the single instance
    //we need to go fromADTsToIDs whenever we return values from invocation (as IDs are contained in ResultData)
    
	Map fromIDtoADTs = new HashMap();
    Map fromADTsToIDs = new HashMap();
    
    //Random r = new Random();
    long r = 0;
    InetSocketAddress siteAddr;
    
    //TODO should we get the identifier from the local site?
    //It would be better in accordance with the model.
    //I.e. we could get a spaceADT and use the Host.getID to get the identifier of the site.
    //At this point can this class moved in the upper DADT layer?
    public IPIdentificator(InetSocketAddress addr) {
        super();
        this.siteAddr = addr;
    }
    

    /**
     * @see Identificator
     */
    public Serializable getID(Object adtInstance) {

        RuntimeReference myWeakReference = new RuntimeReference(adtInstance);
        
        if (fromADTsToIDs.containsKey(myWeakReference))
            return (Serializable) fromADTsToIDs.get(myWeakReference);
        else {
            IPIdentifier identifier = new IPIdentifier(siteAddr,r++);
            fromADTsToIDs.put(myWeakReference,identifier);
            fromIDtoADTs.put(identifier,myWeakReference);
        }
        
        return (Serializable) fromADTsToIDs.get(myWeakReference);
            
//        IPIdentifier identifier = null;
//
//        if (!fromIDtoADTs.containsValue(myWeakReference)) {
//            identifier = new IPIdentifier(siteAddr,r.nextLong());
//            fromIDtoADTs.put(identifier,myWeakReference);
//        } else {
//            Iterator iter = fromIDtoADTs.entrySet().iterator() ;
//            while (iter.hasNext() && identifier == null) {
//                Map.Entry el = (Map.Entry) iter.next();
//                if ( el.getValue().equals(myWeakReference) ) {
//                    identifier = (IPIdentifier) el.getKey();
//                }
//            };
//        }
//        return identifier;
    }

    public Object getADT(Serializable adtIdentifier) {
        //System.out.println(fromIDtoADTs);
        //System.out.println(adtIdentifier);
        Object object = fromIDtoADTs.get(adtIdentifier);
        //System.out.println(new Boolean(fromIDtoADTs.containsKey(adtIdentifier)).toString()+object);
        return ((Reference) object).get();
    }

    
    public Collection getIdentifiedADTs(Collection adtCollection) {
        Collection result = new ArrayList();
            for (Iterator iter = adtCollection.iterator(); iter.hasNext();) {
                Object el = (Object) iter.next();
                result.add(new IdentifiedADT(el,getID(el)));
            }
            return result;
    }

    public Collection getIDs(Collection adtCollection) {
            Collection result = new ArrayList();
            for (Iterator iter = adtCollection.iterator(); iter.hasNext();) {
                Object el = (Object) iter.next();
                result.add(getID(el));
            }
            return result;
    }
    
}
