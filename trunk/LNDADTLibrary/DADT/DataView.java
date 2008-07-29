/*
 * Created on Aug 31, 2004
 *
 */

package DADT;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author migliava
 */
public class DataView implements Serializable {

    ExpressionTree properties;
    
    //public String[] LNproperties;
    
    public DataView(ExpressionTree e) {
        properties = e;
        //LNproperties = getLNProperties();
    }
    
    /**
     * Creates a new DataView by adding dv in and 
     * @param dv
     * @return this after the modification
     */
    public DataView and(DataView dv) {
        if (properties.getDADTClass().equals(dv.properties.getDADTClass())) { 
            properties = this.properties.and(dv.properties);
        }
        return this;
    }

    public boolean isMember(Object adtInstance) {
       
    	return properties.evaluateTree(adtInstance);
    }

    /**
     * @param instances
     * @return
     */
    public Collection filterMatchingInstances(Collection instances) {

            Collection res = Functions.filter(instances,new Predicate() {

                public boolean evaluate(Object ADTInstance) {
                    return isMember(ADTInstance);
                }
            });
            return res;
    }
    
	Class getADTClass() {
	    return getADTClassFromDADT(this.getClass());
	}
	
    public Class getDADTClass() {
        return properties.getDADTClass();
    }
    
    static Class getADTClassFromDADT(Class DADTClass) {
        try {
            return (Class) DADTClass.getField("distributes").get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}