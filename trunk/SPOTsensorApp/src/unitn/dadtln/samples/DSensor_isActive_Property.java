package unitn.dadtln.samples;

/* javaME doesn't support these
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
*/

/*
import polimi.ln.neighborhoodDefs.AtomicPredicate;
import polimi.ln.neighborhoodDefs.BooleanSimplePredicate;
*/
import polimi.ln.neighborhoodDefs.BooleanSimplePredicate;
import polimi.ln.neighborhoodDefs.Predicate;
import unitn.dadt.internals.Property;

public class DSensor_isActive_Property implements Property {	
	
	
	public Class getDADTClass() {
		return null; //return DSensor.class;
	}
	
	
		public DSensor_isActive_Property()  {
		
	}
	
	public boolean evaluate(Object o) {
		Sensor local = (Sensor) o;
	
		return local.isActive();
	
	}

	/*
	 * To be used later in LN simulation to create predicates
	 */
	
	public Predicate getDescriptionForLN() {
		
		return(new BooleanSimplePredicate ("isActive", BooleanSimplePredicate.EQUAL, true));
		
	} 
	
	
    public String toString() {
        return "DSensor_isActive_Property";
    }
    
    public String getClassName()
    {
    	return "isActive";
    }
}
