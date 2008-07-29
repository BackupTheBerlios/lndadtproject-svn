package unitn.dadtln.samples;
import java.io.Serializable;
import java.util.ArrayList;

import polimi.ln.neighborhoodDefs.AtomicPredicate;
import polimi.ln.neighborhoodDefs.BooleanSimplePredicate;
import polimi.ln.neighborhoodDefs.DoubleSimplePredicate;



public class DSensor_isPrecise_Property implements Serializable, DADT.Property {

	public Class getDADTClass() {
		return DSensor.class;
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
	//TODO: to be added in DADT preprocessing
	/*
	 * To be used later in LN simulation to create predicates
	 */
	public AtomicPredicate getDescriptionForLN(Object o) {
		return(new DoubleSimplePredicate("precision", DoubleSimplePredicate.GREATER_THAN, this.precision));
		
	}

	public String toString() {
		return "isPrecise_Property (precision is " + precision + ")";
	}
	
    public String getClassName()
    {
    	return "precision";
    }
}

