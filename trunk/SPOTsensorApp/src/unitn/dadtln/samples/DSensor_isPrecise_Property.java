package unitn.dadtln.samples;

import polimi.ln.neighborhoodDefs.DoubleSimplePredicate;
import polimi.ln.neighborhoodDefs.Predicate;
import unitn.dadt.internals.Property;



public class DSensor_isPrecise_Property implements  Property {

	public Class getDADTClass() {
		return null;
		//return DSensor.class;
	}
	private double precision;

	
	public DSensor_isPrecise_Property(double precision)  {
		this.precision=precision;

	}
	public boolean evaluate(Object o) {
		Sensor local = (Sensor) o;

	{
		return local.precision > precision;
	}
	}
	
	
	/*
	 * To be used later in LN simulation to create predicates
	 */
	
	public Predicate getDescriptionForLN() {
		return(new DoubleSimplePredicate("precision", DoubleSimplePredicate.GREATER_THAN, this.precision));
		
	}

	
	public String toString() {
		return "DSensor_isPrecise_Property";
	}
	
    public String getClassName()
    {
    	return "precision";
    }
}

