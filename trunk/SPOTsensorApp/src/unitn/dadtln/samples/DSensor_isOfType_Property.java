package unitn.dadtln.samples;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;

import polimi.ln.neighborhoodDefs.IntegerSimplePredicate;
import polimi.ln.neighborhoodDefs.Predicate;
import unitn.dadt.internals.Property;


public class DSensor_isOfType_Property implements Property {

	private int type;
		
	public DSensor_isOfType_Property(int type)  {
		this.type = type;

	}
	public boolean evaluate(Object o) {
		Sensor local = (Sensor) o;

		{
			return local.type == type;
		}
	}

	// TODO: to be added by JADT preprocessor
	public String getDADTClass() {
		return "DSensor";
	}
	
	/*
	 * To be used later in LN simulation to create predicates
	 */
	
	public Predicate getDescriptionForLN() {
		
		return( new IntegerSimplePredicate ("type", IntegerSimplePredicate.EQUAL, type));
	} 

	public void serialize(DataOutputStream stream) throws IOException {
    	stream.writeUTF("DSensor_isOfType_Property");
    	stream.writeUTF(Integer.toString(this.type));
	}
    
	public Property deserialize(String strValue){
		return new DSensor_isOfType_Property(Integer.valueOf(strValue).intValue());
	}
    
}
