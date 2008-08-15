package unitn.dadtln.samples;


import polimi.ln.neighborhoodDefs.IntegerSimplePredicate;
import polimi.ln.neighborhoodDefs.Predicate;
import unitn.dadt.internals.Property;


public class DSensor_isOfType_Property implements Property {

	public Class getDADTClass() {
		return null;//return DSensor.class;
	}
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

	/*
	 * To be used later in LN simulation to create predicates
	 */
	
	public Predicate getDescriptionForLN() {
		
/*
		return(new SetMembershipPredicate(Sensor.typeToStr(type), 
							SetMembershipPredicate.IS_IN,
							"OnBoardSensors"));
*/

		return( new IntegerSimplePredicate ("type", IntegerSimplePredicate.EQUAL, type));
	} 
	
    public String toString() {
        return "DSensor_isOfType_Property";
    }
    
    public String getClassName()
    {
    	return "DSensor_isOfType_Property";
    }
}
