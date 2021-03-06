/*
 * Created on Sep 20, 2005
 *
 */
package DADT;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;

import DADT.internals.RuntimeReference;

/**
 * This class is used to register ADTs local to a Site
 * @author migliava
 */
public class BindingRegistry {
    TreeMap registry = new TreeMap();

    /**
     * Gets instances registered under a given DADTClass
     * @param DADTClass
     * @return
     */
    public Collection getLocalInstances(String DADTClassName) {
        //String DADTClassName = DADTClass.getName();
    	Collection objects,notNulls;
    	Collection collection = (Collection) registry.get(DADTClassName);
        if (collection!=null) {
            //System.out.println("size before:"+collection.size());
            objects = Functions.transform(collection,new Transformation() {
                public Object transform(Object o) {
                    return ( ((RuntimeReference)o).get());
                }
            });
            //filter out dead weak references
            //FIXME: they still remain in the table!
            notNulls = Functions.filter(objects,new Predicate() {
                public boolean evaluate(Object o) {
                    return (o!=null);
                }
            });
            
            return notNulls;
        } else
            return new ArrayList();
    }

    /**
     * Register adtInstance into the regsitry under the DADTClass class.
     * Actually it uses WeakReferences. This method assumes that register is NEVER called
     * twice with the same adtInstance on the same DADTClassName as in that case invocation on that
     * adtInstance are undefined (currently as we use a list invocation are duplicated, an should we use
     * a set we should use a mechanism similar to Identificator's object maps, beware that hashcode
     *  
     * @param adtInstance
     * @param DADTClassName
     */
    public void register(Object adtInstance, String DADTClassName) {

    	if (registry.containsKey(DADTClassName)) {
    		((Collection) registry.get(DADTClassName))
    				.add(new RuntimeReference(adtInstance));
    	} else { 
    	    HashSet  a = new HashSet();
    	    a.add(new RuntimeReference(adtInstance));
    	    registry.put(DADTClassName,a);
    	}
    }
    
    /**
     * Removes adtInstance from the regsitry.
     * Currently uses WeakReferences.
     * 
     * @param adtInstance
     * @param DADTClass
     */
    public void unregister(Object adtInstance, String DADTClassName) {

        Collection c = (Collection) registry.get(DADTClassName);//info.getDADTinstances();
		Iterator iter = c.iterator();
		while (iter.hasNext()) {
            RuntimeReference weakRef = (RuntimeReference) iter.next();
            if (weakRef.get()==adtInstance) iter.remove();
        }
    }
}
