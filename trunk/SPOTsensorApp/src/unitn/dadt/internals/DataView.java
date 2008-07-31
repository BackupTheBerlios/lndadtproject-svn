/*
 * 31/08/04, Migliava, Created
 * 27/07/08, Khasanova, Changed to be used on Sun SPOTs under CLDC specification
 */
package unitn.dadt.internals;

import java.util.Vector;

/**
 * @author migliava
 */
public class DataView /* implements Serializable */{

    ExpressionTree properties;
    
    public DataView(ExpressionTree e) {
        properties = e;
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
    public Vector filterMatchingInstances(Vector instances) {

        Vector res = Functions.filter(instances,new Predicate() {

            public boolean evaluate(Object ADTInstance) {
                return isMember(ADTInstance);
            }
        });
        return res;
}    
    
    /*
	Class getADTClass() {
	    return getADTClassFromDADT(this.getClass());
	}
	
    public Class getDADTClass() {
        return properties.getDADTClass();
    }
    
    static Class getADTClassFromDADT(Class DADTClass) {
        try {
        	return null;
        	//return (Class) DADTClass.getField("distributes").get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    */
}