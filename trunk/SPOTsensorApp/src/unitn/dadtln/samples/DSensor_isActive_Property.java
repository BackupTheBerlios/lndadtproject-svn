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
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import polimi.ln.neighborhoodDefs.BooleanSimplePredicate;
import polimi.ln.neighborhoodDefs.Predicate;
import unitn.dadt.internals.Property;

public class DSensor_isActive_Property implements Property {	
	
	public DSensor_isActive_Property()  {
		
	}
	
	public boolean evaluate(Object o) {
		Sensor local = (Sensor) o;
	
		return local.isActive();
	
	}

	// TODO: to be added by JADT preprocessor
	public String getDADTClass() {
		return "DSensor";
	}
	
	/*
	 * To be used later in LN simulation to create predicates
	 */
	
	public Predicate getDescriptionForLN() {
		return(new BooleanSimplePredicate ("isActive", BooleanSimplePredicate.EQUAL, true));
	} 
	
    public void serialize(DataOutputStream stream) throws IOException {
    	stream.writeUTF("DSensor_isActive_Property");
    	stream.writeUTF("");
    }
    
	public Property deserialize(String strValue){
		return new DSensor_isActive_Property();
	}
}
