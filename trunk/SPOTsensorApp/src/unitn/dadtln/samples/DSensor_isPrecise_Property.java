package unitn.dadtln.samples;

import java.io.DataOutputStream;
import java.io.IOException;

import polimi.ln.neighborhoodDefs.DoubleSimplePredicate;
import polimi.ln.neighborhoodDefs.Predicate;
import unitn.dadt.internals.Property;

public class DSensor_isPrecise_Property implements  Property {

	private double precision;

	
	public DSensor_isPrecise_Property(double precision)  {
		this.precision = precision;

	}
	public boolean evaluate(Object o) {
		Sensor local = (Sensor) o;
		return local.precision > precision;
	
	}

	// TODO: to be added by JADT preprocessor
	public String getDADTClass() {
		return "DSensor";
	}
	
	/*
	 * To be used later in LN simulation to create predicates
	 */
	
	public Predicate getDescriptionForLN() {
		return(new DoubleSimplePredicate("precision", DoubleSimplePredicate.GREATER_THAN, this.precision));
		
	}

    public void serialize(DataOutputStream stream) throws IOException {
    	
    	stream.writeUTF("DSensor_isPrecise_Property");
    	stream.writeUTF(Double.toString(this.precision));
    }
    
	public Property deserialize(String strValue){
		return new DSensor_isPrecise_Property(Double.valueOf(strValue).doubleValue());
	}
    
}

