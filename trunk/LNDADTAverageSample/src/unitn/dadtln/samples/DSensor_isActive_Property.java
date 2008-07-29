package unitn.dadtln.samples;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import polimi.ln.neighborhoodDefs.AtomicPredicate;
import polimi.ln.neighborhoodDefs.BooleanSimplePredicate;


public class DSensor_isActive_Property implements Serializable, DADT.Property {	
	
	public Class getDADTClass() {
		return DSensor.class;
	}
	
	public DSensor_isActive_Property()  {
		
	}
	
	public boolean evaluate(Object o) {
		Sensor local = (Sensor) o;
	
		return local.isActive();
	
	}

	//TODO: to be added in DADT preprocessing
	/*
	 * To be used later in LN simulation to create predicates
	 */
	public AtomicPredicate getDescriptionForLN(Object o) {
		
		return(new BooleanSimplePredicate ("isActive", BooleanSimplePredicate.EQUAL, true));
		
	} 

    public String toString() {
        return "isActive_Property ";
    }
    
    public String getClassName()
    {
    	return "isActive";
    }
}
